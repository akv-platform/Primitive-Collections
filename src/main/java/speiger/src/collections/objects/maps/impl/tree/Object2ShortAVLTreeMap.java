package speiger.src.collections.objects.maps.impl.tree;

import java.util.Collections;
import java.util.Map;
import java.util.Comparator;
import java.util.Objects;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.objects.functions.function.Object2ShortFunction;
import speiger.src.collections.objects.functions.function.ObjectShortUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortMap;
import speiger.src.collections.objects.maps.interfaces.Object2ShortNavigableMap;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.utils.maps.Object2ShortMaps;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.collections.ShortBidirectionalIterator;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectIterator;

/**
 * A Simple Type Specific AVL TreeMap implementation that reduces boxing/unboxing.
 * It is using a bit more memory then <a href="https://github.com/vigna/fastutil">FastUtil</a>,
 * but it saves a lot of Performance on the Optimized removal and iteration logic.
 * Which makes the implementation actually useable and does not get outperformed by Javas default implementation.
 * @param <T> the type of elements maintained by this Collection
 */
public class Object2ShortAVLTreeMap<T> extends AbstractObject2ShortMap<T> implements Object2ShortNavigableMap<T>
{
	/** The center of the Tree */
	protected transient Node<T> tree;
	/** The Lowest possible Node */
	protected transient Node<T> first;
	/** The Highest possible Node */
	protected transient Node<T> last;
	/** The amount of elements stored in the Map */
	protected int size = 0;
	/** The Sorter of the Tree */
	protected transient Comparator<T> comparator;
	
	
	/** KeySet Cache */
	protected ObjectNavigableSet<T> keySet;
	/** Values Cache */
	protected ShortCollection values;
	/** EntrySet Cache */
	protected ObjectSet<Object2ShortMap.Entry<T>> entrySet;
	
	/**
	 * Default Constructor
	 */
	public Object2ShortAVLTreeMap() {
	}
	
	/**
	 * Constructor that allows to define the sorter
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Object2ShortAVLTreeMap(Comparator<T> comp) {
		comparator = comp;
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ShortAVLTreeMap(T[] keys, Short[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ShortAVLTreeMap(T[] keys, Short[] values, Comparator<T> comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].shortValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ShortAVLTreeMap(T[] keys, short[] values) {
		this(keys, values, null);
	}
	
	/**
	 * Helper constructor that has a custom sorter and allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param comp the function that decides how the tree is sorted, can be null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2ShortAVLTreeMap(T[] keys, short[] values, Comparator<T> comp) {
		comparator = comp;
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Object2ShortAVLTreeMap(Map<? extends T, ? extends Short> map) {
		this(map, null);
	}
	
	/**
	 * A Helper constructor that has a custom sorter and allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
	 */
	public Object2ShortAVLTreeMap(Map<? extends T, ? extends Short> map, Comparator<T> comp) {
		comparator = comp;
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Object2ShortAVLTreeMap(Object2ShortMap<T> map) {
		this(map, null);
	}
	
	/**
	 * A Type Specific Helper function that has a custom sorter and allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param comp the function that decides how the tree is sorted, can be null
 	 */
	public Object2ShortAVLTreeMap(Object2ShortMap<T> map, Comparator<T> comp) {
		comparator = comp;
		putAll(map);
	}

	/** only used for primitives 
	 * @return null 
	 */
	public T getDefaultMaxValue() { return null; }
	/** only used for primitives 
	 * @return null 
	 */
	public T getDefaultMinValue() { return null; }
	
	
	@Override
	public short put(T key, short value) {
		validate(key);
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<T> parent = tree;
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
		Node<T> adding = new Node<>(key, value, parent);
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
	public short putIfAbsent(T key, short value) {
		validate(key);
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<T> parent = tree;
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
		Node<T> adding = new Node<>(key, value, parent);
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
	public short addTo(T key, short value) {
		if(tree == null) {
			tree = first = last = new Node<>(key, value, null);
			size++;
			return getDefaultReturnValue();
		}
		int compare = 0;
		Node<T> parent = tree;
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
		Node<T> adding = new Node<>(key, value, parent);
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
	public short subFrom(T key, short value) {
		if(tree == null) return getDefaultReturnValue();
		int compare = 0;
		Node<T> parent = tree;
		while(true) {
			if((compare = compare(key, parent.key)) == 0)
			{
				short oldValue = parent.subFrom(value);
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
	public Comparator<T> comparator() { return comparator; }

	@Override
	public boolean containsKey(Object key) {
		return findNode((T)key) != null;
	}
	
	@Override
	public short getShort(T key) {
		Node<T> node = findNode(key);
		return node == null ? getDefaultReturnValue() : node.value;
	}
	
	@Override
	public short getOrDefault(T key, short defaultValue) {
		Node<T> node = findNode(key);
		return node == null ? defaultValue : node.value;
	}
	
	@Override
	public T firstKey() {
		if(tree == null) throw new NoSuchElementException();
		return first.key;
	}
	
	@Override
	public T pollFirstKey() {
		if(tree == null) return null;
		T result = first.key;
		removeNode(first);
		return result;
	}
	
	@Override
	public T lastKey() {
		if(tree == null) throw new NoSuchElementException();
		return last.key;
	}
	
	@Override
	public T pollLastKey() {
		if(tree == null) return null;
		T result = last.key;
		removeNode(last);
		return result;
	}
	
	@Override
	public Object2ShortMap.Entry<T> firstEntry() {
		if(tree == null) return null;
		return first.export();
	}
	
	@Override
	public Object2ShortMap.Entry<T> lastEntry() {
		if(tree == null) return null;
		return last.export();
	}
	
	@Override
	public Object2ShortMap.Entry<T> pollFirstEntry() {
		if(tree == null) return null;
		BasicEntry<T> entry = first.export();
		removeNode(first);
		return entry;
	}
	
	@Override
	public Object2ShortMap.Entry<T> pollLastEntry() {
		if(tree == null) return null;
		BasicEntry<T> entry = last.export();
		removeNode(last);
		return entry;
	}
	
	@Override
	public short firstShortValue() {
		if(tree == null) throw new NoSuchElementException();
		return first.value;
	}
	
	@Override
	public short lastShortValue() {
		if(tree == null) throw new NoSuchElementException();
		return last.value;
	}
	
	@Override
	public short rem(T key) {
		Node<T> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		short value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public short remOrDefault(T key, short defaultValue) {
		Node<T> entry = findNode(key);
		if(entry == null) return defaultValue;
		short value = entry.value;
		removeNode(entry);
		return value;
	}
	
	@Override
	public boolean remove(T key, short value) {
		Node<T> entry = findNode(key);
		if(entry == null || entry.value != value) return false;
		removeNode(entry);
		return true;
	}
	
	@Override
	public boolean replace(T key, short oldValue, short newValue) {
		Node<T> entry = findNode(key);
		if(entry == null || entry.value != oldValue) return false;
		entry.value = newValue;
		return true;
	}
	
	@Override
	public short replace(T key, short value) {
		Node<T> entry = findNode(key);
		if(entry == null) return getDefaultReturnValue();
		short oldValue = entry.value;
		entry.value = value;
		return oldValue;
	}
	
	@Override
	public short computeShort(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(T key, Object2ShortFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			short newValue = mappingFunction.getShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(Objects.equals(entry.value, getDefaultReturnValue())) {
			short newValue = mappingFunction.getShort(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public short supplyShortIfAbsent(T key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null) {
			short newValue = valueProvider.getShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			put(key, newValue);
			return newValue;
		}
		if(entry.value == getDefaultReturnValue()) {
			short newValue = valueProvider.getShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			entry.value = newValue;
		}
		return entry.value;
	}
	
	@Override
	public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, entry.value);
		if(newValue == getDefaultReturnValue()) {
			removeNode(entry);
			return newValue;
		}
		entry.value = newValue;
		return newValue;
	}
	
	@Override
	public short mergeShort(T key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		validate(key);
		Node<T> entry = findNode(key);
		short newValue = entry == null || entry.value == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(entry.value, value);
		if(newValue == getDefaultReturnValue()) {
			if(entry != null)
				removeNode(entry);
		}
		else if(entry == null) put(key, newValue);
		else entry.value = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Object2ShortMap<T> m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2ShortMap.Entry<T> entry : Object2ShortMaps.fastIterable(m)) {
			T key = entry.getKey();
			Node<T> subEntry = findNode(key);
			short newValue = subEntry == null || subEntry.value == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(subEntry.value, entry.getShortValue());
			if(newValue == getDefaultReturnValue()) {
				if(subEntry != null)
					removeNode(subEntry);
			}
			else if(subEntry == null) put(key, newValue);
			else subEntry.value = newValue;
		}
	}
	
	@Override
	public void forEach(ObjectShortConsumer<T> action) {
		for(Node<T> entry = first;entry != null;entry = entry.next())
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
	
	protected ObjectBidirectionalIterator<T> keyIterator() {
		return new AscendingKeyIterator(first);
	}
	
	protected ObjectBidirectionalIterator<T> keyIterator(T element) {
		return new AscendingKeyIterator(findNode(element));
	}
	
	protected ObjectBidirectionalIterator<T> descendingKeyIterator() {
		return new DescendingKeyIterator(last);
	}
		
	@Override
	public Object2ShortAVLTreeMap<T> copy() {
		Object2ShortAVLTreeMap<T> set = new Object2ShortAVLTreeMap<>();
		set.size = size;
		if(tree != null) {
			set.tree = tree.copy();
			Node<T> lastFound = null;
			for(Node<T> entry = tree;entry != null;entry = entry.left) lastFound = entry;
			set.first = lastFound;
			lastFound = null;
			for(Node<T> entry = tree;entry != null;entry = entry.right) lastFound = entry;
			set.last = lastFound;
		}
		return set;
	}
	
	@Override
	public ObjectSortedSet<T> keySet() {
		return navigableKeySet();
	}
	
	@Override
	public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() {
		if(entrySet == null) entrySet = new EntrySet();
		return entrySet;
	}
	
	@Override
	public ShortCollection values() {
		if(values == null) values = new Values();
		return values;
	}
	
	@Override
	public ObjectNavigableSet<T> navigableKeySet() {
		if(keySet == null) keySet = new KeySet<>(this);
		return keySet;
	}
	
	@Override
	public Object2ShortNavigableMap<T> descendingMap() {
		return new DescendingNaivgableSubMap<>(this, true, null, true, true, null, true);
	}
	
	@Override
	public ObjectNavigableSet<T> descendingKeySet() {
		return descendingMap().navigableKeySet();
	}
	
	@Override
	public Object2ShortNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, fromInclusive, false, toKey, toInclusive);
	}
	
	@Override
	public Object2ShortNavigableMap<T> headMap(T toKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, true, null, true, false, toKey, inclusive);
	}
	
	@Override
	public Object2ShortNavigableMap<T> tailMap(T fromKey, boolean inclusive) {
		return new AscendingNaivgableSubMap<>(this, false, fromKey, inclusive, true, null, true);
	}
	
	@Override
	public T lowerKey(T e) {
		Node<T> node = findLowerNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}

	@Override
	public T floorKey(T e) {
		Node<T> node = findFloorNode(e);
		return node != null ? node.key : getDefaultMinValue();
	}
	
	@Override
	public T higherKey(T e) {
		Node<T> node = findHigherNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}

	@Override
	public T ceilingKey(T e) {
		Node<T> node = findCeilingNode(e);
		return node != null ? node.key : getDefaultMaxValue();
	}
	
	@Override
	public Object2ShortMap.Entry<T> lowerEntry(T key) {
		Node<T> node = findLowerNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Object2ShortMap.Entry<T> higherEntry(T key) {
		Node<T> node = findHigherNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Object2ShortMap.Entry<T> floorEntry(T key) {
		Node<T> node = findFloorNode(key);
		return node != null ? node.export() : null;
	}
	
	@Override
	public Object2ShortMap.Entry<T> ceilingEntry(T key) {
		Node<T> node = findCeilingNode(key);
		return node != null ? node.export() : null;
	}
	
	protected Node<T> findLowerNode(T key) {
		Node<T> entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) > 0) {
				if(entry.right != null) entry = entry.right;
				else return entry;
			}
			else {
				if(entry.left != null) entry = entry.left;
				else {
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findFloorNode(T key) {
		Node<T> entry = tree;
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
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findCeilingNode(T key) {
		Node<T> entry = tree;
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
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findHigherNode(T key) {
		Node<T> entry = tree;
		while(entry != null) {
			if(compare(key, entry.key) < 0) {
				if(entry.left != null) entry = entry.left;
				else return entry;
			}
			else {
				if(entry.right != null) entry = entry.right;
				else {
					Node<T> parent = entry.parent;
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
	
	protected Node<T> findNode(T key) {
		Node<T> node = tree;
		int compare;
		while(node != null) {
			if((compare = compare(key, node.key)) == 0) return node;
			if(compare < 0) node = node.left;
			else node = node.right;
		}
		return null;
	}
	
	protected void removeNode(Node<T> entry) {
		size--;
		if(entry.needsSuccessor()) {
			Node<T> successor = entry.next();
			entry.key = successor.key;
			entry.value = successor.value;
			entry = successor;
		}
		if(entry.previous() == null) first = entry.next();
		if(entry.next() == null) last = entry.previous();
		Node<T> replacement = entry.left != null ? entry.left : entry.right;
		if(replacement != null) {
			if(entry.replace(replacement)) tree = replacement;
			entry.left = entry.right = entry.parent = null;
			fixAfterDeletion(replacement);
		}
		else if(entry.parent == null) tree = first = last = null;
		else {
			fixAfterDeletion(entry);
			entry.replace(null);
			entry.parent = null;
		}
	}
	
	protected void validate(T k) { compare(k, k); }
	protected int compare(T k, T v) { return comparator != null ? comparator.compare(k, v) : ((Comparable<T>)k).compareTo((T)v);}
	
	/** From CLR */
	protected void rotateLeft(Node<T> entry) {
		if(entry != null) {
			Node<T> right = entry.right;
			entry.right = right.left;
			if(right.left != null) right.left.parent = entry;
			right.parent = entry.parent;
			if(entry.parent == null) tree = right;
			else if(entry.parent.left == entry) entry.parent.left = right;
			else entry.parent.right = right;
			right.left = entry;
			entry.parent = right;
			entry.updateHeight();
			right.updateHeight();
		}
	}
	
	/** From CLR */
	protected void rotateRight(Node<T> entry) {
		if(entry != null) {
			Node<T> left = entry.left;
			entry.left = left.right;
			if(left.right != null) left.right.parent = entry;
			left.parent = entry.parent;
			if(entry.parent == null) tree = left;
			else if(entry.parent.right == entry) entry.parent.right = left;
			else entry.parent.left = left;
			left.right = entry;
			entry.parent = left;
			entry.updateHeight();
			left.updateHeight();
		}
	}
	
	/** From CLR */
	protected void fixAfterInsertion(Node<T> entry) {
		while(entry != null) {
			entry.updateHeight();
			int balance = entry.getBalance();
			if(balance > 1) {
				int compare = entry.left.getBalance();
				if(compare > 0) rotateRight(entry);
				else if(compare < 0) {
					rotateLeft(entry.left);
					rotateRight(entry);
				}
			}
			else if(balance < -1) {
				int compare = entry.right.getBalance();
				if(compare < 0) rotateLeft(entry);
				else if(compare > 0) {
					rotateRight(entry.right);
					rotateLeft(entry);
				}
			}
			entry = entry.parent;
		}
	}
	
	/** From CLR */
	protected void fixAfterDeletion(Node<T> entry) {
		if(entry != null) {
			entry.updateHeight();
			int balance = entry.getBalance();
			if(balance > 1) {
				int subBalance = entry.left.getBalance();
				if(subBalance >= 0) rotateRight(entry);
				else {
					rotateLeft(entry.left);
					rotateRight(entry);
				}
			}
			else if(balance < -1)
			{
				int subBalance = entry.right.getBalance();
				if(subBalance <= 0) rotateLeft(entry);
				else {
					rotateRight(entry.right);
					rotateLeft(entry);
				}
			}
			entry = entry.parent;
		}
	}
	
	static class KeySet<T> extends AbstractObjectSet<T> implements ObjectNavigableSet<T>
	{
		Object2ShortNavigableMap<T> map;

		public KeySet(Object2ShortNavigableMap<T> map) {
			this.map = map;
		}
		
		@Override
		public T lower(T e) { return map.lowerKey(e); }
		@Override
		public T floor(T e) { return map.floorKey(e); }
		@Override
		public T ceiling(T e) { return map.ceilingKey(e); }
		@Override
		public T higher(T e) { return map.higherKey(e); }
		@Override
		public T pollFirst() { return map.pollFirstKey(); }
		@Override
		public T pollLast() { return map.pollLastKey(); }
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public T first() { return map.firstKey(); } 
		@Override
		public T last() { return map.lastKey(); }
		@Override
		public void clear() { map.clear(); }
		
		@Override
		public boolean remove(Object o) { 
			int oldSize = map.size();
			map.remove(o); 
			return oldSize != map.size();
		}
		
		@Override
		public boolean add(T e) { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<T> iterator(T fromElement) {
			if(map instanceof Object2ShortAVLTreeMap) return ((Object2ShortAVLTreeMap<T>)map).keyIterator(fromElement);
			return ((NavigableSubMap<T>)map).keyIterator(fromElement);
		}
		
		@Override
		public ObjectNavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) { return new KeySet<>(map.subMap(fromElement, fromInclusive, toElement, toInclusive)); }
		@Override
		public ObjectNavigableSet<T> headSet(T toElement, boolean inclusive) { return new KeySet<>(map.headMap(toElement, inclusive)); }
		@Override
		public ObjectNavigableSet<T> tailSet(T fromElement, boolean inclusive) { return new KeySet<>(map.tailMap(fromElement, inclusive)); }
		
		@Override
		public ObjectBidirectionalIterator<T> iterator() {
			if(map instanceof Object2ShortAVLTreeMap) return ((Object2ShortAVLTreeMap<T>)map).keyIterator();
			return ((NavigableSubMap<T>)map).keyIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<T> descendingIterator() {
			if(map instanceof Object2ShortAVLTreeMap) return ((Object2ShortAVLTreeMap<T>)map).descendingKeyIterator();
			return ((NavigableSubMap<T>)map).descendingKeyIterator();
		}
		
		protected Node<T> start() {
			if(map instanceof Object2ShortAVLTreeMap) return ((Object2ShortAVLTreeMap<T>)map).first;
			return ((NavigableSubMap<T>)map).subLowest();
		}
		
		protected Node<T> end() {
			if(map instanceof Object2ShortAVLTreeMap) return null;
			return ((NavigableSubMap<T>)map).subHighest();
		}
		
		protected Node<T> next(Node<T> entry) {
			if(map instanceof Object2ShortAVLTreeMap) return entry.next();
			return ((NavigableSubMap<T>)map).next(entry);
		}
		
		protected Node<T> previous(Node<T> entry) {
			if(map instanceof Object2ShortAVLTreeMap) return entry.previous();
			return ((NavigableSubMap<T>)map).previous(entry);
		}
		
		@Override
		public ObjectNavigableSet<T> descendingSet() { return new KeySet<>(map.descendingMap()); }
		@Override
		public KeySet<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public boolean isEmpty() { return map.isEmpty(); }
		@Override
		public int size() { return map.size(); }
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(entry.key);
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				action.accept(input, entry.key);
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.getBoolean(entry.key)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.getBoolean(entry.key)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(!filter.getBoolean(entry.key)) return false;
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				state = operator.apply(state, entry.key);
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry)) {
				if(empty) {
					empty = false;
					state = entry.key;
					continue;
				}
				state = operator.apply(state, entry.key);
			}
			return state;
		}
		
		@Override
		public T findFirst(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.getBoolean(entry.key)) return entry.key;
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<T> entry = start(), end = end();entry != null && (end == null || (end != previous(entry)));entry = next(entry))
				if(filter.getBoolean(entry.key)) result++;
			return result;
		}
	}
	
	static class AscendingNaivgableSubMap<T> extends NavigableSubMap<T>
	{
		AscendingNaivgableSubMap(Object2ShortAVLTreeMap<T> map, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Object2ShortNavigableMap<T> descendingMap() {
			if(inverse == null) inverse = new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}
		
		@Override
		public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() {
			if(entrySet == null) entrySet = new AscendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		public ObjectNavigableSet<T> navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Object2ShortNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, fromInclusive, false, toKey, toInclusive);
		}
		
		@Override
		public Object2ShortNavigableMap<T> headMap(T toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, toKey, inclusive);
		}
		
		@Override
		public Object2ShortNavigableMap<T> tailMap(T fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new AscendingNaivgableSubMap<>(map, false, fromKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		protected Node<T> subLowest() { return absLowest(); }
		@Override
		protected Node<T> subHighest() { return absHighest(); }
		@Override
		protected Node<T> subCeiling(T key) { return absCeiling(key); }
		@Override
		protected Node<T> subHigher(T key) { return absHigher(key); }
		@Override
		protected Node<T> subFloor(T key) { return absFloor(key); }
		@Override
		protected Node<T> subLower(T key) { return absLower(key); }
		
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator(T element) {
			return new AcsendingSubKeyIterator(absLower(element), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ShortBidirectionalIterator valueIterator() {
			return new AcsendingSubValueIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<T> descendingKeyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		class AscendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Object2ShortMap.Entry<T>> iterator() {
				return new AcsendingSubEntryIterator(absLowest(), absHighFence(), absLowFence());
			}
		}
	}
	
	static class DescendingNaivgableSubMap<T> extends NavigableSubMap<T>
	{
		Comparator<T> comparator;
		DescendingNaivgableSubMap(Object2ShortAVLTreeMap<T> map, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive) {
			super(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			comparator = Collections.reverseOrder(map.comparator());
		}
		
		@Override
		public Comparator<T> comparator() { return comparator; }
		
		@Override
		public Object2ShortNavigableMap<T> descendingMap() {
			if(inverse == null) inverse = new AscendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
			return inverse;
		}

		@Override
		public ObjectNavigableSet<T> navigableKeySet() {
			if(keySet == null) keySet = new KeySet<>(this);
			return keySet;
		}
		
		@Override
		public Object2ShortNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) {
			if (!inRange(fromKey, fromInclusive)) throw new IllegalArgumentException("fromKey out of range");
			if (!inRange(toKey, toInclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, toInclusive, false, fromKey, fromInclusive);
		}
		
		@Override
		public Object2ShortNavigableMap<T> headMap(T toKey, boolean inclusive) {
			if (!inRange(toKey, inclusive)) throw new IllegalArgumentException("toKey out of range");
			return new DescendingNaivgableSubMap<>(map, false, toKey, inclusive, toEnd, hi, hiInclusive);
		}
		
		@Override
		public Object2ShortNavigableMap<T> tailMap(T fromKey, boolean inclusive) {
			if (!inRange(fromKey, inclusive)) throw new IllegalArgumentException("fromKey out of range");
			return new DescendingNaivgableSubMap<>(map, fromStart, lo, loInclusive, false, fromKey, inclusive);
		}
		
		@Override
		public ObjectSet<Object2ShortMap.Entry<T>> object2ShortEntrySet() {
			if(entrySet == null) entrySet = new DescendingSubEntrySet();
			return entrySet;
		}
		
		@Override
		protected Node<T> subLowest() { return absHighest(); }
		@Override
		protected Node<T> subHighest() { return absLowest(); }
		@Override
		protected Node<T> subCeiling(T key) { return absFloor(key); }
		@Override
		protected Node<T> subHigher(T key) { return absLower(key); }
		@Override
		protected Node<T> subFloor(T key) { return absCeiling(key); }
		@Override
		protected Node<T> subLower(T key) { return absHigher(key); }
		@Override
		protected Node<T> next(Node<T> entry) { return entry.previous(); }
		@Override
		protected Node<T> previous(Node<T> entry) { return entry.next(); }
		
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator() {
			return new DecsendingSubKeyIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<T> keyIterator(T element) {
			return new DecsendingSubKeyIterator(absHigher(element), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ShortBidirectionalIterator valueIterator() {
			return new DecsendingSubValueIterator(absHighest(), absLowFence(), absHighFence()); 
		}
		
		@Override
		protected ObjectBidirectionalIterator<T> descendingKeyIterator() {
			return new AcsendingSubKeyIterator(absLowest(), absHighFence(), absLowFence()); 
		}
		
		class DescendingSubEntrySet extends SubEntrySet {
			@Override
			public ObjectIterator<Object2ShortMap.Entry<T>> iterator() {
				return new DecsendingSubEntryIterator(absHighest(), absLowFence(), absHighFence());
			}
		}
	}
	
	static abstract class NavigableSubMap<T> extends AbstractObject2ShortMap<T> implements Object2ShortNavigableMap<T>
	{
		final Object2ShortAVLTreeMap<T> map;
		final T lo, hi;
		final boolean fromStart, toEnd;
		final boolean loInclusive, hiInclusive;
		
		Object2ShortNavigableMap<T> inverse;
		ObjectNavigableSet<T> keySet;
		ObjectSet<Object2ShortMap.Entry<T>> entrySet;
		ShortCollection values;
		
		NavigableSubMap(Object2ShortAVLTreeMap<T> map, boolean fromStart, T lo, boolean loInclusive, boolean toEnd, T hi, boolean hiInclusive) {
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
		
		public T getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		public T getDefaultMinValue() { return map.getDefaultMinValue(); }
		protected boolean isNullComparator() { return map.comparator() == null; }
		
		@Override
		public AbstractObject2ShortMap<T> setDefaultReturnValue(short v) { 
			map.setDefaultReturnValue(v);
			return this;
		}
		
		@Override
		public short getDefaultReturnValue() { return map.getDefaultReturnValue(); }
		
		@Override
		public ShortCollection values() {
			if(values == null) values = new SubMapValues();
			return values;
		}
		
		@Override
		public ObjectNavigableSet<T> descendingKeySet() {
			return descendingMap().navigableKeySet();
		}
		
		@Override
		public ObjectSet<T> keySet() {
			return navigableKeySet();
		}
		
		protected abstract Node<T> subLowest();
		protected abstract Node<T> subHighest();
		protected abstract Node<T> subCeiling(T key);
		protected abstract Node<T> subHigher(T key);
		protected abstract Node<T> subFloor(T key);
		protected abstract Node<T> subLower(T key);
		protected abstract ObjectBidirectionalIterator<T> keyIterator();
		protected abstract ObjectBidirectionalIterator<T> keyIterator(T element);
		protected abstract ShortBidirectionalIterator valueIterator();
		protected abstract ObjectBidirectionalIterator<T> descendingKeyIterator();
		protected T lowKeyOrNull(Node<T> entry) { return entry == null ? null : entry.key; }
		protected T highKeyOrNull(Node<T> entry) { return entry == null ? null : entry.key; }
		protected Node<T> next(Node<T> entry) { return entry.next(); }
		protected Node<T> previous(Node<T> entry) { return entry.previous(); }
		
		protected boolean tooLow(T key) {
			if (!fromStart) {
				int c = map.compare(key, lo);
				if (c < 0 || (c == 0 && !loInclusive)) return true;
			}
			return false;
		}
		
		protected boolean tooHigh(T key) {
			if (!toEnd) {
				int c = map.compare(key, hi);
				if (c > 0 || (c == 0 && !hiInclusive)) return true;
			}
			return false;
		}
		protected boolean inRange(T key) { return !tooLow(key) && !tooHigh(key); }
		protected boolean inClosedRange(T key) { return (fromStart || map.compare(key, lo) >= 0) && (toEnd || map.compare(hi, key) >= 0); }
		protected boolean inRange(T key, boolean inclusive) { return inclusive ? inRange(key) : inClosedRange(key); }
		
		protected Node<T> absLowest() {
			Node<T> e = (fromStart ?  map.first : (loInclusive ? map.findCeilingNode(lo) : map.findHigherNode(lo)));
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<T> absHighest() {
			Node<T> e = (toEnd ?  map.last : (hiInclusive ?  map.findFloorNode(hi) : map.findLowerNode(hi)));
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<T> absCeiling(T key) {
			if (tooLow(key)) return absLowest();
			Node<T> e = map.findCeilingNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<T> absHigher(T key) {
			if (tooLow(key)) return absLowest();
			Node<T> e = map.findHigherNode(key);
			return (e == null || tooHigh(e.key)) ? null : e;
		}
		
		protected Node<T> absFloor(T key) {
			if (tooHigh(key)) return absHighest();
			Node<T> e = map.findFloorNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<T> absLower(T key) {
			if (tooHigh(key)) return absHighest();
			Node<T> e = map.findLowerNode(key);
			return (e == null || tooLow(e.key)) ? null : e;
		}
		
		protected Node<T> absHighFence() { return (toEnd ? null : (hiInclusive ? map.findHigherNode(hi) : map.findCeilingNode(hi))); }
		protected Node<T> absLowFence() { return (fromStart ? null : (loInclusive ?  map.findLowerNode(lo) : map.findFloorNode(lo))); }
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		
		@Override
		public T pollFirstKey() {
			Node<T> entry = subLowest();
			if(entry != null) {
				T result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		@Override
		public T pollLastKey() {
			Node<T> entry = subHighest();
			if(entry != null) {
				T result = entry.key;
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		@Override
		public short firstShortValue() {
			Node<T> entry = subLowest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public short lastShortValue() {
			Node<T> entry = subHighest();
			return entry == null ? map.getDefaultReturnValue() : entry.value;
		}
		
		@Override
		public T firstKey() {
			Node<T> entry = subLowest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public T lastKey() {
			Node<T> entry = subHighest();
			if(entry == null) throw new NoSuchElementException();
			return entry.key;
		}
		
		@Override
		public short put(T key, short value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.put(key, value);
		}
		
		@Override
		public short putIfAbsent(T key, short value) {
			if (!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.putIfAbsent(key, value);
		}
		
		@Override
		public short addTo(T key, short value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.addTo(key, value);
		}
		
		@Override
		public short subFrom(T key, short value) {
			if(!inRange(key)) throw new IllegalArgumentException("key out of range");
			return map.subFrom(key, value);
		}
		
		@Override
		public boolean containsKey(Object key) { return inRange((T)key) && map.containsKey(key); }
		@Override
		public short computeShortIfPresent(T key, ObjectShortUnaryOperator<T> mappingFunction) {
			Objects.requireNonNull(mappingFunction);
			map.validate(key);
			if(!inRange(key)) return getDefaultReturnValue();
			Node<T> entry = map.findNode(key);
			if(entry == null || entry.value == getDefaultReturnValue()) return getDefaultReturnValue();
			short newValue = mappingFunction.apply(key, entry.value);
			if(newValue == getDefaultReturnValue()) {
				map.removeNode(entry);
				return newValue;
			}
			entry.value = newValue;
			return newValue;
		}
		
		@Override
		public short rem(T key) {
			return inRange(key) ? map.rem(key) : getDefaultReturnValue();
		}
		
		@Override
		public short remOrDefault(T key, short defaultValue) {
			return inRange(key) ? map.rem(key) : defaultValue;
		}
		
		@Override
		public boolean remove(T key, short value) {
			return inRange(key) && map.remove(key, value);
		}
		
		
		@Override
		public short getShort(T key) {
			return inRange(key) ? map.getShort(key) : getDefaultReturnValue();
		}
		
		@Override
		public short getOrDefault(T key, short defaultValue) {
			return inRange(key) ? map.getOrDefault(key, defaultValue) : getDefaultReturnValue();
		}
		
		
		@Override
		public T lowerKey(T key) { return lowKeyOrNull(subLower(key)); }
		@Override
		public T floorKey(T key) { return lowKeyOrNull(subFloor(key)); }
		@Override
		public T ceilingKey(T key) { return highKeyOrNull(subCeiling(key)); }
		@Override
		public T higherKey(T key) { return highKeyOrNull(subHigher(key)); }
		@Override
		public Object2ShortMap.Entry<T> lowerEntry(T key) { return subLower(key); }
		@Override
		public Object2ShortMap.Entry<T> floorEntry(T key) { return subFloor(key); }
		@Override
		public Object2ShortMap.Entry<T> ceilingEntry(T key) { return subCeiling(key); }
		@Override
		public Object2ShortMap.Entry<T> higherEntry(T key) { return subHigher(key); }
		
		@Override
		public boolean isEmpty() {
			if(fromStart && toEnd) return map.isEmpty();
			Node<T> n = absLowest();
			return n == null || tooHigh(n.key);
		}
		
		@Override
		public int size() { return fromStart && toEnd ? map.size() : entrySet().size(); }
		
		@Override
		public Object2ShortNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public Object2ShortMap.Entry<T> firstEntry() {
			Node<T> entry = subLowest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Object2ShortMap.Entry<T> lastEntry() {
			Node<T> entry = subHighest();
			return entry == null ? null : entry.export();
		}

		@Override
		public Object2ShortMap.Entry<T> pollFirstEntry() {
			Node<T> entry = subLowest();
			if(entry != null) {
				Object2ShortMap.Entry<T> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}

		@Override
		public Object2ShortMap.Entry<T> pollLastEntry() {
			Node<T> entry = subHighest();
			if(entry != null) {
				Object2ShortMap.Entry<T> result = entry.export();
				map.removeNode(entry);
				return result;
			}
			return null;
		}
		
		abstract class SubEntrySet extends AbstractObjectSet<Object2ShortMap.Entry<T>> {
			@Override
			public int size() {
				if (fromStart && toEnd) return map.size();
				int size = 0;
				for(ObjectIterator<Object2ShortMap.Entry<T>> iter = iterator();iter.hasNext();iter.next(),size++);
				return size;
			}
			
			@Override
			public boolean isEmpty() {
				Node<T> n = absLowest();
				return n == null || tooHigh(n.key);
			}
			
			@Override
			public boolean contains(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Object2ShortMap.Entry)
				{
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>) o;
					if(entry.getKey() == null && isNullComparator()) return false;
					T key = entry.getKey();
					if (!inRange(key)) return false;
					Node<T> node = map.findNode(key);
					return node != null && entry.getShortValue() == node.getShortValue();
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				if(entry.getKey() == null && isNullComparator()) return false;
				T key = (T)entry.getKey();
				if (!inRange(key)) return false;
				Node<T> node = map.findNode(key);
				return node != null && Objects.equals(entry.getValue(), Short.valueOf(node.getShortValue()));
			}
			
			@Override
			public boolean remove(Object o) {
				if (!(o instanceof Map.Entry)) return false;
				if(o instanceof Object2ShortMap.Entry)
				{
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>) o;
					T key = entry.getKey();
					if (!inRange(key)) return false;
					Node<T> node = map.findNode(key);
					if (node != null && node.getShortValue() == entry.getShortValue()) {
						map.removeNode(node);
						return true;
					}
					return false;
				}
				Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
				T key = (T)entry.getKey();
				if (!inRange(key)) return false;
				Node<T> node = map.findNode(key);
				if (node != null && Objects.equals(node.getValue(), entry.getValue())) {
					map.removeNode(node);
					return true;
				}
				return false;
			}
			
			@Override
			public void forEach(Consumer<? super Object2ShortMap.Entry<T>> action) {
				Objects.requireNonNull(action);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public <E> void forEach(E input, ObjectObjectConsumer<E, Object2ShortMap.Entry<T>> action) {
				Objects.requireNonNull(action);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, new BasicEntry<>(entry.key, entry.value));
			}
			
			@Override
			public boolean matchesAny(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return false;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return true;
				}
				return false;
			}
			
			@Override
			public boolean matchesNone(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public boolean matchesAll(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return true;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(!filter.getBoolean(subEntry)) return false;
				}
				return true;
			}
			
			@Override
			public <E> E reduce(E identity, BiFunction<E, Object2ShortMap.Entry<T>, E> operator) {
				Objects.requireNonNull(operator);
				E state = identity;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Object2ShortMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2ShortMap.Entry<T>, Object2ShortMap.Entry<T>> operator) {
				Objects.requireNonNull(operator);
				Object2ShortMap.Entry<T> state = null;
				boolean empty = true;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = new BasicEntry<>(entry.key, entry.value);
						continue;
					}
					state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
				}
				return state;
			}
			
			@Override
			public Object2ShortMap.Entry<T> findFirst(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return null;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) return subEntry;
				}
				return null;
			}
			
			@Override
			public int count(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
				Objects.requireNonNull(filter);
				if(size() <= 0) return 0;
				int result = 0;
				BasicEntry<T> subEntry = new BasicEntry<>();
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					subEntry.set(entry.key, entry.value);
					if(filter.getBoolean(subEntry)) result++;
				}
				return result;
			}
		}
		
		final class SubMapValues extends AbstractShortCollection {
			@Override
			public boolean add(short o) { throw new UnsupportedOperationException(); }
			
			@Override
			public boolean contains(short e) {
				return containsValue(e);
			}
			
			@Override
			public ShortIterator iterator() { return valueIterator(); }
			
			@Override
			public int size() {
				return NavigableSubMap.this.size();
			}
			
			@Override
			public void clear() {
				NavigableSubMap.this.clear();
			}
			
			@Override
			public void forEach(ShortConsumer action) {
				Objects.requireNonNull(action);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(entry.value);
			}
			
			@Override
			public <E> void forEach(E input, ObjectShortConsumer<E> action) {
				Objects.requireNonNull(action);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					action.accept(input, entry.value);
			}
			
			@Override
			public boolean matchesAny(Short2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return true;
				return false;
			}
			
			@Override
			public boolean matchesNone(Short2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public boolean matchesAll(Short2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(!filter.get(entry.value)) return false;
				return true;
			}
			
			@Override
			public short reduce(short identity, ShortShortUnaryOperator operator) {
				Objects.requireNonNull(operator);
				short state = identity;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					state = operator.applyAsShort(state, entry.value);
				return state;
			}
			
			@Override
			public short reduce(ShortShortUnaryOperator operator) {
				Objects.requireNonNull(operator);
				short state = (short)0;
				boolean empty = true;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry)) {
					if(empty) {
						empty = false;
						state = entry.value;
						continue;
					}
					state = operator.applyAsShort(state, entry.value);
				}
				return state;
			}
			
			@Override
			public short findFirst(Short2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) return entry.value;
				return (short)0;
			}
			
			@Override
			public int count(Short2BooleanFunction filter) {
				Objects.requireNonNull(filter);
				int result = 0;
				for(Node<T> entry = subLowest(), last = subHighest();entry != null && (last == null || last != previous(entry));entry = next(entry))
					if(filter.get(entry.value)) result++;
				return result;
			}
		}
		
		class DecsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Object2ShortMap.Entry<T>>
		{
			public DecsendingSubEntryIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public Object2ShortMap.Entry<T> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry();
			}

			@Override
			public Object2ShortMap.Entry<T> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry();
			}
		}
		
		class AcsendingSubEntryIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<Object2ShortMap.Entry<T>>
		{
			public AcsendingSubEntryIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public Object2ShortMap.Entry<T> previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry();
			}

			@Override
			public Object2ShortMap.Entry<T> next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry();
			}
		}
		
		class DecsendingSubKeyIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<T>
		{
			public DecsendingSubKeyIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public T previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().key;
			}

			@Override
			public T next() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().key;
			}
		}
		
		class AcsendingSubKeyIterator extends SubMapEntryIterator implements ObjectBidirectionalIterator<T>
		{
			public AcsendingSubKeyIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public T previous() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().key;
			}

			@Override
			public T next() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().key;
			}
		}
		
		class AcsendingSubValueIterator extends SubMapEntryIterator implements ShortBidirectionalIterator
		{
			public AcsendingSubValueIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence);
			}

			@Override
			public short previousShort() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return previousEntry().value;
			}

			@Override
			public short nextShort() {
				if(!hasNext()) throw new NoSuchElementException();
				return nextEntry().value;
			}
		}
		
		class DecsendingSubValueIterator extends SubMapEntryIterator implements ShortBidirectionalIterator
		{
			public DecsendingSubValueIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence) {
				super(first, forwardFence, backwardFence);
			}
			
			@Override
			public short previousShort() {
				if(!hasPrevious()) throw new NoSuchElementException();
				return nextEntry().value;
			}

			@Override
			public short nextShort() {
				if(!hasNext()) throw new NoSuchElementException();
				return previousEntry().value;
			}
		}
		
		abstract class SubMapEntryIterator
		{
			boolean wasForward;
			Node<T> lastReturned;
			Node<T> next;
			boolean unboundForwardFence;
			boolean unboundBackwardFence;
			T forwardFence;
			T backwardFence;
			
			public SubMapEntryIterator(Node<T> first, Node<T> forwardFence, Node<T> backwardFence)
			{
				next = first;
				this.forwardFence = forwardFence == null ? null : forwardFence.key;
				this.backwardFence = backwardFence == null ? null : backwardFence.key;
				unboundForwardFence = forwardFence == null;
				unboundBackwardFence = backwardFence == null;
			}
			
			public boolean hasNext() {
                return next != null && (unboundForwardFence || next.key != forwardFence);
			}
			
			protected Node<T> nextEntry() {
				lastReturned = next;
				Node<T> result = next;
				next = next.next();
				wasForward = true;
				return result;
			}
			
			public boolean hasPrevious() {
                return next != null && (unboundBackwardFence || next.key != backwardFence);
			}
			
			protected Node<T> previousEntry() {
				lastReturned = next;
				Node<T> result = next;
				next = next.previous();
				wasForward = false;
				return result;
			}
			
			public void remove() {
				if(lastReturned == null) throw new IllegalStateException();
				if(wasForward && lastReturned.needsSuccessor()) next = lastReturned;
				map.removeNode(lastReturned);
				lastReturned = null;
			}
		}
	}
	
	class Values extends AbstractShortCollection
	{
		@Override
		public ShortIterator iterator() {
			return new AscendingValueIterator(first);
		}
		
		@Override
		public boolean add(short e) { throw new UnsupportedOperationException(); }
		
		@Override
		public void clear() {
			Object2ShortAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Object2ShortAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(short e) {
			return containsValue(e);
		}
		
		@Override
		public boolean remove(Object o) {
			for(Node<T> entry = first; entry != null; entry = entry.next()) {
				if(Objects.equals(entry.getValue(), o)) {
					removeNode(entry);
					return true;
				}
			}
			return false;
		}
		
		@Override
		public void forEach(ShortConsumer action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(entry.value);
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(input, entry.value);
		}
		
		@Override
		public boolean matchesAny(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return true;
			return false;
		}
		
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(!filter.get(entry.value)) return false;
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			for(Node<T> entry = first;entry != null;entry = entry.next())
				state = operator.applyAsShort(state, entry.value);
			return state;
		}
		
		@Override
		public short reduce(ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = (short)0;
			boolean empty = true;
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = entry.value;
					continue;
				}
				state = operator.applyAsShort(state, entry.value);
			}
			return state;
		}
		
		@Override
		public short findFirst(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) return entry.value;
			return (short)0;
		}
		
		@Override
		public int count(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			int result = 0;
			for(Node<T> entry = first;entry != null;entry = entry.next())
				if(filter.get(entry.value)) result++;
			return result;
		}
	}
	
	class EntrySet extends AbstractObjectSet<Object2ShortMap.Entry<T>> {
		
		@Override
		public ObjectIterator<Object2ShortMap.Entry<T>> iterator() {
			return new AscendingMapEntryIterator(first);
		}
		
		@Override
		public void clear() {
			Object2ShortAVLTreeMap.this.clear();
		}
		
		@Override
		public int size() {
			return Object2ShortAVLTreeMap.this.size;
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Object2ShortMap.Entry)
			{
				Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>) o;
				if(entry.getKey() == null && comparator() == null) return false;
				T key = entry.getKey();
				Node<T> node = findNode(key);
				return node != null && entry.getShortValue() == node.getShortValue();
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			if(entry.getKey() == null && comparator() == null) return false;
			T key = (T)entry.getKey();
			Node<T> node = findNode(key);
			return node != null && Objects.equals(entry.getValue(), Short.valueOf(node.getShortValue()));
		}
		
		@Override
		public boolean remove(Object o) {
			if (!(o instanceof Map.Entry)) return false;
			if(o instanceof Object2ShortMap.Entry)
			{
				Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>) o;
				T key = entry.getKey();
				Node<T> node = findNode(key);
				if (node != null && entry.getShortValue() == node.getShortValue()) {
					removeNode(node);
					return true;
				}
				return false;
			}
			Map.Entry<?,?> entry = (Map.Entry<?,?>) o;
			T key = (T)entry.getKey();
			Node<T> node = findNode(key);
			if (node != null && Objects.equals(entry.getValue(), Short.valueOf(node.getShortValue()))) {
				removeNode(node);
				return true;
			}
			return false;
		}
		
		@Override
		public void forEach(Consumer<? super Object2ShortMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2ShortMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			for(Node<T> entry = first;entry != null;entry = entry.next())
				action.accept(input, new BasicEntry<>(entry.key, entry.value));
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(!filter.getBoolean(subEntry)) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2ShortMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Object2ShortMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2ShortMap.Entry<T>, Object2ShortMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2ShortMap.Entry<T> state = null;
			boolean empty = true;
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				if(empty) {
					empty = false;
					state = new BasicEntry<>(entry.key, entry.value);
					continue;
				}
				state = operator.apply(state, new BasicEntry<>(entry.key, entry.value));
			}
			return state;
		}
		
		@Override
		public Object2ShortMap.Entry<T> findFirst(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) return subEntry;
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Object2ShortMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			BasicEntry<T> subEntry = new BasicEntry<>();
			for(Node<T> entry = first;entry != null;entry = entry.next()) {
				subEntry.set(entry.key, entry.value);
				if(filter.getBoolean(subEntry)) result++;
			}
			return result;
		}
	}
	
	class DescendingKeyIterator extends MapEntryIterator implements ObjectBidirectionalIterator<T>
	{
		public DescendingKeyIterator(Node<T> first) {
			super(first);
		}

		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return nextEntry().key;
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			return previousEntry().key;
		}
	}
	
	class AscendingMapEntryIterator extends MapEntryIterator implements ObjectBidirectionalIterator<Object2ShortMap.Entry<T>>
	{
		public AscendingMapEntryIterator(Node<T> first)
		{
			super(first);
		}

		@Override
		public Object2ShortMap.Entry<T> previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry();
		}

		@Override
		public Object2ShortMap.Entry<T> next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry();
		}
	}
	
	class AscendingValueIterator extends MapEntryIterator implements ShortBidirectionalIterator
	{
		public AscendingValueIterator(Node<T> first) {
			super(first);
		}

		@Override
		public short previousShort() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().value;
		}

		@Override
		public short nextShort() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().value;
		}
	}
	
	class AscendingKeyIterator extends MapEntryIterator implements ObjectBidirectionalIterator<T>
	{
		public AscendingKeyIterator(Node<T> first) {
			super(first);
		}

		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			return previousEntry().key;
		}

		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			return nextEntry().key;
		}
	}
	
	abstract class MapEntryIterator
	{
		boolean wasMoved = false;
		Node<T> lastReturned;
		Node<T> next;
		
		public MapEntryIterator(Node<T> first)
		{
			next = first;
		}
		
		public boolean hasNext() {
            return next != null;
		}
		
		protected Node<T> nextEntry() {
			lastReturned = next;
			Node<T> result = next;
			next = next.next();
			wasMoved = true;
			return result;
		}
		
		public boolean hasPrevious() {
            return next != null;
		}
		
		protected Node<T> previousEntry() {
			lastReturned = next;
			Node<T> result = next;
			next = next.previous();
			wasMoved = false;
			return result;
		}
		
		public void remove() {
			if(lastReturned == null) throw new IllegalStateException();
			if(wasMoved && lastReturned.needsSuccessor()) next = lastReturned;
			removeNode(lastReturned);
			lastReturned = null;
		}
	}
	
	private static final class Node<T> implements Object2ShortMap.Entry<T>
	{
		T key;
		short value;
		int state;
		Node<T> parent;
		Node<T> left;
		Node<T> right;
		
		Node(T key, short value, Node<T> parent) {
			this.key = key;
			this.value = value;
			this.parent = parent;
		}
		
		Node<T> copy() {
			Node<T> entry = new Node<>(key, value, null);
			entry.state = state;
			if(left != null) {
				Node<T> newLeft = left.copy();
				entry.left = newLeft;
				newLeft.parent = entry;
			}
			if(right != null) {
				Node<T> newRight = right.copy();
				entry.right = newRight;
				newRight.parent = entry;
			}
			return entry;
		}
		
		public BasicEntry<T> export() {
			return new BasicEntry<>(key, value);
		}
		
		@Override
		public T getKey() {
			return key;
		}
		
		@Override
		public short getShortValue() {
			return value;
		}
		
		@Override
		public short setValue(short value) {
			short oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		
		short addTo(short value) {
			short oldValue = this.value;
			this.value += value;
			return oldValue;
		}
		
		short subFrom(short value) {
			short oldValue = this.value;
			this.value -= value;
			return oldValue;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2ShortMap.Entry) {
					Object2ShortMap.Entry<T> entry = (Object2ShortMap.Entry<T>)obj;
					if(entry.getKey() == null) return false;
					return Objects.equals(key, entry.getKey()) && value == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object otherKey = entry.getKey();
				if(otherKey == null) return false;
				Object otherValue = entry.getValue();
				return otherValue instanceof Short && Objects.equals(key, otherKey) && value == ((Short)otherValue).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(key) ^ Short.hashCode(value);
		}
		
		@Override
		public String toString() {
			return Objects.toString(key) + "=" + Short.toString(value);
		}
		
		int getHeight() { return state; }
		
		void updateHeight() { state = (1 + Math.max(left == null ? -1 : left.getHeight(), right == null ? -1 : right.getHeight())); }
		
		int getBalance() { return (left == null ? -1 : left.getHeight()) - (right == null ? -1 : right.getHeight()); }
		
		boolean needsSuccessor() { return left != null && right != null; }
		
		boolean replace(Node<T> entry) {
			if(entry != null) entry.parent = parent;
			if(parent != null) {
				if(parent.left == this) parent.left = entry;
				else parent.right = entry;
			}
			return parent == null;
		}
		
		Node<T> next() {
			if(right != null) {
				Node<T> parent = right;
				while(parent.left != null) parent = parent.left;
				return parent;
			}
			Node<T> parent = this.parent;
			Node<T> control = this;
			while(parent != null && control == parent.right) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
		
		Node<T> previous() {
			if(left != null) {
				Node<T> parent = left;
				while(parent.right != null) parent = parent.right;
				return parent;
			}
			Node<T> parent = this.parent;
			Node<T> control = this;
			while(parent != null && control == parent.left) {
				control = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}
}