package speiger.src.collections.doubles.maps.impl.hash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.functions.DoubleConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectDoubleConsumer;
import speiger.src.collections.doubles.functions.consumer.DoubleCharConsumer;
import speiger.src.collections.doubles.functions.function.Double2CharFunction;
import speiger.src.collections.doubles.functions.function.DoubleCharUnaryOperator;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.function.Double2BooleanFunction;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.maps.abstracts.AbstractDouble2CharMap;
import speiger.src.collections.doubles.maps.interfaces.Double2CharMap;
import speiger.src.collections.doubles.utils.maps.Double2CharMaps;
import speiger.src.collections.doubles.sets.AbstractDoubleSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.chars.collections.AbstractCharCollection;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.functions.CharSupplier;
import speiger.src.collections.chars.functions.function.CharCharUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;import speiger.src.collections.chars.collections.CharIterator;
import speiger.src.collections.chars.functions.CharConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;
import speiger.src.collections.chars.functions.function.Char2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 */
public class Double2CharOpenHashMap extends AbstractDouble2CharMap implements ITrimmable
{
	/** The Backing keys array */
	protected transient double[] keys;
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
	protected transient FastEntrySet entrySet;
	/** KeySet cache */
	protected transient DoubleSet keySet;
	/** Values cache */
	protected transient CharCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	
	/**
	 * Default Constructor
	 */
	public Double2CharOpenHashMap() {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Double2CharOpenHashMap(int minCapacity) {
		this(minCapacity, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Constructor that defines the minimum capacity and load factor
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the minimum capacity is negative
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Double2CharOpenHashMap(int minCapacity, float loadFactor) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new double[nullIndex + 1];
		values = new char[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2CharOpenHashMap(Double[] keys, Character[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Double2CharOpenHashMap(Double[] keys, Character[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].doubleValue(), values[i].charValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Double2CharOpenHashMap(double[] keys, char[] values) {
		this(keys, values, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Double2CharOpenHashMap(double[] keys, char[] values, float loadFactor) {
		this(keys.length, loadFactor);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i], values[i]);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public Double2CharOpenHashMap(Map<? extends Double, ? extends Character> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public Double2CharOpenHashMap(Map<? extends Double, ? extends Character> map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public Double2CharOpenHashMap(Double2CharMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public Double2CharOpenHashMap(Double2CharMap map, float loadFactor) {
		this(map.size(), loadFactor);
		putAll(map);
	}
	
	@Override
	public char put(double key, char value) {
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
	public char putIfAbsent(double key, char value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		return values[slot];
	}
	
	@Override
	public char addTo(double key, char value) {
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
	public char subFrom(double key, char value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		char oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	@Override
	public boolean containsKey(double key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(char value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(Double.doubleToLongBits(keys[i]) != 0 && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Character.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(Double.doubleToLongBits(keys[i]) != 0 && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Character.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public char remove(double key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public char removeOrDefault(double key, char defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Character remove(Object key) {
		int slot = findIndex(key);
		if(slot < 0) return Character.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(double key, char value) {
		if(Double.doubleToLongBits(key) == 0) {
			if(containsNull && value == values[nullIndex]) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(Double.hashCode(key)) & mask;
		double current = keys[pos];
		if(Double.doubleToLongBits(current) == 0) return false;
		if(Double.doubleToLongBits(current) == Double.doubleToLongBits(key) && value == values[pos]) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) == 0) return false;
			else if(Double.doubleToLongBits(current) == Double.doubleToLongBits(key) && value == values[pos]) {
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
		int pos = HashUtil.mix(key.hashCode()) & mask;
		double current = keys[pos];
		if(Double.doubleToLongBits(current) == 0) return false;
		if(Objects.equals(key, Double.valueOf(current)) && Objects.equals(value, Character.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) == 0) return false;
			else if(Objects.equals(key, Double.valueOf(current)) && Objects.equals(value, Character.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public char get(double key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Character get(Object key) {
		int slot = findIndex(key);
		return Character.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public char getOrDefault(double key, char defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Double2CharOpenHashMap copy() {
		Double2CharOpenHashMap map = new Double2CharOpenHashMap(0, loadFactor);
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
	public ObjectSet<Double2CharMap.Entry> double2CharEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public DoubleSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public CharCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(DoubleCharConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(Double.doubleToLongBits(keys[i]) != 0) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(double key, char oldValue, char newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public char replace(double key, char value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		char oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public char computeChar(double key, DoubleCharUnaryOperator mappingFunction) {
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
	public char computeCharIfAbsent(double key, Double2CharFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			char newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		char newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public char supplyCharIfAbsent(double key, CharSupplier valueProvider) {
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
	public char computeCharIfPresent(double key, DoubleCharUnaryOperator mappingFunction) {
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
	public char mergeChar(double key, char value, CharCharUnaryOperator mappingFunction) {
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
	public void mergeAllChar(Double2CharMap m, CharCharUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Double2CharMap.Entry entry : Double2CharMaps.fastIterable(m)) {
			double key = entry.getDoubleKey();
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
		Arrays.fill(keys, 0D);
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
		keys = new double[request + 1];
		values = new char[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	protected int findIndex(double key) {
		if(Double.doubleToLongBits(key) == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Double.hashCode(key)) & mask;
		double current = keys[pos];
		if(Double.doubleToLongBits(current) != 0) {
			if(Double.doubleToLongBits(current) == Double.doubleToLongBits(key)) return pos;
			while(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) != 0)
				if(Double.doubleToLongBits(current) == Double.doubleToLongBits(key)) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		double current = keys[pos];
		if(Double.doubleToLongBits(current) != 0) {
			if(Objects.equals(key, Double.valueOf(current))) return pos;
			while(Double.doubleToLongBits((current = keys[pos = (++pos & mask)])) != 0)
				if(Objects.equals(key, Double.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected char removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		char value = values[pos];
		keys[pos] = 0D;
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
		keys[nullIndex] = 0D;
		values[nullIndex] = (char)0;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, double key, char value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		double[] newKeys = new double[newSize + 1];
		char[] newValues = new char[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(Double.doubleToLongBits(keys[i]) != 0) break;
			}
			if(Double.doubleToLongBits(newKeys[pos = HashUtil.mix(Double.hashCode(keys[i])) & newMask]) != 0)
				while(Double.doubleToLongBits(newKeys[pos = (++pos & newMask)]) != 0);
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
		double current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if(Double.doubleToLongBits((current = keys[startPos])) == 0) {
					keys[last] = 0D;
					values[last] = (char)0;
					return;
				}
				slot = HashUtil.mix(Double.hashCode(current)) & mask;
				if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
				startPos = ++startPos & mask;
			}
			keys[last] = current;
			values[last] = values[startPos];
			onNodeMoved(startPos, last);
		}
	}
	
	protected class MapEntry implements Double2CharMap.Entry, Map.Entry<Double, Character> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public double getDoubleKey() {
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
				if(obj instanceof Double2CharMap.Entry) {
					Double2CharMap.Entry entry = (Double2CharMap.Entry)obj;
					return Double.doubleToLongBits(keys[index]) == Double.doubleToLongBits(entry.getDoubleKey()) && values[index] == entry.getCharValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Double && value instanceof Character && Double.doubleToLongBits(keys[index]) == Double.doubleToLongBits(((Double)key).doubleValue()) && values[index] == ((Character)value).charValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Double.hashCode(keys[index]) ^ Character.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Double.toString(keys[index]) + "=" + Character.toString(values[index]);
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Double2CharMap.Entry> implements Double2CharMap.FastEntrySet {
		@Override
		public ObjectIterator<Double2CharMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Double2CharMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Double2CharMap.Entry> action) {
			if(containsNull) action.accept(new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--)
				if(Double.doubleToLongBits(keys[i]) != 0) action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Double2CharMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Double2CharMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Double2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Double2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Double2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(!filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(!filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Double2CharMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) == 0) continue;
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Double2CharMap.Entry reduce(ObjectObjectUnaryOperator<Double2CharMap.Entry, Double2CharMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Double2CharMap.Entry state = null;
			boolean empty = true;
			if(containsNull) {
				state = new BasicEntry(keys[nullIndex], values[nullIndex]);
				empty = false;
			}
			for(int i = 0;i<size;i++) {
				if(Double.doubleToLongBits(keys[i]) == 0) continue;
				if(empty) {
					empty = false;
					state = new BasicEntry(keys[i], values[i]);
					continue;
				}
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Double2CharMap.Entry findFirst(Object2BooleanFunction<Double2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Double2CharMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
			int result = 0;
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Double2CharOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Double2CharOpenHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Double2CharMap.Entry) {
					Double2CharMap.Entry entry = (Double2CharMap.Entry)o;
					int index = Double2CharOpenHashMap.this.findIndex(entry.getDoubleKey());
					if(index >= 0) return entry.getCharValue() == Double2CharOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = Double2CharOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Character.valueOf(Double2CharOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Double2CharMap.Entry) {
					Double2CharMap.Entry entry = (Double2CharMap.Entry)o;
					return Double2CharOpenHashMap.this.remove(entry.getDoubleKey(), entry.getCharValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Double2CharOpenHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractDoubleSet {
		@Override
		public boolean contains(double e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(double o) {
			int oldSize = size;
			Double2CharOpenHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(double o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public DoubleIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public int size() {
			return Double2CharOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Double2CharOpenHashMap.this.clear();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(DoubleConsumer action) {
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(Double.doubleToLongBits(keys[i]) != 0) action.accept(keys[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectDoubleConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && !filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public double reduce(double identity, DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = identity;
			if(containsNull) state = operator.applyAsDouble(state, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) == 0) continue;
				state = operator.applyAsDouble(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public double reduce(DoubleDoubleUnaryOperator operator) {
			Objects.requireNonNull(operator);
			double state = 0D;
			boolean empty = true;
			if(containsNull) {
				state = keys[nullIndex];
				empty = false;
			}
			for(int i = 0;i<size;i++) {
				if(Double.doubleToLongBits(keys[i]) == 0) continue;
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.applyAsDouble(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public double findFirst(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0D;
			if(containsNull && filter.get(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(keys[i])) return keys[i];
			}
			return 0D;
		}
		
		@Override
		public int count(Double2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(keys[i])) result++;
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
			return Double2CharOpenHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Double2CharOpenHashMap.this.clear();
		}
		
		@Override
		public void forEach(CharConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(Double.doubleToLongBits(keys[i]) != 0) action.accept(values[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectCharConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Char2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) != 0 && !filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public char reduce(char identity, CharCharUnaryOperator operator) {
			Objects.requireNonNull(operator);
			char state = identity;
			if(containsNull) state = operator.applyAsChar(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(Double.doubleToLongBits(keys[i]) == 0) continue;
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
			for(int i = 0;i<size;i++) {
				if(Double.doubleToLongBits(keys[i]) == 0) continue;
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
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(values[i])) return values[i];
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
				if(Double.doubleToLongBits(keys[i]) != 0 && filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Double2CharMap.Entry> {
		MapEntry entry = new MapEntry();
		@Override
		public Double2CharMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Double2CharMap.Entry> {
		MapEntry entry;
		@Override
		public Double2CharMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements DoubleIterator {
		@Override
		public double nextDouble() {
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
		DoubleList wrapped = null;
		
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
						if(Double.doubleToLongBits(keys[pos]) != 0){
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
				int value = findIndex(wrapped.getDouble(nextIndex));
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
				keys[nullIndex] = 0D;
				values[nullIndex] = (char)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Double2CharOpenHashMap.this.remove(wrapped.getDouble(-returnedPos - 1));
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			double current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(Double.doubleToLongBits((current = keys[startPos])) == 0) {
						keys[last] = 0D;
						values[last] = (char)0;
						return;
					}
					slot = HashUtil.mix(Double.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) {
					if(wrapped == null) wrapped = new DoubleArrayList(2);
					wrapped.add(keys[startPos]);
				}
				keys[last] = current;
				values[last] = values[startPos];
			}
		}
	}
}