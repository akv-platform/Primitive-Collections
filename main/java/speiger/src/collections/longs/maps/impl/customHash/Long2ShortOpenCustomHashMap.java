package speiger.src.collections.longs.maps.impl.customHash;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.longs.collections.LongIterator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.consumer.LongShortConsumer;
import speiger.src.collections.longs.functions.function.Long2ShortFunction;
import speiger.src.collections.longs.functions.function.LongShortUnaryOperator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2ShortMap;
import speiger.src.collections.longs.maps.interfaces.Long2ShortMap;
import speiger.src.collections.longs.utils.maps.Long2ShortMaps;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongStrategy;
import speiger.src.collections.shorts.collections.AbstractShortCollection;
import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.functions.ShortSupplier;
import speiger.src.collections.shorts.functions.function.ShortShortUnaryOperator;

import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;import speiger.src.collections.shorts.collections.ShortIterator;
import speiger.src.collections.shorts.functions.ShortConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectShortConsumer;
import speiger.src.collections.shorts.functions.function.Short2BooleanFunction;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.ITrimmable;

/**
 * A Type Specific HashMap that allows for custom HashControl.
 * For cases where Objects/primitive do not allow hashcoding this can be really useful and provide a lot of control.
 */
public class Long2ShortOpenCustomHashMap extends AbstractLong2ShortMap implements ITrimmable
{
	/** The Backing keys array */
	protected transient long[] keys;
	/** The Backing values array */
	protected transient short[] values;
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
	protected transient LongSet keySet;
	/** Values cache */
	protected transient ShortCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	/** How full the Arrays are allowed to get before resize */
	protected final float loadFactor;
	/** Strategy that allows to control the Hash Generation and equals comparason */
	protected final LongStrategy strategy;
	
	/**
	 * Default Contstructor
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Long2ShortOpenCustomHashMap(LongStrategy strategy) {
		this(HashUtil.DEFAULT_MIN_CAPACITY, HashUtil.DEFAULT_LOAD_FACTOR, strategy);
	}
	
	/**
	 * Constructor that defines the minimum capacity
	 * @param minCapacity the minimum capacity the HashMap is allowed to be.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the minimum capacity is negative
	 */
	public Long2ShortOpenCustomHashMap(int minCapacity, LongStrategy strategy) {
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
	public Long2ShortOpenCustomHashMap(int minCapacity, float loadFactor, LongStrategy strategy) {
		if(minCapacity < 0)	throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
		if(loadFactor <= 0 || loadFactor >= 1F) throw new IllegalStateException("Load Factor is not between 0 and 1");
		this.loadFactor = loadFactor;
		this.minCapacity = nullIndex = HashUtil.arraySize(minCapacity, loadFactor);
		this.strategy = strategy;
		mask = nullIndex - 1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new long[nullIndex + 1];
		values = new short[nullIndex + 1];
	}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Long2ShortOpenCustomHashMap(Long[] keys, Short[] values, LongStrategy strategy) {
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
	public Long2ShortOpenCustomHashMap(Long[] keys, Short[] values, float loadFactor, LongStrategy strategy) {
		this(keys.length, loadFactor, strategy);
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		for(int i = 0,m=keys.length;i<m;i++) put(keys[i].longValue(), values[i].shortValue());
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public Long2ShortOpenCustomHashMap(long[] keys, short[] values, LongStrategy strategy) {
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
	public Long2ShortOpenCustomHashMap(long[] keys, short[] values, float loadFactor, LongStrategy strategy) {
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
	public Long2ShortOpenCustomHashMap(Map<? extends Long, ? extends Short> map, LongStrategy strategy) {
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
	public Long2ShortOpenCustomHashMap(Map<? extends Long, ? extends Short> map, float loadFactor, LongStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 * @param strategy the strategy that allows hash control.
	 * @throws NullPointerException if Strategy is null
	 */
	public Long2ShortOpenCustomHashMap(Long2ShortMap map, LongStrategy strategy) {
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
	public Long2ShortOpenCustomHashMap(Long2ShortMap map, float loadFactor, LongStrategy strategy) {
		this(map.size(), loadFactor, strategy);
		putAll(map);
	}
	
	@Override
	public short put(long key, short value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		short oldValue = values[slot];
		values[slot] = value;
		return oldValue;
	}
	
	@Override
	public short putIfAbsent(long key, short value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		return values[slot];
	}
	
	@Override
	public short addTo(long key, short value) {
		int slot = findIndex(key);
		if(slot < 0) {
			insert(-slot-1, key, value);
			return getDefaultReturnValue();
		}
		short oldValue = values[slot];
		values[slot] += value;
		return oldValue;
	}
	
	@Override
	public short subFrom(long key, short value) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		short oldValue = values[slot];
		values[slot] -= value;
		if(value < 0 ? (values[slot] >= getDefaultReturnValue()) : (values[slot] <= getDefaultReturnValue())) removeIndex(slot);
		return oldValue;
	}
	@Override
	public boolean containsKey(long key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return key instanceof Long && findIndex(((Long)key).longValue()) >= 0;
	}
	
	@Override
	public boolean containsValue(short value) {
		if(containsNull && values[nullIndex] == value) return true;
		for(int i = nullIndex;i >= 0;i--)
			if(!strategy.equals(keys[i], 0L) && values[i] == value) return true;
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		if(containsNull && ((value == null && values[nullIndex] == getDefaultReturnValue()) || Objects.equals(value, Short.valueOf(values[nullIndex])))) return true;
		for(int i = nullIndex-1;i >= 0;i--)
			if(!strategy.equals(keys[i], 0L) && ((value == null && values[i] == getDefaultReturnValue()) || Objects.equals(value, Short.valueOf(values[i])))) return true;
		return false;
	}
	
	@Override
	public short remove(long key) {
		int slot = findIndex(key);
		if(slot < 0) return getDefaultReturnValue();
		return removeIndex(slot);
	}
	
	@Override
	public short removeOrDefault(long key, short defaultValue) {
		int slot = findIndex(key);
		if(slot < 0) return defaultValue;
		return removeIndex(slot);
	}
	
	@Override
	public Short remove(Object key) {
		if(!(key instanceof Long)) return getDefaultReturnValue();
		int slot = findIndex(((Long)key).longValue());
		if(slot < 0) return Short.valueOf(getDefaultReturnValue());
		return removeIndex(slot);
	}
	
	@Override
	public boolean remove(long key, short value) {
		if(strategy.equals(key, 0L)) {
			if(containsNull && value == values[nullIndex]) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		long current = keys[pos];
		if(strategy.equals(current, 0L)) return false;
		if(strategy.equals(current, key) && value == values[pos]) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), 0L)) return false;
			else if(strategy.equals(current, key) && value == values[pos]) {
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		Objects.requireNonNull(value);
		if(key == null || (key instanceof Long && strategy.equals(((Long)key).longValue(), 0L))) {
			if(containsNull && Objects.equals(value, Short.valueOf(values[nullIndex]))) {
				removeNullIndex();
				return true;
			}
			return false;
		}
		if(!(key instanceof Long)) return false;
		long keyType = ((Long)key).longValue();
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		long current = keys[pos];
		if(strategy.equals(current, 0L)) return false;
		if(strategy.equals(current, keyType) && Objects.equals(value, Short.valueOf(values[pos]))) {
			removeIndex(pos);
			return true;
		}
		while(true) {
			if(strategy.equals((current = keys[pos = (++pos & mask)]), 0L)) return false;
			else if(strategy.equals(current, keyType) && Objects.equals(value, Short.valueOf(values[pos]))){
				removeIndex(pos);
				return true;
			}
		}
	}
	
	@Override
	public short get(long key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Short get(Object key) {
		if(!(key instanceof Long)) return getDefaultReturnValue();
		int slot = findIndex(((Long)key).longValue());
		return Short.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public short getOrDefault(long key, short defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public Long2ShortOpenCustomHashMap copy() {
		Long2ShortOpenCustomHashMap map = new Long2ShortOpenCustomHashMap(0, loadFactor, strategy);
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
	public ObjectSet<Long2ShortMap.Entry> long2ShortEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public LongSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public ShortCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public void forEach(LongShortConsumer action) {
		if(size() <= 0) return;
		if(containsNull) action.accept(keys[nullIndex], values[nullIndex]);
		for(int i = nullIndex-1;i>=0;i--) {
			if(!strategy.equals(keys[i], 0L)) action.accept(keys[i], values[i]);
		}
	}
	
	@Override
	public boolean replace(long key, short oldValue, short newValue) {
		int index = findIndex(key);
		if(index < 0 || values[index] != oldValue) return false;
		values[index] = newValue;
		return true;
	}
	
	@Override
	public short replace(long key, short value) {
		int index = findIndex(key);
		if(index < 0) return getDefaultReturnValue();
		short oldValue = values[index];
		values[index] = value;
		return oldValue;
	}
	
	@Override
	public short computeShort(long key, LongShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = mappingFunction.applyAsShort(key, getDefaultReturnValue());
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short computeShortIfAbsent(long key, Long2ShortFunction mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = mappingFunction.get(key);
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}
	
	@Override
	public short supplyShortIfAbsent(long key, ShortSupplier valueProvider) {
		Objects.requireNonNull(valueProvider);
		int index = findIndex(key);
		if(index < 0) {
			short newValue = valueProvider.getShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			insert(-index-1, key, newValue);
			return newValue;
		}
		short newValue = values[index];
		if(newValue == getDefaultReturnValue()) {
			newValue = valueProvider.getShort();
			if(newValue == getDefaultReturnValue()) return newValue;
			values[index] = newValue;
		}
		return newValue;
	}

	@Override
	public short computeShortIfPresent(long key, LongShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		if(index < 0 || values[index] == getDefaultReturnValue()) return getDefaultReturnValue();
		short newValue = mappingFunction.applyAsShort(key, values[index]);
		if(newValue == getDefaultReturnValue()) {
			removeIndex(index);
			return newValue;
		}
		values[index] = newValue;
		return newValue;
	}
	
	@Override
	public short mergeShort(long key, short value, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		int index = findIndex(key);
		short newValue = index < 0 || values[index] == getDefaultReturnValue() ? value : mappingFunction.applyAsShort(values[index], value);
		if(newValue == getDefaultReturnValue()) {
			if(index >= 0)
				removeIndex(index);
		}
		else if(index < 0) insert(-index-1, key, newValue);
		else values[index] = newValue;
		return newValue;
	}
	
	@Override
	public void mergeAllShort(Long2ShortMap m, ShortShortUnaryOperator mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		for(Long2ShortMap.Entry entry : Long2ShortMaps.fastIterable(m)) {
			long key = entry.getLongKey();
			int index = findIndex(key);
			short newValue = index < 0 || values[index] == getDefaultReturnValue() ? entry.getShortValue() : mappingFunction.applyAsShort(values[index], entry.getShortValue());
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
		Arrays.fill(keys, 0L);
		Arrays.fill(values, (short)0);
	}
	
	@Override
	public boolean trim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex || this.size > Math.min((int)Math.ceil(request * loadFactor), request - 1)) return false;
		try {
			rehash(request);
		}
		catch(OutOfMemoryError noMemory) { return false; }
		return true;
	}
	
	@Override
	public void clearAndTrim(int size) {
		int request = Math.max(minCapacity, HashUtil.nextPowerOfTwo((int)Math.ceil(size / loadFactor)));
		if(request >= nullIndex) {
			clear();
			return;
		}
		nullIndex = request;
		mask = request-1;
		maxFill = Math.min((int)Math.ceil(nullIndex * loadFactor), nullIndex - 1);
		keys = new long[request + 1];
		values = new short[request + 1];
		this.size = 0;
		containsNull = false;
	}
	
	protected int findIndex(long key) {
		if(strategy.equals(key, 0L)) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(strategy.hashCode(key)) & mask;
		long current = keys[pos];
		if(!strategy.equals(current, 0L)) {
			if(strategy.equals(current, key)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), 0L))
				if(strategy.equals(current, key)) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Long key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		long keyType = ((Long)key).longValue();
		if(strategy.equals(keyType, 0L)) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(strategy.hashCode(keyType)) & mask;
		long current = keys[pos];
		if(!strategy.equals(current, 0L)) {
			if(strategy.equals(current, keyType)) return pos;
			while(!strategy.equals((current = keys[pos = (++pos & mask)]), 0L))
				if(strategy.equals(current, keyType)) return pos;
		}
		return -(pos + 1);
	}
	
	protected short removeIndex(int pos) {
		if(pos == nullIndex) return containsNull ? removeNullIndex() : getDefaultReturnValue();
		short value = values[pos];
		keys[pos] = 0L;
		values[pos] = (short)0;
		size--;
		onNodeRemoved(pos);
		shiftKeys(pos);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected short removeNullIndex() {
		short value = values[nullIndex];
		containsNull = false;
		keys[nullIndex] = 0L;
		values[nullIndex] = (short)0;
		size--;
		onNodeRemoved(nullIndex);
		if(nullIndex > minCapacity && size < maxFill / 4 && nullIndex > HashUtil.DEFAULT_MIN_CAPACITY) rehash(nullIndex / 2);
		return value;
	}
	
	protected void insert(int slot, long key, short value) {
		if(slot == nullIndex) containsNull = true;
		keys[slot] = key;
		values[slot] = value;
		onNodeAdded(slot);
		if(size++ >= maxFill) rehash(HashUtil.arraySize(size+1, loadFactor));
	}
	
	protected void rehash(int newSize) {
		int newMask = newSize - 1;
		long[] newKeys = new long[newSize + 1];
		short[] newValues = new short[newSize + 1];
		for(int i = nullIndex, pos = 0, j = (size - (containsNull ? 1 : 0));j-- != 0;) {
			while(true) {
				if(--i < 0) throw new ConcurrentModificationException("Map was modified during rehash");
				if(!strategy.equals(keys[i], 0L)) break;
			}
			if(!strategy.equals(newKeys[pos = HashUtil.mix(strategy.hashCode(keys[i])) & newMask], 0L))
				while(!strategy.equals(newKeys[pos = (++pos & newMask)], 0L));
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
		long current;
		while(true) {
			startPos = ((last = startPos) + 1) & mask;
			while(true){
				if(strategy.equals((current = keys[startPos]), 0L)) {
					keys[last] = 0L;
					values[last] = (short)0;
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
	
	protected class MapEntry implements Long2ShortMap.Entry, Map.Entry<Long, Short> {
		public int index = -1;
		
		public MapEntry() {}
		public MapEntry(int index) {
			this.index = index;
		}

		@Override
		public long getLongKey() {
			return keys[index];
		}

		@Override
		public short getShortValue() {
			return values[index];
		}

		@Override
		public short setValue(short value) {
			short oldValue = values[index];
			values[index] = value;
			return oldValue;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Long2ShortMap.Entry) {
					Long2ShortMap.Entry entry = (Long2ShortMap.Entry)obj;
					return keys[index] == entry.getLongKey() && values[index] == entry.getShortValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				Object value = entry.getValue();
				return key instanceof Long && value instanceof Short && keys[index] == ((Long)key).longValue() && values[index] == ((Short)value).shortValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return strategy.hashCode(keys[index]) ^ Short.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Long.toString(keys[index]) + "=" + Short.toString(values[index]);
		}
	}
	
	private final class MapEntrySet extends AbstractObjectSet<Long2ShortMap.Entry> implements Long2ShortMap.FastEntrySet {
		@Override
		public ObjectIterator<Long2ShortMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectIterator<Long2ShortMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public void forEach(Consumer<? super Long2ShortMap.Entry> action) {
			if(containsNull) action.accept(new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], 0L)) action.accept(new BasicEntry(keys[i], values[i]));
		}
		
		@Override
		public void fastForEach(Consumer<? super Long2ShortMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				action.accept(entry);
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) {
					entry.set(keys[i], values[i]);
					action.accept(entry);
				}
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Long2ShortMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) action.accept(input, new BasicEntry(keys[i], values[i]));
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Long2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return true;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Long2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Long2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(!filter.getBoolean(entry)) return false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) {
					entry.set(keys[i], values[i]);
					if(!filter.getBoolean(entry)) return false;
				}
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Long2ShortMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			if(containsNull) state = operator.apply(state, new BasicEntry(keys[nullIndex], values[nullIndex]));
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0L)) continue;
				state = operator.apply(state, new BasicEntry(keys[i], values[i]));
			}
			return state;
		}
		
		@Override
		public Long2ShortMap.Entry reduce(ObjectObjectUnaryOperator<Long2ShortMap.Entry, Long2ShortMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Long2ShortMap.Entry state = null;
			boolean empty = true;
			if(containsNull) {
				state = new BasicEntry(keys[nullIndex], values[nullIndex]);
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0L)) continue;
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
		public Long2ShortMap.Entry findFirst(Object2BooleanFunction<Long2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) return entry;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) return entry;
				}
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Long2ShortMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
			int result = 0;
			if(containsNull) {
				entry.set(keys[nullIndex], values[nullIndex]);
				if(filter.getBoolean(entry)) result++;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) {
					entry.set(keys[i], values[i]);
					if(filter.getBoolean(entry)) result++;
				}
			}
			return result;
		}
		
		@Override
		public int size() {
			return Long2ShortOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Long2ShortOpenCustomHashMap.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Long2ShortMap.Entry) {
					Long2ShortMap.Entry entry = (Long2ShortMap.Entry)o;
					int index = Long2ShortOpenCustomHashMap.this.findIndex(entry.getLongKey());
					if(index >= 0) return entry.getShortValue() == Long2ShortOpenCustomHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					if(!(entry.getKey() instanceof Long)) return false;
					int index = Long2ShortOpenCustomHashMap.this.findIndex((Long)entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Short.valueOf(Long2ShortOpenCustomHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		public boolean remove(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Long2ShortMap.Entry) {
					Long2ShortMap.Entry entry = (Long2ShortMap.Entry)o;
					return Long2ShortOpenCustomHashMap.this.remove(entry.getLongKey(), entry.getShortValue());
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
				return Long2ShortOpenCustomHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}
	}
	
	private final class KeySet extends AbstractLongSet {
		@Override
		public boolean contains(long e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(long o) {
			int oldSize = size;
			Long2ShortOpenCustomHashMap.this.remove(o);
			return size != oldSize;
		}
		
		@Override
		public boolean add(long o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public LongIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return Long2ShortOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Long2ShortOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(LongConsumer action) {
			Objects.requireNonNull(action);
			if(containsNull) action.accept(keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], 0L)) action.accept(keys[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) action.accept(input, keys[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(keys[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(keys[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(keys[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && !filter.get(keys[i])) return false;
			}
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			if(containsNull) state = operator.applyAsLong(state, keys[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0L)) continue;
				state = operator.applyAsLong(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			if(containsNull) {
				state = keys[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0L)) continue;
				if(empty) {
					empty = false;
					state = keys[i];
					continue;
				}
				state = operator.applyAsLong(state, keys[i]);
			}
			return state;
		}
		
		@Override
		public long findFirst(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0L;
			if(containsNull && filter.get(keys[nullIndex])) return keys[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(keys[i])) return keys[i];
			}
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(keys[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(keys[i])) result++;
			}
			return result;
		}
	}
	
	private class Values extends AbstractShortCollection {
		@Override
		public boolean contains(short e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(short o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ShortIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return Long2ShortOpenCustomHashMap.this.size();
		}
		
		@Override
		public void clear() {
			Long2ShortOpenCustomHashMap.this.clear();
		}
		
		@Override
		public void forEach(ShortConsumer action) {
			if(containsNull) action.accept(values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--)
				if(!strategy.equals(keys[i], 0L)) action.accept(values[i]);
		}
		
		@Override
		public <E> void forEach(E input, ObjectShortConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			if(containsNull) action.accept(input, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L)) action.accept(input, values[i]);
			}
		}
		
		@Override
		public boolean matchesAny(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			if(containsNull && filter.get(values[nullIndex])) return true;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(values[i])) return true;
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			if(containsNull && !filter.get(values[nullIndex])) return false;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && !filter.get(values[i])) return false;
			}
			return true;
		}
		
		@Override
		public short reduce(short identity, ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = identity;
			if(containsNull) state = operator.applyAsShort(state, values[nullIndex]);
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0L)) continue;
				state = operator.applyAsShort(state, values[i]);
			}
			return state;
		}
		
		@Override
		public short reduce(ShortShortUnaryOperator operator) {
			Objects.requireNonNull(operator);
			short state = (short)0;
			boolean empty = true;
			if(containsNull) {
				state = values[nullIndex];
				empty = false;
			}
			for(int i = nullIndex-1;i>=0;i--) {
				if(strategy.equals(keys[i], 0L)) continue;
				if(empty) {
					empty = false;
					state = values[i];
					continue;
				}
				state = operator.applyAsShort(state, values[i]);
			}
			return state;
		}
		
		@Override
		public short findFirst(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return (short)0;
			if(containsNull && filter.get(values[nullIndex])) return values[nullIndex];
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(values[i])) return values[i];
			}
			return (short)0;
		}
		
		@Override
		public int count(Short2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			if(containsNull && filter.get(values[nullIndex])) result++;
			for(int i = nullIndex-1;i>=0;i--) {
				if(!strategy.equals(keys[i], 0L) && filter.get(values[i])) result++;
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectIterator<Long2ShortMap.Entry> {
		MapEntry entry = new MapEntry();
		@Override
		public Long2ShortMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
	}
	
	private class EntryIterator extends MapIterator implements ObjectIterator<Long2ShortMap.Entry> {
		MapEntry entry;
		@Override
		public Long2ShortMap.Entry next() {
			return entry = new MapEntry(nextEntry());
		}
		
		@Override
		public void remove() {
			super.remove();
			entry.index = -1;
		}
	}
	
	private class KeyIterator extends MapIterator implements LongIterator {
		@Override
		public long nextLong() {
			return keys[nextEntry()];
		}
	}
	
	private class ValueIterator extends MapIterator implements ShortIterator {
		@Override
		public short nextShort() {
			return values[nextEntry()];
		}
	}
	
	private class MapIterator {
		int pos = nullIndex;
		int returnedPos = -1;
		int lastReturned = -1;
		int nextIndex = Integer.MIN_VALUE;
		boolean returnNull = containsNull;
		LongList wrapped = null;
		
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
						if(!strategy.equals(keys[pos], 0L)){
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
				int value = findIndex(wrapped.getLong(nextIndex));
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
				keys[nullIndex] = 0L;
				values[nullIndex] = (short)0;
			}
			else if(returnedPos >= 0) shiftKeys(returnedPos);
			else {
				Long2ShortOpenCustomHashMap.this.remove(wrapped.getLong(-returnedPos - 1));
				lastReturned = -1;
				return;
			}
			size--;
			lastReturned = -1;
		}
		
		private void shiftKeys(int startPos) {
			int slot, last;
			long current;
			while(true) {
				startPos = ((last = startPos) + 1) & mask;
				while(true){
					if(strategy.equals((current = keys[startPos]), 0L)) {
						keys[last] = 0L;
						values[last] = (short)0;
						return;
					}
					slot = HashUtil.mix(strategy.hashCode(current)) & mask;
					if(last <= startPos ? (last >= slot || slot > startPos) : (last >= slot && slot > startPos)) break;
					startPos = ++startPos & mask;
				}
				if(startPos < last) {
					if(wrapped == null) wrapped = new LongArrayList(2);
					wrapped.add(keys[startPos]);
				}
				keys[last] = current;
				values[last] = values[startPos];
			}
		}
	}
}