package speiger.src.collections.longs.utils.maps;

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
import speiger.src.collections.longs.functions.LongComparator;
import speiger.src.collections.longs.functions.consumer.LongObjectConsumer;
import speiger.src.collections.longs.functions.function.Long2ObjectFunction;
import speiger.src.collections.longs.functions.function.LongObjectUnaryOperator;
import speiger.src.collections.longs.maps.abstracts.AbstractLong2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectNavigableMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectSortedMap;
import speiger.src.collections.longs.maps.interfaces.Long2ObjectOrderedMap;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongSortedSet;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.functions.ObjectSupplier;
import speiger.src.collections.objects.utils.ObjectCollections;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Long2ObjectMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Long2ObjectMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <V> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <V> Long2ObjectMap<V> empty() { 
		return (Long2ObjectMap<V>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <V> ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator(Long2ObjectMap<V> map) {
		ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
		return entries instanceof Long2ObjectMap.FastEntrySet ? ((Long2ObjectMap.FastEntrySet<V>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <V> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <V> ObjectIterable<Long2ObjectMap.Entry<V>> fastIterable(Long2ObjectMap<V> map) {
		ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
		return map instanceof Long2ObjectMap.FastEntrySet ? new ObjectIterable<Long2ObjectMap.Entry<V>>(){
			@Override
			public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() { return ((Long2ObjectMap.FastEntrySet<V>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> action) { ((Long2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <V> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <V> void fastForEach(Long2ObjectMap<V> map, Consumer<Long2ObjectMap.Entry<V>> action) {
		ObjectSet<Long2ObjectMap.Entry<V>> entries = map.long2ObjectEntrySet();
		if(entries instanceof Long2ObjectMap.FastEntrySet) ((Long2ObjectMap.FastEntrySet<V>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectMap<V> synchronize(Long2ObjectMap<V> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectMap<V> synchronize(Long2ObjectMap<V> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectSortedMap<V> synchronize(Long2ObjectSortedMap<V> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectSortedMap<V> synchronize(Long2ObjectSortedMap<V> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectOrderedMap<V> synchronize(Long2ObjectOrderedMap<V> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectOrderedMap<V> synchronize(Long2ObjectOrderedMap<V> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectNavigableMap<V> synchronize(Long2ObjectNavigableMap<V> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <V> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <V> Long2ObjectNavigableMap<V> synchronize(Long2ObjectNavigableMap<V> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <V> Long2ObjectMap<V> unmodifiable(Long2ObjectMap<V> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Long2ObjectOrderedMap<V> unmodifiable(Long2ObjectOrderedMap<V> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Long2ObjectSortedMap<V> unmodifiable(Long2ObjectSortedMap<V> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <V> Long2ObjectNavigableMap<V> unmodifiable(Long2ObjectNavigableMap<V> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Long2ObjectMap.Entry<V> unmodifiable(Long2ObjectMap.Entry<V> entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <V> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <V> Long2ObjectMap.Entry<V> unmodifiable(Map.Entry<Long, V> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<V>)entry : (entry == null ? null : new UnmodifyableEntry<>(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <V> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <V> Long2ObjectMap<V> singleton(long key, V value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<V> extends AbstractLong2ObjectMap<V> {
		final long key;
		final V value;
		LongSet keySet;
		ObjectCollection<V> values;
		ObjectSet<Long2ObjectMap.Entry<V>> entrySet;
		
		SingletonMap(long key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public V put(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(long key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(long key) { return Objects.equals(this.key, Long.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public V getOrDefault(long key, V defaultValue) { return Objects.equals(this.key, Long.valueOf(key)) ? value : defaultValue; }
		@Override
		public V compute(long key, LongObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(long key, Long2ObjectFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(long key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(long key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Long2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap<V> copy() { return new SingletonMap<>(key, value); }
		@Override
		public LongSet keySet() { 
			if(keySet == null) keySet = LongSets.singleton(key);
			return keySet;
		}
		
		@Override
		public ObjectCollection<V> values() { 
			if(values == null) values = ObjectSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractLong2ObjectMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<V> extends AbstractLong2ObjectMap<V> {
		@Override
		public V put(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(long key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(long key) { return getDefaultReturnValue(); }
		@Override
		public V getOrDefault(long key, V defaultValue) { return defaultValue; }
		@Override
		public V compute(long key, LongObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(long key, Long2ObjectFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(long key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(long key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Long2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public LongSet keySet() { return LongSets.empty(); }
		@Override
		public ObjectCollection<V> values() { return ObjectCollections.empty(); }
		@Override
		public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<V> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<V> extends AbstractLong2ObjectMap.BasicEntry<V> {
		
		UnmodifyableEntry(Map.Entry<Long, V> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Long2ObjectMap.Entry<V> entry) {
			super(entry.getLongKey(), entry.getValue());
		}
		
		@Override
		public void set(long key, V value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<V> extends UnmodifyableSortedMap<V> implements Long2ObjectNavigableMap<V> {
		Long2ObjectNavigableMap<V> map;
		
		UnmodifyableNavigableMap(Long2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Long2ObjectNavigableMap<V> descendingMap() { return Long2ObjectMaps.unmodifiable(map.descendingMap()); }
		@Override
		public LongNavigableSet navigableKeySet() { return LongSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public LongNavigableSet keySet() { return LongSets.unmodifiable(map.keySet()); }
		@Override
		public LongNavigableSet descendingKeySet() { return LongSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Long2ObjectMap.Entry<V> firstEntry() { return Long2ObjectMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Long2ObjectMap.Entry<V> lastEntry() { return Long2ObjectMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Long2ObjectMap.Entry<V> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2ObjectMap.Entry<V> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Long2ObjectNavigableMap<V> subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { return Long2ObjectMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Long2ObjectNavigableMap<V> headMap(long toKey, boolean inclusive) { return Long2ObjectMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Long2ObjectNavigableMap<V> tailMap(long fromKey, boolean inclusive) { return Long2ObjectMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Long2ObjectNavigableMap<V> subMap(long fromKey, long toKey) { return Long2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2ObjectNavigableMap<V> headMap(long toKey) { return Long2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2ObjectNavigableMap<V> tailMap(long fromKey) { return Long2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(long e) { throw new UnsupportedOperationException(); }
		@Override
		public long getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(long e) { throw new UnsupportedOperationException(); }
		@Override
		public long getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public long lowerKey(long key) { return map.lowerKey(key); }
		@Override
		public long higherKey(long key) { return map.higherKey(key); }
		@Override
		public long floorKey(long key) { return map.floorKey(key); }
		@Override
		public long ceilingKey(long key) { return map.ceilingKey(key); }
		@Override
		public Long2ObjectMap.Entry<V> lowerEntry(long key) { return Long2ObjectMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Long2ObjectMap.Entry<V> higherEntry(long key) { return Long2ObjectMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Long2ObjectMap.Entry<V> floorEntry(long key) { return Long2ObjectMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Long2ObjectMap.Entry<V> ceilingEntry(long key) { return Long2ObjectMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Long2ObjectNavigableMap<V> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<V> extends UnmodifyableMap<V> implements Long2ObjectOrderedMap<V> {
		Long2ObjectOrderedMap<V> map;
		
		UnmodifyableOrderedMap(Long2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putAndMoveToLast(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToFirst(long key) { throw new UnsupportedOperationException(); }
		@Override
		public V getAndMoveToLast(long key) { throw new UnsupportedOperationException(); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Long2ObjectOrderedMap<V> copy() { return map.copy(); }
		@Override
		public LongOrderedSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return (LongOrderedSet)keys;
		}
				
		@Override
		public ObjectOrderedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableOrderedEntrySet<>(map.long2ObjectEntrySet());
			return (ObjectOrderedSet<Long2ObjectMap.Entry<V>>)entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<V> extends UnmodifyableMap<V> implements Long2ObjectSortedMap<V> {
		Long2ObjectSortedMap<V> map;
		
		UnmodifyableSortedMap(Long2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { return map.comparator(); }
		@Override
		public Long2ObjectSortedMap<V> subMap(long fromKey, long toKey) { return Long2ObjectMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Long2ObjectSortedMap<V> headMap(long toKey) { return Long2ObjectMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Long2ObjectSortedMap<V> tailMap(long fromKey) { return Long2ObjectMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public LongSortedSet keySet() { return LongSets.unmodifiable(map.keySet()); }
		@Override
		public long firstLongKey() { return map.firstLongKey(); }
		@Override
		public long pollFirstLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public long lastLongKey() { return map.lastLongKey(); }
		@Override
		public long pollLastLongKey() { throw new UnsupportedOperationException(); }
		@Override
		public V firstValue() { return map.firstValue(); }
		@Override
		public V lastValue() { return map.lastValue(); }
		@Override
		public Long2ObjectSortedMap<V> copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectMap<V> {
		Long2ObjectMap<V> map;
		ObjectCollection<V> values;
		LongSet keys;
		ObjectSet<Long2ObjectMap.Entry<V>> entrySet;
		
		UnmodifyableMap(Long2ObjectMap<V> map) {
			this.map = map;
		}
		
		@Override
		public V put(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V putIfAbsent(long key, V value){ throw new UnsupportedOperationException(); }
		@Override
		public V remove(long key) { throw new UnsupportedOperationException(); }
		@Override
		public V removeOrDefault(long key, V defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(long key, V value) { throw new UnsupportedOperationException(); }
		@Override
		public V get(long key) {
			V type = map.get(key);
			return Objects.equals(type, map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public V getOrDefault(long key, V defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public V compute(long key, LongObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfAbsent(long key, Long2ObjectFunction<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public V supplyIfAbsent(long key, ObjectSupplier<V> valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public V merge(long key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAll(Long2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceObjects(LongObjectUnaryOperator<V> mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void replaceObjects(Long2ObjectMap<V> m) { throw new UnsupportedOperationException(); }
		@Override
		public Long2ObjectMap<V> copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public LongSet keySet() { 
			if(keys == null) keys = LongSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.long2ObjectEntrySet());
			return entrySet;
		}
	}
	
	
	/**
	 * The Unmodifyable Ordered Set implementation for the Unmodifyable Ordered Map implementation
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedEntrySet<V> extends UnmodifyableEntrySet<V> implements ObjectOrderedSet<Long2ObjectMap.Entry<V>>
	{
		ObjectOrderedSet<Long2ObjectMap.Entry<V>> set;
		
		UnmodifyableOrderedEntrySet(ObjectOrderedSet<Long2ObjectMap.Entry<V>> c) {
			super(c);
			set = c;
		}

		@Override
		public boolean addAndMoveToFirst(Long2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean addAndMoveToLast(Long2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(Long2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(Long2ObjectMap.Entry<V> o) { throw new UnsupportedOperationException(); }
		@Override
		public ObjectOrderedSet<Long2ObjectMap.Entry<V>> copy() { return set.copy(); }
		@Override
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> iterator() { return ObjectIterators.unmodifiable(set.iterator()); }
		@Override
		public ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> iterator(Long2ObjectMap.Entry<V> fromElement) { return ObjectIterators.unmodifiable(set.iterator(fromElement)); }
		@Override
		public Long2ObjectMap.Entry<V> first() { return set.first(); }
		@Override
		public Long2ObjectMap.Entry<V> pollFirst() { throw new UnsupportedOperationException(); }
		@Override
		public Long2ObjectMap.Entry<V> last() { return set.last(); }
		@Override
		public Long2ObjectMap.Entry<V> pollLast() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<V> extends ObjectSets.UnmodifiableSet<Long2ObjectMap.Entry<V>>
	{
		ObjectSet<Long2ObjectMap.Entry<V>> s;
		
		UnmodifyableEntrySet(ObjectSet<Long2ObjectMap.Entry<V>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Long2ObjectMap.Entry<V>> action) {
			s.forEach(T -> action.accept(Long2ObjectMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Long2ObjectMap.Entry<V>> iterator() {
			return new ObjectIterator<Long2ObjectMap.Entry<V>>() {
				ObjectIterator<Long2ObjectMap.Entry<V>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Long2ObjectMap.Entry<V> next() { return Long2ObjectMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<V> extends SynchronizedSortedMap<V> implements Long2ObjectNavigableMap<V> {
		Long2ObjectNavigableMap<V> map;
		
		SynchronizedNavigableMap(Long2ObjectNavigableMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Long2ObjectNavigableMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Long2ObjectNavigableMap<V> descendingMap() { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public LongNavigableSet navigableKeySet() { synchronized(mutex) { return LongSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public LongNavigableSet descendingKeySet() { synchronized(mutex) { return LongSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public LongNavigableSet keySet() { synchronized(mutex) { return LongSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Long2ObjectMap.Entry<V> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Long2ObjectMap.Entry<V> lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Long2ObjectMap.Entry<V> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Long2ObjectMap.Entry<V> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Long2ObjectNavigableMap<V> subMap(long fromKey, boolean fromInclusive, long toKey, boolean toInclusive) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Long2ObjectNavigableMap<V> headMap(long toKey, boolean inclusive) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Long2ObjectNavigableMap<V> tailMap(long fromKey, boolean inclusive) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Long2ObjectNavigableMap<V> subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2ObjectNavigableMap<V> headMap(long toKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2ObjectNavigableMap<V> tailMap(long fromKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public long lowerKey(long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public long higherKey(long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public long floorKey(long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public long ceilingKey(long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Long2ObjectMap.Entry<V> lowerEntry(long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Long2ObjectMap.Entry<V> higherEntry(long key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Long2ObjectMap.Entry<V> floorEntry(long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Long2ObjectMap.Entry<V> ceilingEntry(long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Long2ObjectNavigableMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Long2ObjectNavigableMap<V> subMap(Long fromKey, boolean fromInclusive, Long toKey, boolean toInclusive) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ObjectNavigableMap<V> headMap(Long toKey, boolean inclusive) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ObjectNavigableMap<V> tailMap(Long fromKey, boolean inclusive) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Long2ObjectNavigableMap<V> subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ObjectNavigableMap<V> headMap(Long toKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ObjectNavigableMap<V> tailMap(Long fromKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(long e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public long getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(long e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public long getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Long lowerKey(Long key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Long floorKey(Long key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Long ceilingKey(Long key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Long higherKey(Long key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Long2ObjectMap.Entry<V> lowerEntry(Long key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Long2ObjectMap.Entry<V> floorEntry(Long key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Long2ObjectMap.Entry<V> ceilingEntry(Long key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Long2ObjectMap.Entry<V> higherEntry(Long key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<V> extends SynchronizedMap<V> implements Long2ObjectOrderedMap<V> {
		Long2ObjectOrderedMap<V> map;
		
		SynchronizedOrderedMap(Long2ObjectOrderedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Long2ObjectOrderedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public V putAndMoveToFirst(long key, V value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public V putAndMoveToLast(long key, V value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(long key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(long key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public V getAndMoveToFirst(long key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public V getAndMoveToLast(long key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Long2ObjectOrderedMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public LongOrderedSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return (LongOrderedSet)keys;
		}
		
		@Override
		public ObjectOrderedSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2ObjectEntrySet(), mutex);
			return (ObjectOrderedSet<Long2ObjectMap.Entry<V>>)entrySet;
		}
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<V> extends SynchronizedMap<V> implements Long2ObjectSortedMap<V> {
		Long2ObjectSortedMap<V> map;
		
		SynchronizedSortedMap(Long2ObjectSortedMap<V> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Long2ObjectSortedMap<V> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public LongComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Long2ObjectSortedMap<V> subMap(long fromKey, long toKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Long2ObjectSortedMap<V> headMap(long toKey)  { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Long2ObjectSortedMap<V> tailMap(long fromKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public LongSortedSet keySet() { synchronized(mutex) { return LongSets.synchronize(map.keySet(), mutex); } }
		@Override
		public long firstLongKey() { synchronized(mutex) { return map.firstLongKey(); } }
		@Override
		public long pollFirstLongKey() { synchronized(mutex) { return map.pollFirstLongKey(); } }
		@Override
		public long lastLongKey() { synchronized(mutex) { return map.lastLongKey(); } }
		@Override
		public long pollLastLongKey() { synchronized(mutex) { return map.pollLastLongKey(); } }
		@Override
		public V firstValue() { synchronized(mutex) { return map.firstValue(); } }
		@Override
		public V lastValue() { synchronized(mutex) { return map.lastValue(); } }
		@Override
		public Long2ObjectSortedMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Long firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Long lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Long2ObjectSortedMap<V> subMap(Long fromKey, Long toKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ObjectSortedMap<V> headMap(Long toKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Long2ObjectSortedMap<V> tailMap(Long fromKey) { synchronized(mutex) { return Long2ObjectMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <V> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectMap<V> {
		Long2ObjectMap<V> map;
		ObjectCollection<V> values;
		LongSet keys;
		ObjectSet<Long2ObjectMap.Entry<V>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Long2ObjectMap<V> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Long2ObjectMap<V> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public V getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractLong2ObjectMap<V> setDefaultReturnValue(V v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public V put(long key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public V putIfAbsent(long key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Long2ObjectMap<V> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public void putAll(Long2ObjectMap<V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Long, ? extends V> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(long[] keys, V[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(long key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public V get(long key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public V remove(long key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public V removeOrDefault(long key, V defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(long key, V value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(long key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public V replace(long key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceObjects(Long2ObjectMap<V> m) { synchronized(mutex) { map.replaceObjects(m); } }
		@Override
		public void replaceObjects(LongObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { map.replaceObjects(mappingFunction); } }
		@Override
		public V compute(long key, LongObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		public V computeIfAbsent(long key, Long2ObjectFunction<V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		public V computeIfPresent(long key, LongObjectUnaryOperator<V> mappingFunction) { synchronized(mutex) { return map.computeIfPresent(key, mappingFunction); } }
		@Override
		public V supplyIfAbsent(long key, ObjectSupplier<V> valueProvider) { synchronized(mutex) { return map.supplyIfAbsent(key, valueProvider); } }
		@Override
		public V merge(long key, V value, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		public void mergeAll(Long2ObjectMap<V> m, ObjectObjectUnaryOperator<V, V> mappingFunction) { synchronized(mutex) { map.mergeAll(m, mappingFunction); } }
		@Override
		public V getOrDefault(long key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(LongObjectConsumer<V> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Long2ObjectMap<V> copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public LongSet keySet() {
			if(keys == null) keys = LongSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public ObjectCollection<V> values() {
			if(values == null) values = ObjectCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.long2ObjectEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public V get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public V getOrDefault(Object key, V defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public V put(Long key, V value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public V remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public V putIfAbsent(Long key, V value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Long key, V oldValue, V newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public V replace(Long key, V value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Long, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public V compute(Long key, BiFunction<? super Long, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfAbsent(Long key, Function<? super Long, ? extends V> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V computeIfPresent(Long key, BiFunction<? super Long, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public V merge(Long key, V value, BiFunction<? super V, ? super V, ? extends V> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Long, ? super V> action) { synchronized(mutex) { map.forEach(action); } }
	}
}