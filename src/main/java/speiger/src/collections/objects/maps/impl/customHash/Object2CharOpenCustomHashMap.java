package speiger.src.collections.objects.maps.impl.customHash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.objects.functions.function.Object2CharFunction;
import speiger.src.collections.objects.functions.function.ObjectCharUnaryOperator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2CharMap;
import speiger.src.collections.objects.maps.interfaces.Object2CharMap;
import speiger.src.collections.objects.utils.maps.Object2CharMaps;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;

import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific HashMap that allows for custom HashControl.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 * @param <T> the type of elements maintained by this Collection
 */
public class Object2CharOpenCustomHashMap<T> extends AbstractObject2CharMap<T> implements ITrimmable
{
	/** The Backing keys array */
	protected transient T[] keys;
	/** The Backing values array */
	protected transient char[] values;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Minimum array size the HashMap will be */
	protected transient int minCapacity;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Maximum amount of Values that can be stored before the array gets expanded usually 75% */
	protected transient int maxFill;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** EntrySet cache */
	protected transient FastEntrySet<T> entrySet;
	/** KeySet cache */
	protected transient ObjectSet<T> keySet;
	/** Values cache */
	protected transient CharCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	/** Strategy that allows to control the Hash Generation and equals comparason */
	protected final ObjectStrategy<? super T> strategy;
	
	/**
	 * Default Contstructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2CharOpenCustomHashMap(ObjectStrategy<? super T> strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Object2CharOpenCustomHashMap(int minCapacity, ObjectStrategy<? super T> strategy) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2CharOpenCustomHashMap(int minCapacity, float loadFactor, ObjectStrategy<? super T> strategy) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		this.strategy = strategy;
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = (T[])new Object[nullIndex + 1];
		values = new char[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2CharOpenCustomHashMap(T[] keys, Character[] values, ObjectStrategy<? super T> strategy) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2CharOpenCustomHashMap(T[] keys, Character[] values, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i].charValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Object2CharOpenCustomHashMap(T[] keys, char[] values, ObjectStrategy<? super T> strategy) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2CharOpenCustomHashMap(T[] keys, char[] values, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2CharOpenCustomHashMap(Map<? extends T, ? extends Character> map, ObjectStrategy<? super T> strategy) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Object2CharOpenCustomHashMap(Map<? extends T, ? extends Character> map, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Object2CharOpenCustomHashMap(Object2CharMap<T> map, ObjectStrategy<? super T> strategy) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Object2CharOpenCustomHashMap(Object2CharMap<T> map, float loadFactor, ObjectStrategy<? super T> strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public char put(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		char oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public char putIfAbsent(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		return values[slot];
	}
	
	@Override
	public char addTo(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		char oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public char subFrom(T key, char value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		char oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	@Override
	public boolean containsKey(Object key) {
		return findIndex((T)key) >= 0;
	}
	
	@Override
	public boolean containsValue(char value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex;i >= 0;i--)
			if(!strategy.equals(keys[i], null) && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Character.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(!strategy.equals(keys[i], null) && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Character.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public char rem(T key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public char remOrDefault(T key, char defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Character remove(Object key) {
		int slot = findIndex((T)key);
		if(slot < 0) return Character.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(T key, char value) {
		if(strategy.equals(key, null)) {
			if(containsNull && value == values[nullIndex]) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		T current = keys[pos];
		if(strategy.equals(current, null)) return false;
		if(strategy.equals(current, key) && value == values[pos]) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), null)) return false;
			else if(strategy.equals(current, key) && value == values[pos]) {
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		Objects.requireNonNull(value);
		if(key == null) {
			if(containsNull && Objects.equals(value, Character.valueOf(values[nullIndex]))) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		T keyType = (T)key;
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		T current = keys[pos];
		if(strategy.equals(current, null)) return false;
		if(strategy.equals(current, keyType) && Objects.equals(value, Character.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), null)) return false;
			else if(strategy.equals(current, keyType) && Objects.equals(value, Character.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public char getChar(T key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Character get(Object key) {
		int slot = findIndex((T)key);
		return Character.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public char getOrDefault(T key, char defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Object2CharOpenCustomHashMap<T> copy() {
		Object2CharOpenCustomHashMap<T> map = new Object2CharOpenCustomHashMap<>(0, loadFactor, strategy);
		map.minCapacity = minCapacity;
		map.mask = mask;
		map.maxFill = maxFill;
		map.nullIndex = nullIndex;
		map.containsNull = containsNull;
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, values.length);
		return map;
	}
	
	@Override
	public ObjectSet<Object2CharMap.Entry<T>> object2CharEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public ObjectSet<T> keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public CharCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(ObjectCharConsumer<T> action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], null)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(T key, char oldValue, char newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public char replace(T key, char value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		char oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public char computeChar(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = mappingFunction.applyAsChar(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = mappingFunction.applyAsChar(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public char computeCharIfAbsent(T key, Object2CharFunction<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = mappingFunction.getChar(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.getChar(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public char supplyCharIfAbsent(T key, CharSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = valueProvider.getChar();
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getChar();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}

	@Override
	public char computeCharIfPresent(T key, ObjectCharUnaryOperator<T> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		char newValue = mappingFunction.applyAsChar(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public char mergeChar(T key, char value, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		char newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsChar(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllChar(Object2CharMap<T> m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Object2CharMap.Entry<T> entry : Object2CharMaps.fastIterable(m)) {
			T key = entry.getKey();
			int index = findIndex(key);
			char newValue = index < 0 || values[index] == getDefaultReturnValue() ? entry.getCharValue() : mappingFunction.applyAsChar(values[index], entry.getCharValue());
			if(newValue == getDefaultReturnValue()) {
				if(index >= 0)
					removeIndex(index);
			}
			else if(index < 0) insert(-index-1, key, newValue);
			else values[index] = newValue;
		}
	}
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() {
		if(size == 0) return;
		size = 0;
		containsNull = false;
		Arrays.fill(keys, null);
		Arrays.fill(values, (char)0);
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= size || this.size > Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
		try {
			rehash(request);
		}
		catch(OutOfMemoryError noMemory) { return false; }
		return true;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= size) {
			clear();
			return;
		}
		nullIndex = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = (T[])new Object[request + 1];
		values = new char[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	protected int findIndex(T key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		T keyType = (T)key;
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		T current = keys[pos];
		if(!strategy.equals(current, null)) {
			if(strategy.equals(current, keyType)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), null))
				if(strategy.equals(current, keyType)) return pos;
		}
		return -(pos + 1);
	}
	
	protected char removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		char value = values[pos];
		keys[pos] = null;
		values[pos] = (char)0;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected char removeNullIndex() {
		char value = values[nullIndex];
		containsNull = false;
		keys[nullIndex] = null;
		values[nullIndex] = (char)0;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, T key, char value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		T[] newKeys = (T[])new Object[newSize + 1];
		char[] newValues = new char[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(!strategy.equals(keys[i], null)) break;
			}
			if(!strategy.equals(newKeys[pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask], null))
				while(!strategy.equals(newKeys[pos = (++pos & newMask)], null));
			newKeys[pos] = keys[i];
			newValues[pos] = values[i];
		}
		newValues[newSize] = values[nullIndex];
		nullIndex = newSize;
		mask = newMask;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = newKeys;
		values = newValues;
	}
	
	protected void onNodeAdded(int pos) {
		
	}
	
	protected void onNodeRemoved(int pos) {
		
	}
	
	protected void onNodeMoved(int from, int to) {
		
	}
	
	protected void shiftKeys(int startPos) {
		int slot, last;
		T current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if(strategy.equals((current = keys[startPos]), null)) {
					keys[last] = null;
					values[last] = (char)0;
					return;
				}
				slot = HashUtil.mix(strategy.hashCode(current)) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current;
			values[last] = values[startPos];
			onNodeMoved(startPos, last);
		}
	}
	
	protected class MapEntry implements Object2CharMap.Entry<T>, Map.Entry<T, Character> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public T getKey() {
			return keys[index];
		}

		@Override
		public char getCharValue() {
			return values[index];
		}

		@Override
		public char setValue(char value) {
			char oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Object2CharMap.Entry) {
					Object2CharMap.Entry<T> entry = (Object2CharMap.Entry<T>)obj;
					return Objects.equals(keys[index], entry.getKey()) && values[index] == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return value instanceof Character && Objects.equals(keys[index], key) && values[index] == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return strategy.hashCode(keys[index]) ^ Character.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Objects.toString(keys[index]) + "=" + Character.toString(values[index]);
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Object2CharMap.Entry<T>> implements Object2CharMap.FastEntrySet<T> {
		@Override
		public ObjectIterator<Object2CharMap.Entry<T>> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Object2CharMap.Entry<T>> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Object2CharMap.Entry<T>> action) {
			if(containsNull) action.accept(new BasicEntry<>(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], null)) action.accept(new BasicEntry<>(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Object2CharMap.Entry<T>> action) {
			BasicEntry<T> entry = new BasicEntry<>();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(keys[i], values[i]);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Object2CharMap.Entry<T>> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new BasicEntry<>(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(input, new BasicEntry<>(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry<T> entry = new BasicEntry<>();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> entry = new BasicEntry<>();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry<T> entry = new BasicEntry<>();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(!filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(keys[i], values[i]);
					if(!filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Object2CharMap.Entry<T>, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new BasicEntry<>(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				state = operator.apply(state, new BasicEntry<>(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Object2CharMap.Entry<T> reduce(ObjectObjectUnaryOperator<Object2CharMap.Entry<T>, Object2CharMap.Entry<T>> operator) {
			Objects.requireNonNull(operator);
			Object2CharMap.Entry<T> state = null;
			boolean empty = true;
			if(containsNull) {
				state = new BasicEntry<>(keys[nullIndex], values[nullIndex]);
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				if(empty) {
					empty = false;
					state = new BasicEntry<>(keys[i], values[i]);
					continue;
				}
				state = operator.apply(state, new BasicEntry<>(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Object2CharMap.Entry<T> findFirst(Object2BooleanFunction<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry<T> entry = new BasicEntry<>();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Object2CharMap.Entry<T>> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry<T> entry = new BasicEntry<>();
			int result = 0;
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Object2CharOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2CharOpenCustomHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2CharMap.Entry) {
					Object2CharMap.Entry<T> entry = (Object2CharMap.Entry<T>)o;
					int index = Object2CharOpenCustomHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return entry.getCharValue() == Object2CharOpenCustomHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Object2CharOpenCustomHashMap.this.findIndex((T)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Character.valueOf(Object2CharOpenCustomHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Object2CharMap.Entry) {
					Object2CharMap.Entry<T> entry = (Object2CharMap.Entry<T>)o;
					return Object2CharOpenCustomHashMap.this.remove(entry.getKey(), entry.getCharValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Object2CharOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractObjectSet<T> {
		@Override
		public boolean contains(Object e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(Object o) {
			int oldSize = size;
			Object2CharOpenCustomHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(T o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ObjectIterator<T> iterator() {
			return new KeyIterator();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Object2CharOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2CharOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(Consumer<? super T> action) {
			Objects.requireNonNull(action);
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], null)) action.accept(keys[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.getBoolean(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.getBoolean(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.getBoolean(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.getBoolean(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.getBoolean(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && !filter.getBoolean(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				state = operator.apply(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
			Objects.requireNonNull(operator);
			T state = null;
			boolean empty = true;
			if(containsNull) {
				state = keys[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.apply(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public T findFirst(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			if(containsNull && filter.getBoolean(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.getBoolean(keys[i])) return keys[i];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<T> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.getBoolean(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.getBoolean(keys[i])) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractCharCollection {
		@Override
		public boolean contains(char e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(char o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public CharIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Object2CharOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Object2CharOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(CharConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], null)) action.accept(values[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null)) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && !filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			if(containsNull) state = operator.applyAsChar(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				state = operator.applyAsChar(state, values[i]);
			}
			return state;
		}
		
		@Override
		public char reduce(CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = (char)0;
			boolean empty = true;
			if(containsNull) {
				state = values[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], null)) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsChar(state, values[i]);
			}
			return state;
		}
		
		@Override
		public char findFirst(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (char)0;
			if(containsNull && filter.get(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.get(values[i])) return values[i];
			}
			return (char)0;
		}
		
		@Override
		public int count(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], null) && filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<T>> {
		MapEntry entry = new MapEntry();
		@Override
		public Object2CharMap.Entry<T> next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<T>> {
		MapEntry entry;
		@Override
		public Object2CharMap.Entry<T> next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements ObjectIterator<T> {
		@Override
		public T next() {
			return keys[nextEntry()];
		}
	}
	
	private class ValueIterator extends MapIterator implements CharIterator {
		@Override
		public char nextChar() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		ObjectList<T> wrapped = null;
		
		public boolean hasNext() {
			if(nextIndex == Integer.MIN_VALUE) {
				if(returnNull) {
					returnNull = false;
					nextIndex = nullIndex;
				}
				else
				{
					while(true) {
						if(--pos < 0) {
							if(wrapped == null || wrapped.size() <= -pos - 1) break;
							nextIndex = -pos - 1;
							break;
						}
						if(!strategy.equals(keys[pos], null)){
							nextIndex = pos;
							break;
						}
					}
				}
			}
			return nextIndex != Integer.MIN_VALUE;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			returnedPos = pos;
			if(nextIndex < 0){
				lastReturned = Integer.MAX_VALUE;
				int value = findIndex(wrapped.get(nextIndex));
				if(value < 0) throw new IllegalStateException("Entry ["+nextIndex+"] was removed during Iteration");
				nextIndex = Integer.MIN_VALUE;
				return value;
			}
			int value = (lastReturned = nextIndex);
			nextIndex = Integer.MIN_VALUE;
			return value;
		}
		
		public void remove() {
			if(lastReturned == -1) throw new IllegalStateException();
			if(lastReturned == nullIndex) {
				containsNull = false;
				keys[nullIndex] = null;
				values[nullIndex] = (char)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Object2CharOpenCustomHashMap.this.remove(wrapped.get(-returnedPos - 1));
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			T current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(strategy.equals((current = keys[startPos]), null)) {
						keys[last] = null;
						values[last] = (char)0;
						return;
					}
					slot = HashUtil.mix(strategy.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) {
					if(wrapped == null) wrapped = new ObjectArrayList<>(2);
					wrapped.add(keys[startPos]);
				}
				keys[last] = current;
				values[last] = values[startPos];
			}
		}
	}
}