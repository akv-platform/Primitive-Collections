package speiger.src.collections.longs.maps.impl.immutable;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.BiFunction;

import speiger.src.collections.longs.collections.LongBidirectionalIterator;
import speiger.src.collections.longs.functions.LongConsumer;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.longs.functions.consumer.LongBooleanConsumer;
import speiger.src.collections.longs.functions.function.Long2BooleanFunction;
import speiger.src.collections.longs.functions.function.LongBooleanUnaryOperator;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.lists.LongListIterator;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanOrderedMap;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2BooleanMap;
import speiger.src.collections.longs.maps.interfaces.Long2BooleanMap;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.utils.maps.Long2BooleanMaps;
import speiger.src.collections.longs.utils.LongArrays;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.consumer.ObjectBooleanConsumer;
import speiger.src.collections.booleans.functions.function.Boolean2BooleanFunction;
import speiger.src.collections.longs.sets.AbstractLongSet;
import speiger.src.collections.booleans.collections.AbstractBooleanCollection;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanIterator;
import speiger.src.collections.booleans.functions.BooleanSupplier;
import speiger.src.collections.booleans.functions.function.BooleanBooleanUnaryOperator;
import speiger.src.collections.booleans.functions.BooleanConsumer;
import speiger.src.collections.booleans.lists.BooleanListIterator;
import speiger.src.collections.booleans.utils.BooleanArrays;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.lists.ObjectListIterator;
import speiger.src.collections.objects.sets.AbstractObjectSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Custom implementation of the HashMap
 * Instead of using Wrapper Object Arrays for storing keys and values there is dedicated arrays for storing keys and values.
 * Extra to that there is a couple quality of life functions provided
 */
public class ImmutableLong2BooleanOpenHashMap extends AbstractLong2BooleanMap implements Long2BooleanOrderedMap
{
	/** The Backing keys array */
	protected transient long[] keys;
	/** The Backing values array */
	protected transient boolean[] values;
	/** The Backing array for links between nodes. Left 32 Bits => Previous Entry, Right 32 Bits => Next Entry */
	protected transient long[] links;
	/** If a null value is present */
	protected transient boolean containsNull;
	/** Index of the Null Value */
	protected transient int nullIndex;
	/** Max Index that is allowed to be searched through nullIndex - 1 */
	protected transient int mask;
	/** The First Index in the Map */
	protected int firstIndex = -1;
	/** The Last Index in the Map */
	protected int lastIndex = -1;
	/** EntrySet cache */
	protected transient FastOrderedSet entrySet;
	/** KeySet cache */
	protected transient LongOrderedSet keySet;
	/** Values cache */
	protected transient BooleanCollection valuesC;
	
	/** Amount of Elements stored in the HashMap */
	protected int size;
	
	/**
	 * Helper constructor for copying the Map
	 */
	protected ImmutableLong2BooleanOpenHashMap() {}
	
	/**
	 * Helper constructor that allow to create a map from boxed values (it will unbox them)
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableLong2BooleanOpenHashMap(Long[] keys, Boolean[] values) {
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
	public ImmutableLong2BooleanOpenHashMap(Long[] keys, Boolean[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(LongArrays.unwrap(keys), BooleanArrays.unwrap(values), 0, keys.length, loadFactor);
	}
	
	/**
	 * Helper constructor that allow to create a map from unboxed values
	 * @param keys the keys that should be put into the map
	 * @param values the values that should be put into the map.
	 * @throws IllegalStateException if the keys and values do not match in lenght
	 */
	public ImmutableLong2BooleanOpenHashMap(long[] keys, boolean[] values) {
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
	public ImmutableLong2BooleanOpenHashMap(long[] keys, boolean[] values, float loadFactor) {
		if(keys.length != values.length) throw new IllegalStateException("Input Arrays are not equal size");
		init(keys, values, 0, keys.length, loadFactor);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 */
	public ImmutableLong2BooleanOpenHashMap(Map<? extends Long, ? extends Boolean> map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Helper constructor that allows to create a Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
	 */
	public ImmutableLong2BooleanOpenHashMap(Map<? extends Long, ? extends Boolean> map, float loadFactor) {
		long[] keys = new long[map.size()];
		boolean[] values = new boolean[keys.length];
		int index = 0;
		for(Map.Entry<? extends Long, ? extends Boolean> entry : map.entrySet()) {
			keys[index] = entry.getKey().longValue();
			values[index] = entry.getValue().booleanValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
 	 */
	public ImmutableLong2BooleanOpenHashMap(Long2BooleanMap map) {
		this(map, HashUtil.DEFAULT_LOAD_FACTOR);
	}
	
	/**
	 * A Type Specific Helper function that allows to create a new Map with exactly the same values as the provided map.
	 * @param map the values that should be present in the map
	 * @param loadFactor the percentage of how full the backing array can be before they resize
	 * @throws IllegalStateException if the loadfactor is either below/equal to 0 or above/equal to 1
 	 */
	public ImmutableLong2BooleanOpenHashMap(Long2BooleanMap map, float loadFactor) {
		long[] keys = new long[map.size()];
		boolean[] values = new boolean[keys.length];
		int index = 0;
		for(Long2BooleanMap.Entry entry : Long2BooleanMaps.fastIterable(map)) {
			keys[index] = entry.getLongKey();
			values[index] = entry.getBooleanValue();
			index++;
		}
		init(keys, values, 0, index, loadFactor);
	}
	
	protected void init(long[] a, boolean[] b, int offset, int length, float loadFactor) {
		SanityChecks.checkArrayCapacity(a.length, offset, length);
		int newSize = HashUtil.arraySize(length+1, loadFactor);
		int newMask = newSize - 1;
		long[] newKeys = new long[newSize + 1];
		boolean[] newValues = new boolean[newSize + 1];
		long[] newLinks = new long[newSize + 1];
		int prev = -1;
		for(int i = offset,m=offset+length;i<m;i++)
		{
			long o = a[i];
			if(o == 0) {
				if(!containsNull) {
					size++;
					if(prev != -1) {
						newLinks[prev] ^= ((newLinks[prev] ^ (newSize & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
						newLinks[newSize] ^= ((newLinks[newSize] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
						prev = newSize;
					}
					else {
						prev = firstIndex = newSize;
						newLinks[newSize] = -1L;
					}
				}
				containsNull = true;
				newValues[newSize] = b[i];
				continue;
			}
			boolean found = true;
			int pos = HashUtil.mix(Long.hashCode(o)) & newMask;
			long current = newKeys[pos];
			if(current != 0) {
				if(current == o) {
					newValues[pos] = b[i];
					continue;
				}
				while((current = newKeys[pos = (++pos & newMask)]) != 0) {
					if(current == o) {
						found = false;
						newValues[pos] = b[i];
						break;
					}
				}
			}
			if(found) {
				size++;
				newKeys[pos] = o;
				newValues[pos] = b[i];
				if(prev != -1) {
					newLinks[prev] ^= ((newLinks[prev] ^ (pos & 0xFFFFFFFFL)) & 0xFFFFFFFFL);
					newLinks[pos] ^= ((newLinks[pos] ^ ((prev & 0xFFFFFFFFL) << 32)) & 0xFFFFFFFF00000000L);
					prev = pos;
				}
				else {
					prev = firstIndex = pos;
					newLinks[pos] = -1L;
				}
			}
		}
		nullIndex = newSize;
		mask = newMask;
		keys = newKeys;
		values = newValues;
		links = newLinks;
		lastIndex = prev;
		if(prev != -1) newLinks[prev] |= 0xFFFFFFFFL;
	}
	
	@Override
	public boolean put(long key, boolean value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean putIfAbsent(long key, boolean value) { throw new UnsupportedOperationException(); }	
	@Override
	public boolean putAndMoveToFirst(long key, boolean value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean putAndMoveToLast(long key, boolean value) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
	@Override
	public boolean getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean containsKey(long key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	@Deprecated
	public boolean containsKey(Object key) {
		return findIndex(key) >= 0;
	}
	
	@Override
	public boolean containsValue(boolean value) {
		int index = firstIndex;
		while(index != -1) {
			if(values[index] == value) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	@Deprecated
	public boolean containsValue(Object value) {
		int index = firstIndex;
		while(index != -1) {
			if((value == null && values[index] == getDefaultReturnValue()) || Objects.equals(value, Boolean.valueOf(values[index]))) return true;
			index = (int)links[index];
		}
		return false;
	}
	
	@Override
	public boolean remove(long key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean removeOrDefault(long key, boolean defaultValue) { throw new UnsupportedOperationException(); }
	
	@Override
	@Deprecated
	public Boolean remove(Object key) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(long key, boolean value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean remove(Object key, Object value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean get(long key) {
		int slot = findIndex(key);
		return slot < 0 ? getDefaultReturnValue() : values[slot];
	}
	
	@Override
	public Boolean get(Object key) {
		int slot = findIndex(key);
		return Boolean.valueOf(slot < 0 ? getDefaultReturnValue() : values[slot]);
	}
	
	@Override
	public boolean getOrDefault(long key, boolean defaultValue) {
		int slot = findIndex(key);
		return slot < 0 ? defaultValue : values[slot];
	}
	
	@Override
	public long firstLongKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[firstIndex];
	}
	
	@Override
	public long pollFirstLongKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public long lastLongKey() {
		if(size == 0) throw new NoSuchElementException();
		return keys[lastIndex];
	}
	
	@Override
	public long pollLastLongKey() { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean firstBooleanValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[firstIndex];
	}
	
	@Override
	public boolean lastBooleanValue() {
		if(size == 0) throw new NoSuchElementException();
		return values[lastIndex];
	}	

	@Override
	public ObjectOrderedSet<Long2BooleanMap.Entry> long2BooleanEntrySet() {
		if(entrySet == null) entrySet = new MapEntrySet();
		return entrySet;
	}
	
	@Override
	public LongOrderedSet keySet() {
		if(keySet == null) keySet = new KeySet();
		return keySet;
	}
	
	@Override
	public BooleanCollection values() {
		if(valuesC == null) valuesC = new Values();
		return valuesC;
	}
	
	@Override
	public ImmutableLong2BooleanOpenHashMap copy() {
		ImmutableLong2BooleanOpenHashMap map = new ImmutableLong2BooleanOpenHashMap();
		map.mask = mask;
		map.nullIndex = nullIndex;
		map.containsNull = containsNull;
		map.size = size;
		map.keys = Arrays.copyOf(keys, keys.length);
		map.values = Arrays.copyOf(values, values.length);
		map.links = Arrays.copyOf(links, links.length);
		map.firstIndex = firstIndex;
		map.lastIndex = lastIndex;
		return map;
	}
	
	@Override
	public void forEach(LongBooleanConsumer action) {
		int index = firstIndex;
		while(index != -1){
			action.accept(keys[index], values[index]);
			index = (int)links[index];
		}
	}
	
	@Override
	public boolean replace(long key, boolean oldValue, boolean newValue) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean replace(long key, boolean value) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean computeBoolean(long key, LongBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean computeBooleanIfAbsent(long key, Long2BooleanFunction mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean supplyBooleanIfAbsent(long key, BooleanSupplier valueProvider) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean computeBooleanIfPresent(long key, LongBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public boolean mergeBoolean(long key, boolean value, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public void mergeAllBoolean(Long2BooleanMap m, BooleanBooleanUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
	
	@Override
	public int size() { return size; }
	
	@Override
	public void clear() { throw new UnsupportedOperationException(); }
	
	protected int findIndex(long key) {
		if(key == 0) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(Long.hashCode(key)) & mask;
		long current = keys[pos];
		if(current != 0) {
			if(current == key) return pos;
			while((current = keys[pos = (++pos & mask)]) != 0)
				if(current == key) return pos;
		}
		return -(pos + 1);
	}
	
	protected int findIndex(Object key) {
		if(key == null) return containsNull ? nullIndex : -(nullIndex + 1);
		int pos = HashUtil.mix(key.hashCode()) & mask;
		long current = keys[pos];
		if(current != 0) {
			if(Objects.equals(key, Long.valueOf(current))) return pos;
			while((current = keys[pos = (++pos & mask)]) != 0)
				if(Objects.equals(key, Long.valueOf(current))) return pos;
		}
		return -(pos + 1);
	}
	
	protected class MapEntry implements Long2BooleanMap.Entry, Map.Entry<Long, Boolean> {
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
		public boolean getBooleanValue() {
			return values[index];
		}

		@Override
		public boolean setValue(boolean value) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Map.Entry) {
				if(obj instanceof Long2BooleanMap.Entry) {
					Long2BooleanMap.Entry entry = (Long2BooleanMap.Entry)obj;
					return keys[index] == entry.getLongKey() && values[index] == entry.getBooleanValue();
				}
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
				Object key = entry.getKey();
				if(!(key instanceof Long)) return false;
				Object value = entry.getValue();
				return key instanceof Long && value instanceof Boolean && keys[index] == ((Long)key).longValue() && values[index] == ((Boolean)value).booleanValue();
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return Long.hashCode(keys[index]) ^ Boolean.hashCode(values[index]);
		}
		
		@Override
		public String toString() {
			return Long.toString(keys[index]) + "=" + Boolean.toString(values[index]);
		}
	}
	
	private class MapEntrySet extends AbstractObjectSet<Long2BooleanMap.Entry> implements Long2BooleanOrderedMap.FastOrderedSet {
		@Override
		public boolean addAndMoveToFirst(Long2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Long2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToFirst(Long2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean moveToLast(Long2BooleanMap.Entry o) { throw new UnsupportedOperationException(); }
		
		@Override
		public Long2BooleanMap.Entry first() {
			return new BasicEntry(firstLongKey(), firstBooleanValue());
		}
		
		@Override
		public Long2BooleanMap.Entry last() {
			return new BasicEntry(lastLongKey(), lastBooleanValue());
		}
		
		@Override
		public Long2BooleanMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		
		@Override
		public Long2BooleanMap.Entry pollLast() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectBidirectionalIterator<Long2BooleanMap.Entry> iterator() {
			return new EntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Long2BooleanMap.Entry> iterator(Long2BooleanMap.Entry fromElement) {
			return new EntryIterator(fromElement.getLongKey());
		}
		
		@Override
		public ObjectBidirectionalIterator<Long2BooleanMap.Entry> fastIterator() {
			return new FastEntryIterator();
		}
		
		@Override
		public ObjectBidirectionalIterator<Long2BooleanMap.Entry> fastIterator(long fromElement) {
			return new FastEntryIterator(fromElement);
		}
		
		@Override
		public MapEntrySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(Consumer<? super Long2BooleanMap.Entry> action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public void fastForEach(Consumer<? super Long2BooleanMap.Entry> action) {
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1){
				entry.set(keys[index], values[index]);
				action.accept(entry);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectObjectConsumer<E, Long2BooleanMap.Entry> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1) {
				action.accept(input, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Object2BooleanFunction<Long2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Object2BooleanFunction<Long2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Object2BooleanFunction<Long2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(!filter.getBoolean(entry)) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public <E> E reduce(E identity, BiFunction<E, Long2BooleanMap.Entry, E> operator) {
			Objects.requireNonNull(operator);
			E state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.apply(state, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Long2BooleanMap.Entry reduce(ObjectObjectUnaryOperator<Long2BooleanMap.Entry, Long2BooleanMap.Entry> operator) {
			Objects.requireNonNull(operator);
			Long2BooleanMap.Entry state = null;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = new BasicEntry(keys[index], values[index]);
					index = (int)links[index];
					continue;
				}
				state = operator.apply(state, new BasicEntry(keys[index], values[index]));
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public Long2BooleanMap.Entry findFirst(Object2BooleanFunction<Long2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return null;
			BasicEntry entry = new BasicEntry();
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) return entry;
				index = (int)links[index];
			}
			return null;
		}
		
		@Override
		public int count(Object2BooleanFunction<Long2BooleanMap.Entry> filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			BasicEntry entry = new BasicEntry();
			int result = 0;
			int index = firstIndex;
			while(index != -1) {
				entry.set(keys[index], values[index]);
				if(filter.getBoolean(entry)) result++;
				index = (int)links[index];
			}
			return result;
		}
		
		@Override
		@Deprecated
		public boolean contains(Object o) {
			if(o instanceof Map.Entry) {
				if(o instanceof Long2BooleanMap.Entry) {
					Long2BooleanMap.Entry entry = (Long2BooleanMap.Entry)o;
					int index = ImmutableLong2BooleanOpenHashMap.this.findIndex(entry.getLongKey());
					if(index >= 0) return entry.getBooleanValue() == ImmutableLong2BooleanOpenHashMap.this.values[index];
				}
				else {
					Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
					int index = ImmutableLong2BooleanOpenHashMap.this.findIndex(entry.getKey());
					if(index >= 0) return Objects.equals(entry.getValue(), Boolean.valueOf(ImmutableLong2BooleanOpenHashMap.this.values[index]));
				}
			}
			return false;
		}
		
		@Override
		@Deprecated
		public boolean remove(Object o) { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableLong2BooleanOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
	}
	
	private final class KeySet extends AbstractLongSet implements LongOrderedSet {
		@Override
		public boolean contains(long e) {
			return containsKey(e);
		}
		
		@Override
		public boolean remove(long o) { throw new UnsupportedOperationException(); }
		
		@Override
		public boolean add(long o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAndMoveToFirst(long o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean addAndMoveToLast(long o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToFirst(long o) { throw new UnsupportedOperationException(); }

		@Override
		public boolean moveToLast(long o) { throw new UnsupportedOperationException(); }
		
		@Override
		public LongListIterator iterator() {
			return new KeyIterator();
		}
		
		@Override
		public LongBidirectionalIterator iterator(long fromElement) {
			return new KeyIterator(fromElement);
		}
		
		@Override
		public KeySet copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public int size() {
			return ImmutableLong2BooleanOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public long firstLong() {
			return firstLongKey();
		}
		
		@Override
		public long pollFirstLong() { throw new UnsupportedOperationException(); }

		@Override
		public long lastLong() {
			return lastLongKey();
		}

		@Override
		public long pollLastLong() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(LongConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectLongConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, keys[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.get(keys[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public long reduce(long identity, LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsLong(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public long reduce(LongLongUnaryOperator operator) {
			Objects.requireNonNull(operator);
			long state = 0L;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = keys[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsLong(state, keys[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public long findFirst(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0L;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(keys[index])) return keys[index];
				index = (int)links[index];
			}
			return 0L;
		}
		
		@Override
		public int count(Long2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int index = firstIndex;
			int result = 0;
			while(index != -1){
				if(filter.get(keys[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class Values extends AbstractBooleanCollection {
		@Override
		public boolean contains(boolean e) {
			return containsValue(e);
		}
		
		@Override
		public boolean add(boolean o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public BooleanIterator iterator() {
			return new ValueIterator();
		}
		
		@Override
		public int size() {
			return ImmutableLong2BooleanOpenHashMap.this.size();
		}
		
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public void forEach(BooleanConsumer action) {
			int index = firstIndex;
			while(index != -1){
				action.accept(values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public <E> void forEach(E input, ObjectBooleanConsumer<E> action) {
			Objects.requireNonNull(action);
			if(size() <= 0) return;
			int index = firstIndex;
			while(index != -1){
				action.accept(input, values[index]);
				index = (int)links[index];
			}
		}
		
		@Override
		public boolean matchesAny(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return true;
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public boolean matchesNone(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean matchesAll(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return true;
			int index = firstIndex;
			while(index != -1){
				if(!filter.get(values[index])) return false;
				index = (int)links[index];
			}
			return true;
		}
		
		@Override
		public boolean reduce(boolean identity, BooleanBooleanUnaryOperator operator) {
			Objects.requireNonNull(operator);
			boolean state = identity;
			int index = firstIndex;
			while(index != -1) {
				state = operator.applyAsBoolean(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public boolean reduce(BooleanBooleanUnaryOperator operator) {
			Objects.requireNonNull(operator);
			boolean state = false;
			boolean empty = true;
			int index = firstIndex;
			while(index != -1) {
				if(empty) {
					empty = false;
					state = values[index];
					index = (int)links[index];
					continue;
				}
				state = operator.applyAsBoolean(state, values[index]);
				index = (int)links[index];
			}
			return state;
		}
		
		@Override
		public boolean findFirst(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return false;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) return values[index];
				index = (int)links[index];
			}
			return false;
		}
		
		@Override
		public int count(Boolean2BooleanFunction filter) {
			Objects.requireNonNull(filter);
			if(size() <= 0) return 0;
			int result = 0;
			int index = firstIndex;
			while(index != -1){
				if(filter.get(values[index])) result++;
				index = (int)links[index];
			}
			return result;
		}
	}
	
	private class FastEntryIterator extends MapIterator implements ObjectListIterator<Long2BooleanMap.Entry> {
		MapEntry entry = new MapEntry();
		
		public FastEntryIterator() {}
		public FastEntryIterator(long from) {
			super(from);
		}
		
		@Override
		public Long2BooleanMap.Entry next() {
			entry.index = nextEntry();
			return entry;
		}
		
		@Override
		public Long2BooleanMap.Entry previous() {
			entry.index = previousEntry();
			return entry;
		}
		
		@Override
		public void set(Long2BooleanMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Long2BooleanMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class EntryIterator extends MapIterator implements ObjectListIterator<Long2BooleanMap.Entry> {
		
		public EntryIterator() {}
		public EntryIterator(long from) {
			super(from);
		}
		
		@Override
		public Long2BooleanMap.Entry next() {
			return new MapEntry(nextEntry());
		}
		
		@Override
		public Long2BooleanMap.Entry previous() {
			return new MapEntry(previousEntry());
		}
		
		@Override
		public void remove() { throw new UnsupportedOperationException(); }

		@Override
		public void set(Long2BooleanMap.Entry entry) { throw new UnsupportedOperationException(); }

		@Override
		public void add(Long2BooleanMap.Entry entry) { throw new UnsupportedOperationException(); }
	}
	
	private class KeyIterator extends MapIterator implements LongListIterator {
		
		public KeyIterator() {}
		public KeyIterator(long from) {
			super(from);
		}
		
		@Override
		public long previousLong() {
			return keys[previousEntry()];
		}

		@Override
		public long nextLong() {
			return keys[nextEntry()];
		}
		
		@Override
		public void set(long e) { throw new UnsupportedOperationException(); }
		@Override
		public void add(long e) { throw new UnsupportedOperationException(); }
	}
	
	private class ValueIterator extends MapIterator implements BooleanListIterator {
		public ValueIterator() {}
		
		@Override
		public boolean previousBoolean() {
			return values[previousEntry()];
		}

		@Override
		public boolean nextBoolean() {
			return values[nextEntry()];
		}

		@Override
		public void set(boolean e) { throw new UnsupportedOperationException(); }

		@Override
		public void add(boolean e) { throw new UnsupportedOperationException(); }
		
	}
	
	private class MapIterator {
		int previous = -1;
		int next = -1;
		int current = -1;
		int index = 0;
		
		MapIterator() {
			next = firstIndex;
		}
		
		MapIterator(long from) {
			if(from == 0) {
				if(containsNull) {
					next = (int) links[nullIndex];
					previous = nullIndex;
				}
				else throw new NoSuchElementException("The null element is not in the set");
			}
			else if(keys[lastIndex] == from) {
				previous = lastIndex;
				index = size;
			}
			else {
				int pos = HashUtil.mix(Long.hashCode(from)) & mask;
				while(keys[pos] != 0) {
					if(keys[pos] == from) {
						next = (int)links[pos];
						previous = pos;
						break;
					}
					pos = ++pos & mask;
				}
				if(previous == -1 && next == -1)
					throw new NoSuchElementException("The element was not found");
			}
		}
		
		public boolean hasNext() {
			return next != -1;
		}

		public boolean hasPrevious() {
			return previous != -1;
		}
		
		public int nextIndex() {
			ensureIndexKnown();
			return index;
		}
		
		public int previousIndex() {
			ensureIndexKnown();
			return index - 1;
		}
		
		public void remove() { throw new UnsupportedOperationException(); }
		
		public int previousEntry() {
			if(!hasPrevious()) throw new NoSuchElementException();
			current = previous;
			previous = (int)(links[current] >> 32);
			next = current;
			if(index >= 0) index--;
			return current;
		}
		
		public int nextEntry() {
			if(!hasNext()) throw new NoSuchElementException();
			current = next;
			next = (int)(links[current]);
			previous = current;
			if(index >= 0) index++;
			return current;
		}
		
		private void ensureIndexKnown() {
			if(index == -1) {
				if(previous == -1) {
					index = 0;
				}
				else if(next == -1) {
					index = size;
				}
				else {
					index = 1;
					for(int pos = firstIndex;pos != previous;pos = (int)links[pos], index++);
				}
			}
		}
	}
}