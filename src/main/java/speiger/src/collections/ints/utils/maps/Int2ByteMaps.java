package speiger.src.collections.ints.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.utils.ObjectIterators;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.ints.functions.IntComparator;
import speiger.src.collections.ints.functions.consumer.IntByteConsumer;
import speiger.src.collections.ints.functions.function.Int2ByteFunction;
import speiger.src.collections.ints.functions.function.IntByteUnaryOperator;
import speiger.src.collections.ints.maps.abstracts.AbstractInt2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteNavigableMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteSortedMap;
import speiger.src.collections.ints.maps.interfaces.Int2ByteOrderedMap;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntSortedSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.bytes.collections.ByteCollection;
import speiger.src.collections.bytes.functions.function.ByteByteUnaryOperator;
import speiger.src.collections.bytes.functions.ByteSupplier;
import speiger.src.collections.bytes.utils.ByteCollections;
import speiger.src.collections.bytes.utils.ByteSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Int2ByteMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Int2ByteMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Int2ByteMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Int2ByteMap.Entry> fastIterator(Int2ByteMap map) {
		ObjectSet<Int2ByteMap.Entry> entries = map.int2ByteEntrySet();
		return entries instanceof Int2ByteMap.FastEntrySet ? ((Int2ByteMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Int2ByteMap.Entry> fastIterable(Int2ByteMap map) {
		ObjectSet<Int2ByteMap.Entry> entries = map.int2ByteEntrySet();
		return map instanceof Int2ByteMap.FastEntrySet ? new ObjectIterable<Int2ByteMap.Entry>(){
			@Override
			public ObjectIterator<Int2ByteMap.Entry> iterator() { return ((Int2ByteMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Int2ByteMap.Entry> action) { ((Int2ByteMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Int2ByteMap map, Consumer<Int2ByteMap.Entry> action) {
		ObjectSet<Int2ByteMap.Entry> entries = map.int2ByteEntrySet();
		if(entries instanceof Int2ByteMap.FastEntrySet) ((Int2ByteMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteMap synchronize(Int2ByteMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteMap synchronize(Int2ByteMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteSortedMap synchronize(Int2ByteSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteSortedMap synchronize(Int2ByteSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteOrderedMap synchronize(Int2ByteOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteOrderedMap synchronize(Int2ByteOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteNavigableMap synchronize(Int2ByteNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Int2ByteNavigableMap synchronize(Int2ByteNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Int2ByteMap unmodifiable(Int2ByteMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2ByteOrderedMap unmodifiable(Int2ByteOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Int2ByteSortedMap unmodifiable(Int2ByteSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Int2ByteNavigableMap unmodifiable(Int2ByteNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2ByteMap.Entry unmodifiable(Int2ByteMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Int2ByteMap.Entry unmodifiable(Map.Entry<Integer, Byte> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Int2ByteMap singleton(int key, byte value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractInt2ByteMap {
		final int key;
		final byte value;
		IntSet keySet;
		ByteCollection values;
		ObjectSet<Int2ByteMap.Entry> entrySet;
		
		SingletonMap(int key, byte value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public byte put(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(int key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(int key) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(int key, byte defaultValue) { return Objects.equals(this.key, Integer.valueOf(key)) ? value : defaultValue; }
		@Override
		public byte computeByte(int key, IntByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(int key, Int2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(int key, IntByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(int key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(int key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Int2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public IntSet keySet() { 
			if(keySet == null) keySet = IntSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ByteCollection values() { 
			if(values == null) values = ByteSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Int2ByteMap.Entry> int2ByteEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractInt2ByteMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractInt2ByteMap {
		@Override
		public byte put(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(int key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(int key) { return getDefaultReturnValue(); }
		@Override
		public byte getOrDefault(int key, byte defaultValue) { return defaultValue; }
		@Override
		public byte computeByte(int key, IntByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(int key, Int2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(int key, IntByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(int key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(int key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Int2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public IntSet keySet() { return IntSets.empty(); }
		@Override
		public ByteCollection values() { return ByteCollections.empty(); }
		@Override
		public ObjectSet<Int2ByteMap.Entry> int2ByteEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractInt2ByteMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Integer, Byte> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Int2ByteMap.Entry entry) {
			super(entry.getIntKey(), entry.getByteValue());
		}
		
		@Override
		public void set(int key, byte value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Int2ByteNavigableMap {
		Int2ByteNavigableMap map;
		
		UnmodifyableNavigableMap(Int2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Int2ByteNavigableMap descendingMap() { return Int2ByteMaps.unmodifiable(map.descendingMap()); }
		@Override
		public IntNavigableSet navigableKeySet() { return IntSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public IntNavigableSet keySet() { return IntSets.unmodifiable(map.keySet()); }
		@Override
		public IntNavigableSet descendingKeySet() { return IntSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Int2ByteMap.Entry firstEntry() { return Int2ByteMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Int2ByteMap.Entry lastEntry() { return Int2ByteMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Int2ByteMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2ByteMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Int2ByteNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { return Int2ByteMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Int2ByteNavigableMap headMap(int toKey, boolean inclusive) { return Int2ByteMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Int2ByteNavigableMap tailMap(int fromKey, boolean inclusive) { return Int2ByteMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Int2ByteNavigableMap subMap(int fromKey, int toKey) { return Int2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2ByteNavigableMap headMap(int toKey) { return Int2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2ByteNavigableMap tailMap(int fromKey) { return Int2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(int e) { throw new UnsupportedOperationException(); }
		@Override
		public int getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public int lowerKey(int key) { return map.lowerKey(key); }
		@Override
		public int higherKey(int key) { return map.higherKey(key); }
		@Override
		public int floorKey(int key) { return map.floorKey(key); }
		@Override
		public int ceilingKey(int key) { return map.ceilingKey(key); }
		@Override
		public Int2ByteMap.Entry lowerEntry(int key) { return Int2ByteMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Int2ByteMap.Entry higherEntry(int key) { return Int2ByteMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Int2ByteMap.Entry floorEntry(int key) { return Int2ByteMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Int2ByteMap.Entry ceilingEntry(int key) { return Int2ByteMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Int2ByteNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Int2ByteOrderedMap {
		Int2ByteOrderedMap map;
		
		UnmodifyableOrderedMap(Int2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putAndMoveToLast(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToFirst(int key) { throw new UnsupportedOperationException(); }
		@Override
		public byte getAndMoveToLast(int key) { throw new UnsupportedOperationException(); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Int2ByteOrderedMap copy() { return map.copy(); }
		@Override
		public IntOrderedSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return (IntOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Int2ByteMap.Entry> int2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet(map.int2ByteEntrySet());
			return (ObjectOrderedSet<Int2ByteMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Int2ByteSortedMap {
		Int2ByteSortedMap map;
		
		UnmodifyableSortedMap(Int2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { return map.comparator(); }
		@Override
		public Int2ByteSortedMap subMap(int fromKey, int toKey) { return Int2ByteMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Int2ByteSortedMap headMap(int toKey) { return Int2ByteMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Int2ByteSortedMap tailMap(int fromKey) { return Int2ByteMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public IntSortedSet keySet() { return IntSets.unmodifiable(map.keySet()); }
		@Override
		public int firstIntKey() { return map.firstIntKey(); }
		@Override
		public int pollFirstIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public int lastIntKey() { return map.lastIntKey(); }
		@Override
		public int pollLastIntKey() { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteValue() { return map.firstByteValue(); }
		@Override
		public byte lastByteValue() { return map.lastByteValue(); }
		@Override
		public Int2ByteSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractInt2ByteMap implements Int2ByteMap {
		Int2ByteMap map;
		ByteCollection values;
		IntSet keys;
		ObjectSet<Int2ByteMap.Entry> entrySet;
		
		UnmodifyableMap(Int2ByteMap map) {
			this.map = map;
		}
		
		@Override
		public byte put(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte putIfAbsent(int key, byte value){ throw new UnsupportedOperationException(); }
		@Override
		public byte addTo(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte subFrom(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte remove(int key) { throw new UnsupportedOperationException(); }
		@Override
		public byte removeOrDefault(int key, byte defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(int key, byte value) { throw new UnsupportedOperationException(); }
		@Override
		public byte get(int key) {
			byte type = map.get(key);
			return type == map.getDefaultReturnValue() && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public byte getOrDefault(int key, byte defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public byte computeByte(int key, IntByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfAbsent(int key, Int2ByteFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte computeByteIfPresent(int key, IntByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public byte supplyByteIfAbsent(int key, ByteSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public byte mergeByte(int key, byte value, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllByte(Int2ByteMap m, ByteByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBytes(IntByteUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceBytes(Int2ByteMap m) { throw new UnsupportedOperationException(); }
		@Override
		public Int2ByteMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public IntSet keySet() { 
			if(keys == null) keys = IntSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Int2ByteMap.Entry> int2ByteEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.int2ByteEntrySet());
			return entrySet;
		}
	}
	
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 */
	public static class UnmodifyableOrderedEntrySet extends UnmodifyableEntrySet implements ObjectOrderedSet<Int2ByteMap.Entry>
	{
		ObjectOrderedSet<Int2ByteMap.Entry> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Int2ByteMap.Entry> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Int2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Int2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Int2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Int2ByteMap.Entry o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Int2ByteMap.Entry> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Int2ByteMap.Entry> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Int2ByteMap.Entry> iterator(Int2ByteMap.Entry fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Int2ByteMap.Entry first() { return set.first(); }
		@Override
		public Int2ByteMap.Entry pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Int2ByteMap.Entry last() { return set.last(); }
		@Override
		public Int2ByteMap.Entry pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Int2ByteMap.Entry>
	{
		ObjectSet<Int2ByteMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Int2ByteMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Int2ByteMap.Entry> action) {
			s.forEach(T -> action.accept(Int2ByteMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Int2ByteMap.Entry> iterator() {
			return new ObjectIterator<Int2ByteMap.Entry>() {
				ObjectIterator<Int2ByteMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Int2ByteMap.Entry next() { return Int2ByteMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Int2ByteNavigableMap {
		Int2ByteNavigableMap map;
		
		SynchronizedNavigableMap(Int2ByteNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Int2ByteNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Int2ByteNavigableMap descendingMap() { synchronized(mutex) { return Int2ByteMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public IntNavigableSet navigableKeySet() { synchronized(mutex) { return IntSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public IntNavigableSet descendingKeySet() { synchronized(mutex) { return IntSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public IntNavigableSet keySet() { synchronized(mutex) { return IntSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Int2ByteMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Int2ByteMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Int2ByteMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Int2ByteMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Int2ByteNavigableMap subMap(int fromKey, boolean fromInclusive, int toKey, boolean toInclusive) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Int2ByteNavigableMap headMap(int toKey, boolean inclusive) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Int2ByteNavigableMap tailMap(int fromKey, boolean inclusive) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Int2ByteNavigableMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2ByteNavigableMap headMap(int toKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2ByteNavigableMap tailMap(int fromKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public int lowerKey(int key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public int higherKey(int key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public int floorKey(int key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public int ceilingKey(int key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Int2ByteMap.Entry lowerEntry(int key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Int2ByteMap.Entry higherEntry(int key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Int2ByteMap.Entry floorEntry(int key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Int2ByteMap.Entry ceilingEntry(int key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Int2ByteNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Int2ByteNavigableMap subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ByteNavigableMap headMap(Integer toKey, boolean inclusive) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ByteNavigableMap tailMap(Integer fromKey, boolean inclusive) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Int2ByteNavigableMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ByteNavigableMap headMap(Integer toKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ByteNavigableMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(int e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public int getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(int e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public int getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Integer lowerKey(Integer key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Integer floorKey(Integer key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Integer ceilingKey(Integer key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Integer higherKey(Integer key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Int2ByteMap.Entry lowerEntry(Integer key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Int2ByteMap.Entry floorEntry(Integer key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Int2ByteMap.Entry ceilingEntry(Integer key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Int2ByteMap.Entry higherEntry(Integer key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Int2ByteOrderedMap {
		Int2ByteOrderedMap map;
		
		SynchronizedOrderedMap(Int2ByteOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Int2ByteOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public byte putAndMoveToFirst(int key, byte value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public byte putAndMoveToLast(int key, byte value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(int key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(int key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public byte getAndMoveToFirst(int key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public byte getAndMoveToLast(int key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Int2ByteOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntOrderedSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return (IntOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Int2ByteMap.Entry> int2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2ByteEntrySet(), mutex);
			return (ObjectOrderedSet<Int2ByteMap.Entry>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Int2ByteSortedMap {
		Int2ByteSortedMap map;
		
		SynchronizedSortedMap(Int2ByteSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Int2ByteSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public IntComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Int2ByteSortedMap subMap(int fromKey, int toKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Int2ByteSortedMap headMap(int toKey)  { synchronized(mutex) { return Int2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Int2ByteSortedMap tailMap(int fromKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public IntSortedSet keySet() { synchronized(mutex) { return IntSets.synchronize(map.keySet(), mutex); } }
		@Override
		public int firstIntKey() { synchronized(mutex) { return map.firstIntKey(); } }
		@Override
		public int pollFirstIntKey() { synchronized(mutex) { return map.pollFirstIntKey(); } }
		@Override
		public int lastIntKey() { synchronized(mutex) { return map.lastIntKey(); } }
		@Override
		public int pollLastIntKey() { synchronized(mutex) { return map.pollLastIntKey(); } }
		@Override
		public byte firstByteValue() { synchronized(mutex) { return map.firstByteValue(); } }
		@Override
		public byte lastByteValue() { synchronized(mutex) { return map.lastByteValue(); } }
		@Override
		public Int2ByteSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Integer firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Integer lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Int2ByteSortedMap subMap(Integer fromKey, Integer toKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ByteSortedMap headMap(Integer toKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Int2ByteSortedMap tailMap(Integer fromKey) { synchronized(mutex) { return Int2ByteMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractInt2ByteMap implements Int2ByteMap {
		Int2ByteMap map;
		ByteCollection values;
		IntSet keys;
		ObjectSet<Int2ByteMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Int2ByteMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Int2ByteMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public byte getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractInt2ByteMap setDefaultReturnValue(byte v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public byte put(int key, byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public byte putIfAbsent(int key, byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Int2ByteMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public byte addTo(int key, byte value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public byte subFrom(int key, byte value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Int2ByteMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Int2ByteMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Integer, ? extends Byte> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(int[] keys, byte[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(int key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(byte value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public byte get(int key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public byte remove(int key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public byte removeOrDefault(int key, byte defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(int key, byte value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(int key, byte oldValue, byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public byte replace(int key, byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceBytes(Int2ByteMap m) { synchronized(mutex) { map.replaceBytes(m); } }
		@Override
		public void replaceBytes(IntByteUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceBytes(mappingFunction); } }
		@Override
		public byte computeByte(int key, IntByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByte(key, mappingFunction); } }
		@Override
		public byte computeByteIfAbsent(int key, Int2ByteFunction mappingFunction) { synchronized(mutex) { return map.computeByteIfAbsent(key, mappingFunction); } }
		@Override
		public byte computeByteIfPresent(int key, IntByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeByteIfPresent(key, mappingFunction); } }
		@Override
		public byte supplyByteIfAbsent(int key, ByteSupplier valueProvider) { synchronized(mutex) { return map.supplyByteIfAbsent(key, valueProvider); } }
		@Override
		public byte mergeByte(int key, byte value, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeByte(key, value, mappingFunction); } }
		@Override
		public void mergeAllByte(Int2ByteMap m, ByteByteUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllByte(m, mappingFunction); } }
		@Override
		public byte getOrDefault(int key, byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(IntByteConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Int2ByteMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public IntSet keySet() {
			if(keys == null) keys = IntSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ByteCollection values() {
			if(values == null) values = ByteCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Int2ByteMap.Entry> int2ByteEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.int2ByteEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Byte get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Byte getOrDefault(Object key, Byte defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Byte put(Integer key, Byte value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Byte remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Byte putIfAbsent(Integer key, Byte value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Integer key, Byte oldValue, Byte newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Byte replace(Integer key, Byte value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Byte compute(Integer key, BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfAbsent(Integer key, Function<? super Integer, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte computeIfPresent(Integer key, BiFunction<? super Integer, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Byte merge(Integer key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Integer, ? super Byte> action) { synchronized(mutex) { map.forEach(action); } }
	}
}