package speiger.src.collections.shorts.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.shorts.functions.consumer.ShortIntConsumer;
import speiger.src.collections.shorts.functions.function.Short2IntFunction;
import speiger.src.collections.shorts.functions.function.ShortIntUnaryOperator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.maps.abstracts.AbstractShort2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.shorts.maps.interfaces.Short2IntNavigableMap;
import speiger.src.collections.shorts.sets.AbstractShortSet;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.utils.maps.Short2IntMaps;
import speiger.src.collections.ints.collections.AbstractIntCollection;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.ints.functions.IntSupplier;
import speiger.src.collections.ints.collections.IntBidirectionalIterator;
import speiger.src.collections.ints.functions.function.IntIntUnaryOperator;
import speiger.src.collections.ints.functions.IntConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectIntConsumer;
import speiger.src.collections.ints.functions.function.Int2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;

/**
 * A Simple Type Specific RB TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 */
public class Short2IntRBTreeMap extends AbstractShort2IntMap implements Short2IntNavigableMap
{
	/** The center of the Tree */
	protected transient Node tree;
	/** The Lowest possible Node */
	protected transient Node first;
	/** The Highest possible Node */
	protected transient Node last;
	/** The amount of elements stored in the Map */
	protected int size = 0;
	/** The Sorter of the Tree */
	protected transient ShortComparator comparator;
	
	/** the default return value for max searches */
	protected short defaultMaxNotFound = Short.MIN_VALUE;
	/** the default return value for min searches */
	protected short defaultMinNotFound = Short.MAX_VALUE;
	
	/** KeySet Cache */
	protected ShortNavigableSet keySet;
	/** Values Cache */
	protected IntCollection values;
	/** EntrySet Cache */
	protected ObjectSet<Short2IntMap.Entry> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Short2IntRBTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Short2IntRBTreeMap(ShortComparator comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Short2IntRBTreeMap(Short[] keys, Integer[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Short2IntRBTreeMap(Short[] keys, Integer[] values, ShortComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].shortValue(), values[i].intValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Short2IntRBTreeMap(short[] keys, int[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Short2IntRBTreeMap(short[] keys, int[] values, ShortComparator comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Short2IntRBTreeMap(Map<? extends Short, ? extends Integer> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Short2IntRBTreeMap(Map<? extends Short, ? extends Integer> map, ShortComparator comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Short2IntRBTreeMap(Short2IntMap map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Short2IntRBTreeMap(Short2IntMap map, ShortComparator comp) {
		comparator = comp;
		putAll(map);
	}

	@Override
	public void setDefaultMaxValue(short value) { defaultMaxNotFound = value; }
	@Override
	public short getDefaultMaxValue() { return defaultMaxNotFound; }
	@Override
	public void setDefaultMinValue(short value) { defaultMinNotFound = value; }
	@Override
	public short getDefaultMinValue() { return defaultMinNotFound; }
	
	
	@Override
	public int put(short key, int value) {
		if(tree == null) {
			tree = first = last = new Node(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) return parent.setValue(value);
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Node adding = new Node(key, value, parent);
		if(compare < 0)  {
			parent.left = adding;
			if(parent == first)	first = adding;
		}
		else {
			parent.right = adding;
			if(parent == last) last = adding;
		}
		fixAfterInsertion(adding);
		size++;
		return getDefaultReturnValue();
	}
	
	@Override
	public int putIfAbsent(short key, int value) {
		if(tree == null) {
			tree = first = last = new Node(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) return parent.value;
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Node adding = new Node(key, value, parent);
		if(compare < 0)  {
			parent.left = adding;
			if(parent == first)	first = adding;
		}
		else  {
			parent.right = adding;
			if(parent == last) last = adding;
		}
		fixAfterInsertion(adding);
		size++;
		return getDefaultReturnValue();
	}
	
	@Override
	public int addTo(short key, int value) {
		if(tree == null) {
			tree = first = last = new Node(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0) return parent.addTo(value);
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		Node adding = new Node(key, value, parent);
		if(compare < 0)  {
			parent.left = adding;
			if(parent == first)	first = adding;
		}
		else  {
			parent.right = adding;
			if(parent == last) last = adding;
		}
		fixAfterInsertion(adding);
		size++;
		return getDefaultReturnValue();
	}
	
	@Override
	public int subFrom(short key, int value) {
		if(tree == null) return getDefaultReturnValue();
		int compare = 0;
		Node parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0)
			{
				int oldValue = parent.subFrom(value);
				if(value < 0 ? (parent.value >= getDefaultReturnValue()) : (parent.value <= getDefaultReturnValue())) removeNode(parent);
				return oldValue;
			}
			if(compare < 0) {
				if(parent.left == null) break;
				parent = parent.left;
			}
			else if(compare > 0) {
				if(parent.right == null) break;
				parent = parent.right;
			}
		}
		return getDefaultReturnValue();
	}
	
	@Override
	public ShortComparator comparator() { return comparator; }

	@Override
	public boolean containsKey(short key) {
		return findNode(key) != null;
	}
	
	@Override
	public int get(short key) {
		Node node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public int getOrDefault(short key, int defaultValue) {
		Node node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public short firstShortKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public short pollFirstShortKey() {
		if(tree == null) return getDefaultMinValue();
		short result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public short lastShortKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public short pollLastShortKey() {
		if(tree == null) return getDefaultMaxValue();
		short result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Short2IntMap.Entry firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Short2IntMap.Entry lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Short2IntMap.Entry pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Short2IntMap.Entry pollLastEntry() {
		if(tree == null) return null;
		BasicEntry entry = last.export();
		removeNode(last);
		return entry;
	}
	
	@Override
	public int firstIntValue() {
		if(tree == null) throw new NoSuchElementException();
		return first.value;
	}
	
	@Override
	public int lastIntValue() {
		if(tree == null) throw new NoSuchElementException();
		return last.value;
	}
	
	@Override
	public int remove(short key) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		int value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public int removeOrDefault(short key, int defaultValue) {
		Node entry = findNode(key);
		if(entry == null) return defaultValue;
		int value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(short key, int value) {
		Node entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(short key, int oldValue, int newValue) {
		Node entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public int replace(short key, int value) {
		Node entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		int oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public int computeInt(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			int newValue = mappingFunction.applyAsInt(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		int newValue = mappingFunction.applyAsInt(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public int computeIntIfAbsent(short key, Short2IntFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null) {
			int newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			int newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public int supplyIntIfAbsent(short key, IntSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		Node entry = findNode(key);
		if(entry == null) {
			int newValue = valueProvider.getInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(entry.value == getDefaultReturnValue()) {
			int newValue = valueProvider.getInt();
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public int computeIntIfPresent(short key, ShortIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
		int newValue = mappingFunction.applyAsInt(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public int mergeInt(short key, int value, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		Node entry = findNode(key);
		int newValue = entry == null || entry.value == getDefaultReturnValue() ? value : mappingFunction.applyAsInt(entry.value, value);
		if(newValue == getDefaultReturnValue()) {
			if(entry != null)
				removeNode(entry);
		}
		else if(entry == null) put(key, newValue);
		else entry.value = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllInt(Short2IntMap m, IntIntUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Short2IntMap.Entry entry : Short2IntMaps.fastIterable(m)) {
			short key = entry.getShortKey();
			Node subEntry = findNode(key);
			int newValue = subEntry == null || subEntry.value == getDefaultReturnValue() ? entry.getIntValue() : mappingFunction.applyAsInt(subEntry.value, entry.getIntValue());
			if(newValue == getDefaultReturnValue()) {
				if(subEntry != null)
					removeNode(subEntry);
			}
			else if(subEntry == null) put(key, newValue);
			else subEntry.value = newValue;
		}
	}
	
	@Override
	public void forEach(ShortIntConsumer action) {
		for(Node entry = first;entry != null;entry = entry.next())
			action.accept(entry.key, entry.value);
	}
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() {
		size = 0;
		first = null;
		last = null;
		tree = null;
	}
	
	protected ShortBidirectionalIterator keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected ShortBidirectionalIterator keyIterator(short element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected ShortBidirectionalIterator descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Short2IntRBTreeMap copy() {
		Short2IntRBTreeMap set = new Short2IntRBTreeMap();
		set.size = size;
		if(tree != null) {
			set.tree = tree.copy();
			Node lastFound = null;
			for(Node entry = tree;entry != null;entry = entry.left) lastFound = entry;
			set.first = lastFound;
			lastFound = null;
			for(Node entry = tree;entry != null;entry = entry.right) lastFound = entry;
			set.last = lastFound;
		}
		return set;
	}
	
	@Override
	public ShortNavigableSet keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public IntCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public ShortNavigableSet navigableKeySet() {
		if(keySet == null) keySet = new KeySet(this);
		return keySet;
	}
	
	@Override
	public Short2IntNavigableMap descendingMap() {
		return new DescendingNaivgableSubMap(this, true, (short)0, true, true, (short)0, true);
	}
	
	@Override
	public ShortNavigableSet descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Short2IntNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Short2IntNavigableMap headMap(short toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, true, (short)0, true, false, toKey, inclusive);
	}
	
	@Override
	public Short2IntNavigableMap tailMap(short fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap(this, false, fromKey, inclusive, true, (short)0, true);
	}
	
	@Override
	public short lowerKey(short e) {
		Node node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public short floorKey(short e) {
		Node node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public short higherKey(short e) {
		Node node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public short ceilingKey(short e) {
		Node node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Short2IntMap.Entry lowerEntry(short key) {
		Node node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Short2IntMap.Entry higherEntry(short key) {
		Node node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Short2IntMap.Entry floorEntry(short key) {
		Node node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Short2IntMap.Entry ceilingEntry(short key) {
		Node node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node findLowerNode(short key) {
		Node entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) > 0) {
				if(entry.right != null) entry = entry.right;
				else return entry;
			}
			else {
				if(entry.left != null) entry = entry.left;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.left == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
		}
		return null;
	}
	
	protected Node findFloorNode(short key) {
		Node entry = tree;
		int compare;
		while(entry != null) {
			if((compare = compare(key, entry.key)) > 0) {
				if(entry.right == null) break;
				entry = entry.right;
				continue;
			}
			else if(compare < 0) {
				if(entry.left != null) entry = entry.left;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.left == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
				continue;
			}
			break;
		}
		return entry;
	}
	
	protected Node findCeilingNode(short key) {
		Node entry = tree;
		int compare;
		while(entry != null) {
			if((compare = compare(key, entry.key)) < 0) {
				if(entry.left == null) break;
				entry = entry.left;
				continue;
			}
			else if(compare > 0) {
				if(entry.right != null) entry = entry.right;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.right == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
				continue;
			}
			break;
		}
		return entry;
	}
	
	protected Node findHigherNode(short key) {
		Node entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) < 0) {
				if(entry.left != null) entry = entry.left;
				else return entry;
			}
			else {
				if(entry.right != null) entry = entry.right;
				else {
					Node parent = entry.parent;
					while(parent != null && parent.right == entry) {
						entry = parent;
						parent = parent.parent;
					}
					return parent;
				}
			}
		}
		return null;
	}
	
	protected Node findNode(short key) {
		Node node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(key, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected void removeNode(Node entry) {
		size--;
		if(entry.needsSuccessor()) {
			Node successor = entry.next();
			entry.key = successor.key;
			entry.value = successor.value;
			entry = successor;
		}
		Node replacement = entry.left != null ? entry.left : entry.right;
		if(replacement != null) {
			if(entry.replace(replacement)) tree = replacement;
			if(entry == first) first = replacement;
			if(entry == last) last = entry.right != null ? entry.right : replacement;
			entry.left = entry.right = entry.parent = null;
			if(entry.isBlack()) fixAfterDeletion(replacement);
		}
		else if(entry.parent == null) tree = first = last = null;
		else {
			if(entry.isBlack())
				fixAfterDeletion(entry);
			entry.replace(null);
			if(entry.parent != null) {
				Node parent = entry.parent;
				if(entry == first) first = parent.left != null ? parent.left : parent;
				if(entry == last) last = entry.right != null ? parent.right : parent;
			}
			entry.parent = null;
		}
	}
	
	protected void validate(short k) { compare(k, k); }
	protected int compare(short k, short v) { return comparator != null ? comparator.compare(k, v) : Short.compare(k, v);}
	protected static boolean isBlack(Node p) { return p == null || p.isBlack(); }
	protected static Node parentOf(Node p) { return (p == null ? null : p.parent); }
	protected static void setBlack(Node p, boolean c) { if(p != null) p.setBlack(c); }
	protected static Node leftOf(Node p) { return p == null ? null : p.left; }
	protected static Node rightOf(Node p) { return (p == null) ? null : p.right; }
	
	protected void rotateLeft(Node entry) {
		if(entry != null) {
			Node right = entry.right;
			entry.right = right.left;
			if(right.left != null) right.left.parent = entry;
			right.parent = entry.parent;
			if(entry.parent == null) tree = right;
			else if(entry.parent.left == entry) entry.parent.left = right;
			else entry.parent.right = right;
			right.left = entry;
			entry.parent = right;
		}
	}
	
	protected void rotateRight(Node entry) {
		if(entry != null) {
			Node left = entry.left;
			entry.left = left.right;
			if(left.right != null) left.right.parent = entry;
			left.parent = entry.parent;
			if(entry.parent == null) tree = left;
			else if(entry.parent.right == entry) entry.parent.right = left;
			else entry.parent.left = left;
			left.right = entry;
			entry.parent = left;
		}
	}
	
	protected void fixAfterInsertion(Node entry) {
		entry.setBlack(false);
		while(entry != null && entry != tree && !entry.parent.isBlack()) {
			if(parentOf(entry) == leftOf(parentOf(parentOf(entry)))) {
				Node y = rightOf(parentOf(parentOf(entry)));
				if(!isBlack(y)) {
					setBlack(parentOf(entry), true);
					setBlack(y, true);
					setBlack(parentOf(parentOf(entry)), false);
					entry = parentOf(parentOf(entry));
				}
				else {
					if(entry == rightOf(parentOf(entry))) {
						entry = parentOf(entry);
						rotateLeft(entry);
					}
					setBlack(parentOf(entry), true);
					setBlack(parentOf(parentOf(entry)), false);
					rotateRight(parentOf(parentOf(entry)));
				}
			}
			else {
				Node y = leftOf(parentOf(parentOf(entry)));
				if(!isBlack(y)) {
					setBlack(parentOf(entry), true);
					setBlack(y, true);
					setBlack(parentOf(parentOf(entry)), false);
					entry = parentOf(parentOf(entry));
				}
				else {
					if(entry == leftOf(parentOf(entry))) {
						entry = parentOf(entry);
						rotateRight(entry);
					}
					setBlack(parentOf(entry), true);
					setBlack(parentOf(parentOf(entry)), false);
					rotateLeft(parentOf(parentOf(entry)));
				}
			}
		}
		tree.setBlack(true);
	}
	
	protected void fixAfterDeletion(Node entry) {
		while(entry != tree && isBlack(entry)) {
			if(entry == leftOf(parentOf(entry))) {
				Node sib = rightOf(parentOf(entry));
				if(!isBlack(sib)) {
					setBlack(sib, true);
					setBlack(parentOf(entry), false);
					rotateLeft(parentOf(entry));
					sib = rightOf(parentOf(entry));
				}
				if(isBlack(leftOf(sib)) && isBlack(rightOf(sib))) {
					setBlack(sib, false);
					entry = parentOf(entry);
				}
				else {
					if(isBlack(rightOf(sib))) {
						setBlack(leftOf(sib), true);
						setBlack(sib, false);
						rotateRight(sib);
						sib = rightOf(parentOf(entry));
					}
					setBlack(sib, isBlack(parentOf(entry)));
					setBlack(parentOf(entry), true);
					setBlack(rightOf(sib), true);
					rotateLeft(parentOf(entry));
					entry = tree;
				}
			}
			else {
				Node sib = leftOf(parentOf(entry));
				if(!isBlack(sib)) {
					setBlack(sib, true);
					setBlack(parentOf(entry), false);
					rotateRight(parentOf(entry));
					sib = leftOf(parentOf(entry));
				}
				if(isBlack(rightOf(sib)) && isBlack(leftOf(sib))) {
					setBlack(sib, false);
					entry = parentOf(entry);
				}
				else {
					if(isBlack(leftOf(sib))) {
						setBlack(rightOf(sib), true);
						setBlack(sib, false);
						rotateLeft(sib);
						sib = leftOf(parentOf(entry));
					}
					setBlack(sib, isBlack(parentOf(entry)));
					setBlack(parentOf(entry), true);
					setBlack(leftOf(sib), true);
					rotateRight(parentOf(entry));
					entry = tree;
				}
			}
		}
		setBlack(entry, true);
	}
	
	static class KeySet extends AbstractShortSet implements ShortNavigableSet
	{
		Short2IntNavigableMap map;

		public KeySet(Short2IntNavigableMap map) {
			this.map = map;
		}
		
		@Override
		public void setDefaultMaxValue(short e) { map.setDefaultMaxValue(e); }
		@Override
		public short getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(short e) { map.setDefaultMinValue(e); }
		@Override
		public short getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public short lower(short e) { return map.lowerKey(e); }
		@Override
		public short floor(short e) { return map.floorKey(e); }
		@Override
		public short ceiling(short e) { return map.ceilingKey(e); }
		@Override
		public short higher(short e) { return map.higherKey(e); }
		
		@Override
		public Short lower(Short e) {
			Short2IntMap.Entry node = map.lowerEntry(e.shortValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Short floor(Short e) {
			Short2IntMap.Entry node = map.floorEntry(e.shortValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Short higher(Short e) {
			Short2IntMap.Entry node = map.higherEntry(e.shortValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public Short ceiling(Short e) {
			Short2IntMap.Entry node = map.ceilingEntry(e.shortValue());
			return node != null ? node.getKey() : null;
		}
		
		@Override
		public short pollFirstShort() { return map.pollFirstShortKey(); }
		@Override
		public short pollLastShort() { return map.pollLastShortKey(); }
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		@Override
		public short firstShort() { return map.firstShortKey(); } 
		@Override
		public short lastShort() { return map.lastShortKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(short o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(short e) { throw new UnsupportedOperationException(); }
		@Override
		public ShortBidirectionalIterator iterator(short fromElement) {
			if(map instanceof Short2IntRBTreeMap) return ((Short2IntRBTreeMap)map).keyIterator(fromElement);
			return ((NavigableSubMap)map).keyIterator(fromElement);
		}
		
		@Override
		public ShortNavigableSet subSet(short fromElement, boolean fromInclusive, short toElement, boolean toInclusive) { return new KeySet(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public ShortNavigableSet headSet(short toElement, boolean inclusive) { return new KeySet(map.headMap(toElement, inclusive)); }
		@Override
		public ShortNavigableSet tailSet(short fromElement, boolean inclusive) { return new KeySet(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public ShortBidirectionalIterator iterator() {
			if(map instanceof Short2IntRBTreeMap) return ((Short2IntRBTreeMap)map).keyIterator();
			return ((NavigableSubMap)map).keyIterator();
		}
		
		@Override
		public ShortBidirectionalIterator descendingIterator() {
			if(map instanceof Short2IntRBTreeMap) return ((Short2IntRBTreeMap)map).descendingKeyIterator();
			return ((NavigableSubMap)map).descendingKeyIterator();
		}
		
		protected Node start() {
			if(map instanceof Short2IntRBTreeMap) return ((Short2IntRBTreeMap)map).first;
			return ((NavigableSubMap)map).subLowest();
		}
		
		protected Node end() {
			if(map instanceof Short2IntRBTreeMap) return null;
			return ((NavigableSubMap)map).subHighest();
		}
		
		protected Node next(Node entry) {
			if(map instanceof Short2IntRBTreeMap) return entry.next();
			return ((NavigableSubMap)map).next(entry);
		}
		
		protected Node previous(Node entry) {
			if(map instanceof Short2IntRBTreeMap) return entry.previous();
			return ((NavigableSubMap)map).previous(entry);
		}
		
		@Override
		public ShortNavigableSet descendingSet() { return new KeySet(map.descendingMap()); }
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(ShortConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.get(entry.key)) return false;
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.applyAsShort(state, entry.key);
			return state;
		}
		
		@Override
		public short reduce(ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = (short)0;
			boolean empty = true;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.applyAsShort(state, entry.key);
			}
			return state;
		}
		
		@Override
		public short findFirst(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) return entry.key;
			return (short)0;
		}
		
		@Override
		public int count(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.get(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap extends NavigableSubMap
	{
		AscendingNaivgableSubMap(Short2IntRBTreeMap map, boolean fromStart, short lo, boolean loInclusive, boolean toEnd, short hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Short2IntNavigableMap descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public ShortNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public ShortNavigableSet keySet() {
			return navigableKeySet();
		}
		
		@Override
		public Short2IntNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Short2IntNavigableMap headMap(short toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Short2IntNavigableMap tailMap(short fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node subLowest() { return absLowest(); }
		@Override
		protected Node subHighest() { return absHighest(); }
		@Override
		protected Node subCeiling(short key) { return absCeiling(key); }
		@Override
		protected Node subHigher(short key) { return absHigher(key); }
		@Override
		protected Node subFloor(short key) { return absFloor(key); }
		@Override
		protected Node subLower(short key) { return absLower(key); }
		
		@Override
		protected ShortBidirectionalIterator keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected ShortBidirectionalIterator keyIterator(short element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ShortBidirectionalIterator descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Short2IntMap.Entry> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap extends NavigableSubMap
	{
		ShortComparator comparator;
		DescendingNaivgableSubMap(Short2IntRBTreeMap map, boolean fromStart, short lo, boolean loInclusive, boolean toEnd, short hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = map.comparator() == null ? ShortComparator.of(Collections.reverseOrder()) : map.comparator().reversed();
		}
		
		@Override
		public ShortComparator comparator() { return comparator; }
		
		@Override
		public Short2IntNavigableMap descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public ShortNavigableSet navigableKeySet() {
			if(keySet == null) keySet = new KeySet(this);
			return keySet;
		}
		
		@Override
		public ShortNavigableSet keySet() {
			return navigableKeySet();
		}
		
		@Override
		public Short2IntNavigableMap subMap(short fromKey, boolean fromInclusive, short toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Short2IntNavigableMap headMap(short toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Short2IntNavigableMap tailMap(short fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Short2IntMap.Entry> short2IntEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node subLowest() { return absHighest(); }
		@Override
		protected Node subHighest() { return absLowest(); }
		@Override
		protected Node subCeiling(short key) { return absFloor(key); }
		@Override
		protected Node subHigher(short key) { return absLower(key); }
		@Override
		protected Node subFloor(short key) { return absCeiling(key); }
		@Override
		protected Node subLower(short key) { return absHigher(key); }
		@Override
		protected Node next(Node entry) { return entry.previous(); }
		@Override
		protected Node previous(Node entry) { return entry.next(); }
		
		@Override
		protected ShortBidirectionalIterator keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ShortBidirectionalIterator keyIterator(short element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected IntBidirectionalIterator valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ShortBidirectionalIterator descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Short2IntMap.Entry> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap extends AbstractShort2IntMap implements Short2IntNavigableMap
	{
		final Short2IntRBTreeMap map;
		final short lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Short2IntNavigableMap inverse;
		ShortNavigableSet keySet;
		ObjectSet<Short2IntMap.Entry> entrySet;
		IntCollection values;
		
		NavigableSubMap(Short2IntRBTreeMap map, boolean fromStart, short lo, boolean loInclusive, boolean toEnd, short hi, boolean hiInclusive) {
			if (!fromStart && !toEnd) {
				if (map.compare(lo, hi) > 0) throw new IllegalArgumentException("fromKey > toKey");
			} 
			else {
				if (!fromStart) map.validate(lo);
				if (!toEnd) map.validate(hi);
			}
			this.map = map;
			this.fromStart = fromStart;
			this.lo = lo;
			this.loInclusive = loInclusive;
			this.toEnd = toEnd;
			this.hi = hi;
			this.hiInclusive = hiInclusive;
		}
		
		@Override
		public void setDefaultMaxValue(short e) { map.setDefaultMaxValue(e); }
		@Override
		public short getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(short e) { map.setDefaultMinValue(e); }
		@Override
		public short getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractShort2IntMap setDefaultReturnValue(int v) { 
			map.setDefaultReturnValue(v);
			return this;
		}
		
		@Override
		public int getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		
		@Override
		public IntCollection values() {
			if(values == null) values = new SubMapValues();
			return values;
		}
		
		@Override
		public ShortNavigableSet descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public ShortNavigableSet keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node subLowest();
		protected abstract Node subHighest();
		protected abstract Node subCeiling(short key);
		protected abstract Node subHigher(short key);
		protected abstract Node subFloor(short key);
		protected abstract Node subLower(short key);
		protected abstract ShortBidirectionalIterator keyIterator();
		protected abstract ShortBidirectionalIterator keyIterator(short element);
		protected abstract IntBidirectionalIterator valueIterator();
		protected abstract ShortBidirectionalIterator descendingKeyIterator();
		protected short lowKeyOrNull(Node entry) { return entry == null ? getDefaultMinValue() : entry.key; }
		protected short highKeyOrNull(Node entry) { return entry == null ? getDefaultMaxValue() : entry.key; }
		protected Node next(Node entry) { return entry.next(); }
		protected Node previous(Node entry) { return entry.previous(); }
		
		protected boolean tooLow(short key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(short key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(short key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(short key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(short key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node absLowest() {
			Node e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHighest() {
			Node e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absCeiling(short key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absHigher(short key) {
			if (tooLow(key)) return absLowest();
			Node e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node absFloor(short key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absLower(short key) {
			if (tooHigh(key)) return absHighest();
			Node e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public ShortComparator comparator() { return map.comparator(); }
		
		@Override
		public short pollFirstShortKey() {
			Node entry = subLowest();
			if(entry != null) {
				short result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMinValue();
		}
		
		@Override
		public short pollLastShortKey() {
			Node entry = subHighest();
			if(entry != null) {
				short result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return getDefaultMaxValue();
		}
		
		@Override
		public int firstIntValue() {
			Node entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public int lastIntValue() {
			Node entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.value;
		}
		
		@Override
		public short firstShortKey() {
			Node entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public short lastShortKey() {
			Node entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public int put(short key, int value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public int putIfAbsent(short key, int value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public int addTo(short key, int value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.addTo(key, value);
		}
		
		@Override
		public int subFrom(short key, int value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.subFrom(key, value);
		}
		
		@Override
		public boolean containsKey(short key) { return inRange(key) && map.containsKey(key); }
		
		@Override
		public int computeIntIfPresent(short key, ShortIntUnaryOperator mappingFunction) {
			Objects.requireNonNull(mappingFunction);
			if(!inRange(key)) return getDefaultReturnValue();
			Node entry = map.findNode(key);
			if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
			int newValue = mappingFunction.apply(key, entry.value);
			if(newValue == getDefaultReturnValue()) {
				map.removeNode(entry);
				return newValue;
			}
			entry.value = newValue;
			return newValue;
		}
		
		@Override
		public int remove(short key) {
			return inRange(key) ? map.remove(key) : getDefaultReturnValue();
		}
		
		@Override
		public int removeOrDefault(short key, int defaultValue) {
			return inRange(key) ? map.removeOrDefault(key, defaultValue) : defaultValue;
		}
		
		@Override
		public boolean remove(short key, int value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public int get(short key) {
			return inRange(key) ? map.get(key) : getDefaultReturnValue();
		}
		
		@Override
		public int getOrDefault(short key, int defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public short lowerKey(short key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public short floorKey(short key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public short ceilingKey(short key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public short higherKey(short key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Short2IntMap.Entry lowerEntry(short key) { return subLower(key); }
		@Override
		public Short2IntMap.Entry floorEntry(short key) { return subFloor(key); }
		@Override
		public Short2IntMap.Entry ceilingEntry(short key) { return subCeiling(key); }
		@Override
		public Short2IntMap.Entry higherEntry(short key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Short2IntNavigableMap copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Short2IntMap.Entry firstEntry() {
			Node entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Short2IntMap.Entry lastEntry() {
			Node entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Short2IntMap.Entry pollFirstEntry() {
			Node entry = subLowest();
			if(entry != null) {
				Short2IntMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Short2IntMap.Entry pollLastEntry() {
			Node entry = subHighest();
			if(entry != null) {
				Short2IntMap.Entry result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Short2IntMap.Entry> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Short2IntMap.Entry> iter = iterator();iter.hasNext();iter.next(),size++);
				return size;
			}
			
			@Override
			public boolean isEmpty() {
				Node n = absLowest();
				return n == null || tooHigh(n.key);
			}
			
			@Override
			public boolean contains(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Short2IntMap.Entry)
				{
					Short2IntMap.Entry entry = (Short2IntMap.Entry) o;
					short key = entry.getShortKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					return node != null && entry.getIntValue() == node.getIntValue();
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				Short key = (Short)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), Integer.valueOf(node.getIntValue()));
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Short2IntMap.Entry)
				{
					Short2IntMap.Entry entry = (Short2IntMap.Entry) o;
					short key = entry.getShortKey();
					if (!inRange(key)) return false;
					Node node = map.findNode(key);
					if (node != null && node.getIntValue() == entry.getIntValue()) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				Short key = (Short)entry.getKey();
				if (!inRange(key)) return false;
				Node node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Short2IntMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Short2IntMap.Entry> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Object2BooleanFunction<Short2IntMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return false;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return true;
				}
				return false;
			}
			
			@Override
			public boolean matchesNone(Object2BooleanFunction<Short2IntMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public boolean matchesAll(Object2BooleanFunction<Short2IntMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(!filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, Short2IntMap.Entry, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Short2IntMap.Entry reduce(ObjectObjectUnaryOperator<Short2IntMap.Entry, Short2IntMap.Entry> operator) {
				Objects.requireNonNull(operator);
				Short2IntMap.Entry state = null;
				boolean empty = true;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = new BasicEntry(entry.key, entry.value);
						continue;
					}
					state = operator.apply(state, new BasicEntry(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Short2IntMap.Entry findFirst(Object2BooleanFunction<Short2IntMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return null;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return subEntry;
				}
				return null;
			}
			
			@Override
			public int count(Object2BooleanFunction<Short2IntMap.Entry> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return 0;
				int result = 0;
				BasicEntry subEntry = new BasicEntry();
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) result++;
				}
				return result;
			}
		}
		
		final class SubMapValues extends AbstractIntCollection {
			@Override
			public boolean add(int o) { throw new UnsupportedOperationException(); }
			
			@Override
			public boolean contains(int e) {
				return containsValue(e);
			}
			
			@Override
			public IntIterator iterator() { return valueIterator(); }
			
			@Override
			public int size() {
				return NavigableSubMap.this.size();
			}
			
			@Override
			public void clear() {
				NavigableSubMap.this.clear();
			}
			
			@Override
			public void forEach(IntConsumer action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectIntConsumer<E> action) {
				Objects.requireNonNull(action);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public int reduce(int identity, IntIntUnaryOperator operator) {
				Objects.requireNonNull(operator);
				int state = identity;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					state = operator.applyAsInt(state, entry.value);
				return state;
			}
			
			@Override
			public int reduce(IntIntUnaryOperator operator) {
				Objects.requireNonNull(operator);
				int state = 0;
				boolean empty = true;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = entry.value;
						continue;
					}
					state = operator.applyAsInt(state, entry.value);
				}
				return state;
			}
			
			@Override
			public int findFirst(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return entry.value;
				return 0;
			}
			
			@Override
			public int count(Int2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Short2IntMap.Entry>
		{
			public DecsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node moveNext(Node node) {
				return node.previous();
			}
			
			@Override
			protected Node movePrevious(Node node) {
				return node.next();
			}
			
			@Override
			public Short2IntMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Short2IntMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Short2IntMap.Entry>
		{
			public AcsendingSubEntryIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public Short2IntMap.Entry previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Short2IntMap.Entry next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements ShortBidirectionalIterator
		{
			public DecsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node moveNext(Node node) {
				return node.previous();
			}
			
			@Override
			protected Node movePrevious(Node node) {
				return node.next();
			}
			
			@Override
			public short previousShort() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public short nextShort() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements ShortBidirectionalIterator
		{
			public AcsendingSubKeyIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public short previousShort() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public short nextShort() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public AcsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, true);
			}

			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		class DecsendingSubValueIterator extends SubMapEntryIterator implements IntBidirectionalIterator
		{
			public DecsendingSubValueIterator(Node first, Node forwardFence, Node backwardFence) {
				super(first, forwardFence, backwardFence, false);
			}
			
			@Override
			protected Node moveNext(Node node) {
				return node.previous();
			}
			
			@Override
			protected Node movePrevious(Node node) {
				return node.next();
			}
			
			@Override
			public int previousInt() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public int nextInt() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		abstract class SubMapEntryIterator
		{
			final boolean isForward;
			boolean wasForward;
			Node lastReturned;
			Node next;
			Node previous;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			short forwardFence;
			short backwardFence;
			
			public SubMapEntryIterator(Node first, Node forwardFence, Node backwardFence, boolean isForward) {
				next = first;
				previous = first == null ? null : movePrevious(first);
				this.forwardFence = forwardFence == null ? (short)0 : forwardFence.key;
				this.backwardFence = backwardFence == null ? (short)0 : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
				this.isForward = isForward;
			}
			
			protected Node moveNext(Node node) {
				return node.next();
			}
			
			protected Node movePrevious(Node node) {
				return node.previous();
			}
			
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			protected Node nextEntry() {
				lastReturned = next;
				previous = next;
				Node result = next;
				next = moveNext(next);
				wasForward = isForward;
				return result;
			}
			
			public boolean hasPrevious() {
                return previous != null && (unboundBackwardFence || previous.key != backwardFence);
			}
			
			protected Node previousEntry() {
				lastReturned = previous;
				next = previous;
				Node result = previous;
				previous = movePrevious(previous);
				wasForward = !isForward;
				return result;
			}
			
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(next == lastReturned) next = moveNext(next);
				if(previous == lastReturned) previous = movePrevious(previous);
				if(wasForward && lastReturned.needsSuccessor()) next = lastReturned;
				map.removeNode(lastReturned);
				lastReturned = null;
			}
		}
	}
	
	class Values extends AbstractIntCollection
	{
		@Override
		public IntIterator iterator() {
			return new AscendingValueIterator(first);
		}
		
		@Override
		public boolean add(int e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void clear() {
			Short2IntRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Short2IntRBTreeMap.this.size;
		}
		
		@Override
		public boolean contains(int e) {
			return containsValue(e);
		}
		
		@Override
		public boolean remove(Object o) {
			for(Node entry = first; entry != null; entry = entry.next()) {
				if(Objects.equals(entry.getValue(), o)) {
					removeNode(entry);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public void forEach(IntConsumer action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectIntConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(!filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public int reduce(int identity, IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = identity;
			for(Node entry = first;entry != null;entry = entry.next())
				state = operator.applyAsInt(state, entry.value);
			return state;
		}
		
		@Override
		public int reduce(IntIntUnaryOperator operator) {
			Objects.requireNonNull(operator);
			int state = 0;
			boolean empty = true;
			for(Node entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = entry.value;
					continue;
				}
				state = operator.applyAsInt(state, entry.value);
			}
			return state;
		}
		
		@Override
		public int findFirst(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return entry.value;
			return 0;
		}
		
		@Override
		public int count(Int2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Short2IntMap.Entry> {
		
		@Override
		public ObjectIterator<Short2IntMap.Entry> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Short2IntRBTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Short2IntRBTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Short2IntMap.Entry)
			{
				Short2IntMap.Entry entry = (Short2IntMap.Entry) o;
				short key = entry.getShortKey();
				Node node = findNode(key);
				return node != null && entry.getIntValue() == node.getIntValue();
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			if(!(entry.getKey() instanceof Short)) return false;
			Short key = (Short)entry.getKey();
			Node node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), Integer.valueOf(node.getIntValue()));
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Short2IntMap.Entry)
			{
				Short2IntMap.Entry entry = (Short2IntMap.Entry) o;
				short key = entry.getShortKey();
				Node node = findNode(key);
				if (node != null && entry.getIntValue() == node.getIntValue()) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			Short key = (Short)entry.getKey();
			Node node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), Integer.valueOf(node.getIntValue()))) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Short2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Short2IntMap.Entry> action) {
			Objects.requireNonNull(action);
			for(Node entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Short2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Short2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Short2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(!filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Short2IntMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Short2IntMap.Entry reduce(ObjectObjectUnaryOperator<Short2IntMap.Entry, Short2IntMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Short2IntMap.Entry state = null;
			boolean empty = true;
			for(Node entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = new BasicEntry(entry.key, entry.value);
					continue;
				}
				state = operator.apply(state, new BasicEntry(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Short2IntMap.Entry findFirst(Object2BooleanFunction<Short2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return subEntry;
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Short2IntMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry subEntry = new BasicEntry();
			for(Node entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) result++;
			}
			return result;
		}
	}
	
	class DescendingKeyIterator extends MapEntryIterator implements ShortBidirectionalIterator
	{
		public DescendingKeyIterator(Node first) {
			super(first, false);
		}
		
		@Override
		protected Node moveNext(Node node) {
			return node.previous();
		}
		
		@Override
		protected Node movePrevious(Node node) {
			return node.next();
		}
		
		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Short2IntMap.Entry>
	{
		public AscendingMapEntryIterator(Node first)
		{
			super(first, true);
		}

		@Override
		public Short2IntMap.Entry previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Short2IntMap.Entry next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements IntBidirectionalIterator
	{
		public AscendingValueIterator(Node first) {
			super(first, true);
		}

		@Override
		public int previousInt() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().value;
		}

		@Override
		public int nextInt() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().value;
		}
	}
	
	class AscendingKeyIterator extends MapEntryIterator implements ShortBidirectionalIterator
	{
		public AscendingKeyIterator(Node first) {
			super(first, true);
		}

		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	abstract class MapEntryIterator
	{
		final boolean isForward;
		boolean wasMoved = false;
		Node lastReturned;
		Node next;
		Node previous;
		
		public MapEntryIterator(Node first, boolean isForward) {
			next = first;
			previous = first == null ? null : movePrevious(first);
			this.isForward = isForward;
		}
		
		protected Node moveNext(Node node) {
			return node.next();
		}
		
		protected Node movePrevious(Node node) {
			return node.previous();
		}
		
		public boolean hasNext() {
            return next != null;
		}
		
		protected Node nextEntry() {
			lastReturned = next;
			previous = next;
			Node result = next;
			next = moveNext(next);
			wasMoved = isForward;
			return result;
		}
		
		public boolean hasPrevious() {
            return previous != null;
		}
		
		protected Node previousEntry() {
			lastReturned = previous;
			next = previous;
			Node result = previous;
			previous = movePrevious(previous);
			wasMoved = !isForward;
			return result;
		}
		
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(next == lastReturned) next = moveNext(next);
			if(previous == lastReturned) previous = movePrevious(previous);
			if(wasMoved && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	private static final class Node implements Short2IntMap.Entry
	{
		static final int BLACK = 1;
		
		short key;
		int value;
		int state;
		Node parent;
		Node left;
		Node right;
		
		Node(short key, int value, Node parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
		
		Node copy() {
			Node entry = new Node(key, value, null);
			entry.state = state;
			if(left != null) {
				Node newLeft = left.copy();
				entry.left = newLeft;
				newLeft.parent = entry;
			}
			if(right != null) {
				Node newRight = right.copy();
				entry.right = newRight;
				newRight.parent = entry;
			}
			return entry;
		}
		
		public BasicEntry export() {
			return new BasicEntry(key, value);
		}
		
		@Override
		public short getShortKey() {
			return key;
		}
		
		@Override
		public int getIntValue() {
			return value;
		}
		
		@Override
		public int setValue(int value) {
			int oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		int addTo(int value) {
			int oldValue = this.value;
			this.value += value;
			return oldValue;
		}

		int subFrom(int value) {
			int oldValue = this.value;
			this.value -= value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Short2IntMap.Entry) {
					Short2IntMap.Entry entry = (Short2IntMap.Entry)obj;
					return key == entry.getShortKey() && value == entry.getIntValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherKey instanceof Short && otherValue instanceof Integer && key == ((Short)otherKey).shortValue() && value == ((Integer)otherValue).intValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Short.hashCode(key) ^ Integer.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Short.toString(key) + "=" + Integer.toString(value);
		}
		
		boolean isBlack() {
			return (state & BLACK) != 0;
		}
		
		void setBlack(boolean value) {
			if(value) state |= BLACK;
			else state &= ~BLACK;
		}
		
		boolean needsSuccessor() { return left != null && right != null; }
		
		boolean replace(Node entry) {
			if(entry != null) entry.parent = parent;
			if(parent != null) {
				if(parent.left == this) parent.left = entry;
				else parent.right = entry;
			}
			return parent == null;
		}
		
		Node next() {
			if(right != null) {
				Node parent = right;
				while(parent.left != null) parent = parent.left;
				return parent;
			}
			Node parent = this.parent;
			Node control = this;
			while(parent != null && control == parent.right) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		Node previous() {
			if(left != null) {
				Node parent = left;
				while(parent.right != null) parent = parent.right;
				return parent;
			}
			Node parent = this.parent;
			Node control = this;
			while(parent != null && control == parent.left) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
}