package speiger.src.collections.objects.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.Comparator;
import java.util.function.Consumer;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.objects.functions.function.Object2LongFunction;
import speiger.src.collections.objects.functions.function.ObjectLongUnaryOperator;
import speiger.src.collections.objects.maps.abstracts.AbstractObject2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongNavigableMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongSortedMap;
import speiger.src.collections.objects.maps.interfaces.Object2LongOrderedMap;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.utils.LongCollections;
import speiger.src.collections.longs.utils.LongSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Object2LongMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Object2LongMap<?> EMPTY = new EmptyMap<>();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @param <T> the type of elements maintained by this Collection
	 * @return empty map of desired type
	 */
	public static <T> Object2LongMap<T> empty() { 
		return (Object2LongMap<T>)EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static <T> ObjectIterator<Object2LongMap.Entry<T>> fastIterator(Object2LongMap<T> map) {
		ObjectSet<Object2LongMap.Entry<T>> entries = map.object2LongEntrySet();
		return entries instanceof Object2LongMap.FastEntrySet ? ((Object2LongMap.FastEntrySet<T>)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @param <T> the type of elements maintained by this Collection
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static <T> ObjectIterable<Object2LongMap.Entry<T>> fastIterable(Object2LongMap<T> map) {
		ObjectSet<Object2LongMap.Entry<T>> entries = map.object2LongEntrySet();
		return map instanceof Object2LongMap.FastEntrySet ? new ObjectIterable<Object2LongMap.Entry<T>>(){
			@Override
			public ObjectIterator<Object2LongMap.Entry<T>> iterator() { return ((Object2LongMap.FastEntrySet<T>)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Object2LongMap.Entry<T>> action) { ((Object2LongMap.FastEntrySet<T>)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @param <T> the type of elements maintained by this Collection
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static <T> void fastForEach(Object2LongMap<T> map, Consumer<Object2LongMap.Entry<T>> action) {
		ObjectSet<Object2LongMap.Entry<T>> entries = map.object2LongEntrySet();
		if(entries instanceof Object2LongMap.FastEntrySet) ((Object2LongMap.FastEntrySet<T>)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongMap<T> synchronize(Object2LongMap<T> map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongMap<T> synchronize(Object2LongMap<T> map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongSortedMap<T> synchronize(Object2LongSortedMap<T> map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongSortedMap<T> synchronize(Object2LongSortedMap<T> map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongOrderedMap<T> synchronize(Object2LongOrderedMap<T> map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongOrderedMap<T> synchronize(Object2LongOrderedMap<T> map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap<>(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongNavigableMap<T> synchronize(Object2LongNavigableMap<T> map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @param <T> the type of elements maintained by this Collection
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static <T> Object2LongNavigableMap<T> synchronize(Object2LongNavigableMap<T> map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap<>(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2LongMap<T> unmodifiable(Object2LongMap<T> map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2LongOrderedMap<T> unmodifiable(Object2LongOrderedMap<T> map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2LongSortedMap<T> unmodifiable(Object2LongSortedMap<T> map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap<>(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static <T> Object2LongNavigableMap<T> unmodifiable(Object2LongNavigableMap<T> map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap<>(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2LongMap.Entry<T> unmodifiable(Object2LongMap.Entry<T> entry) { return entry instanceof UnmodifyableEntry ? entry : new UnmodifyableEntry<>(entry); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @param <T> the type of elements maintained by this Collection
	 * @return a Unmodifyable Entry
	 */
	public static <T> Object2LongMap.Entry<T> unmodifiable(Map.Entry<T, Long> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry<T>)entry : new UnmodifyableEntry<>(entry); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @param <T> the type of elements maintained by this Collection
	 * @return a unmodifiable Singleton map.
	 */
	public static <T> Object2LongMap<T> singleton(T key, long value) { return new SingletonMap<>(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SingletonMap<T> extends AbstractObject2LongMap<T> {
		final T key;
		final long value;
		ObjectSet<T> keySet;
		LongCollection values;
		ObjectSet<Object2LongMap.Entry<T>> entrySet;
		
		SingletonMap(T key, long value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public long put(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public long remOrDefault(T key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long getLong(T key) { return Objects.equals(this.key, key) ? value : getDefaultReturnValue(); }
		@Override
		public long getOrDefault(T key, long defaultValue) { return Objects.equals(this.key, key) ? value : defaultValue; }
		@Override
		public SingletonMap<T> copy() { return new SingletonMap<>(key, value); }
		@Override
		public ObjectSet<T> keySet() { 
			if(keySet == null) keySet = ObjectSets.singleton(key);
			return keySet;
		}
		
		@Override
		public LongCollection values() { 
			if(values == null) values = LongSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractObject2LongMap.BasicEntry<>(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class EmptyMap<T> extends AbstractObject2LongMap<T> {
		@Override
		public long put(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long addTo(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public long remOrDefault(T key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long getLong(T key) { return getDefaultReturnValue(); }
		@Override
		public long getOrDefault(T key, long defaultValue) { return 0L; }
		@Override
		public ObjectSet<T> keySet() { return ObjectSets.empty(); }
		@Override
		public LongCollection values() { return LongCollections.empty(); }
		@Override
		public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap<T> copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntry<T> extends AbstractObject2LongMap.BasicEntry<T> {
		
		UnmodifyableEntry(Map.Entry<T, Long> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Object2LongMap.Entry<T> entry) {
			super(entry.getKey(), entry.getLongValue());
		}
		
		@Override
		public void set(T key, long value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableNavigableMap<T> extends UnmodifyableSortedMap<T> implements Object2LongNavigableMap<T> {
		Object2LongNavigableMap<T> map;
		
		UnmodifyableNavigableMap(Object2LongNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Object2LongNavigableMap<T> descendingMap() { return Object2LongMaps.synchronize(map.descendingMap()); }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { return ObjectSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { return ObjectSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Object2LongMap.Entry<T> firstEntry() { return Object2LongMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Object2LongMap.Entry<T> lastEntry() { return Object2LongMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Object2LongMap.Entry<T> pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2LongMap.Entry<T> pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Object2LongNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { return Object2LongMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Object2LongNavigableMap<T> headMap(T toKey, boolean inclusive) { return Object2LongMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Object2LongNavigableMap<T> tailMap(T fromKey, boolean inclusive) { return Object2LongMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Object2LongNavigableMap<T> subMap(T fromKey, T toKey) { return Object2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2LongNavigableMap<T> headMap(T toKey) { return Object2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2LongNavigableMap<T> tailMap(T fromKey) { return Object2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T lowerKey(T key) { return map.lowerKey(key); }
		@Override
		public T higherKey(T key) { return map.higherKey(key); }
		@Override
		public T floorKey(T key) { return map.floorKey(key); }
		@Override
		public T ceilingKey(T key) { return map.ceilingKey(key); }
		@Override
		public Object2LongMap.Entry<T> lowerEntry(T key) { return Object2LongMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Object2LongMap.Entry<T> higherEntry(T key) { return Object2LongMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Object2LongMap.Entry<T> floorEntry(T key) { return Object2LongMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Object2LongMap.Entry<T> ceilingEntry(T key) { return Object2LongMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Object2LongNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableOrderedMap<T> extends UnmodifyableMap<T> implements Object2LongOrderedMap<T> {
		Object2LongOrderedMap<T> map;
		
		UnmodifyableOrderedMap(Object2LongOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putAndMoveToLast(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToFirst(T key) { throw new UnsupportedOperationException(); }
		@Override
		public long getAndMoveToLast(T key) { throw new UnsupportedOperationException(); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Object2LongOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableSortedMap<T> extends UnmodifyableMap<T> implements Object2LongSortedMap<T> {
		Object2LongSortedMap<T> map;
		
		UnmodifyableSortedMap(Object2LongSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { return map.comparator(); }
		@Override
		public Object2LongSortedMap<T> subMap(T fromKey, T toKey) { return Object2LongMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Object2LongSortedMap<T> headMap(T toKey) { return Object2LongMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Object2LongSortedMap<T> tailMap(T fromKey) { return Object2LongMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public T firstKey() { return map.firstKey(); }
		@Override
		public T pollFirstKey() { return map.pollFirstKey(); }
		@Override
		public T lastKey() { return map.lastKey(); }
		@Override
		public T pollLastKey() { return map.pollLastKey(); }
		@Override
		public long firstLongValue() { return map.firstLongValue(); }
		@Override
		public long lastLongValue() { return map.lastLongValue(); }
		@Override
		public Object2LongSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableMap<T> extends AbstractObject2LongMap<T> implements Object2LongMap<T> {
		Object2LongMap<T> map;
		LongCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2LongMap.Entry<T>> entrySet;
		
		UnmodifyableMap(Object2LongMap<T> map) {
			this.map = map;
		}
		
		@Override
		public long put(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long putIfAbsent(T key, long value){ throw new UnsupportedOperationException(); }
		@Override
		public long addTo(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long subFrom(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long rem(T key) { throw new UnsupportedOperationException(); }
		@Override
		public long remOrDefault(T key, long defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(T key, long value) { throw new UnsupportedOperationException(); }
		@Override
		public long getLong(T key) { return map.getLong(key); }
		@Override
		public long getOrDefault(T key, long defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public Object2LongMap<T> copy() { throw new UnsupportedOperationException(); }
		
		@Override
		public ObjectSet<T> keySet() { 
			if(keys == null) keys = ObjectSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet<>(map.object2LongEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class UnmodifyableEntrySet<T> extends ObjectSets.UnmodifiableSet<Object2LongMap.Entry<T>>
	{
		ObjectSet<Object2LongMap.Entry<T>> s;
		
		UnmodifyableEntrySet(ObjectSet<Object2LongMap.Entry<T>> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Object2LongMap.Entry<T>> action) {
			s.forEach(T -> action.accept(Object2LongMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Object2LongMap.Entry<T>> iterator() {
			return new ObjectIterator<Object2LongMap.Entry<T>>() {
				ObjectIterator<Object2LongMap.Entry<T>> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Object2LongMap.Entry<T> next() { return Object2LongMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedNavigableMap<T> extends SynchronizedSortedMap<T> implements Object2LongNavigableMap<T> {
		Object2LongNavigableMap<T> map;
		
		SynchronizedNavigableMap(Object2LongNavigableMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Object2LongNavigableMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Object2LongNavigableMap<T> descendingMap() { synchronized(mutex) { return Object2LongMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ObjectNavigableSet<T> navigableKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ObjectNavigableSet<T> descendingKeySet() { synchronized(mutex) { return ObjectSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public Object2LongMap.Entry<T> firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2LongMap.Entry<T> lastEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Object2LongMap.Entry<T> pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Object2LongMap.Entry<T> pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Object2LongNavigableMap<T> subMap(T fromKey, boolean fromInclusive, T toKey, boolean toInclusive) { synchronized(mutex) { return Object2LongMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Object2LongNavigableMap<T> headMap(T toKey, boolean inclusive) { synchronized(mutex) { return Object2LongMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Object2LongNavigableMap<T> tailMap(T fromKey, boolean inclusive) { synchronized(mutex) { return Object2LongMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Object2LongNavigableMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2LongNavigableMap<T> headMap(T toKey) { synchronized(mutex) { return Object2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2LongNavigableMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T lowerKey(T key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public T higherKey(T key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public T floorKey(T key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public T ceilingKey(T key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Object2LongMap.Entry<T> lowerEntry(T key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Object2LongMap.Entry<T> higherEntry(T key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Object2LongMap.Entry<T> floorEntry(T key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Object2LongMap.Entry<T> ceilingEntry(T key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Object2LongNavigableMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedOrderedMap<T> extends SynchronizedMap<T> implements Object2LongOrderedMap<T> {
		Object2LongOrderedMap<T> map;
		
		SynchronizedOrderedMap(Object2LongOrderedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Object2LongOrderedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public long putAndMoveToFirst(T key, long value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public long putAndMoveToLast(T key, long value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(T key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(T key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public long getAndMoveToFirst(T key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public long getAndMoveToLast(T key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Object2LongOrderedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedSortedMap<T> extends SynchronizedMap<T> implements Object2LongSortedMap<T> {
		Object2LongSortedMap<T> map;
		
		SynchronizedSortedMap(Object2LongSortedMap<T> map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Object2LongSortedMap<T> map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Comparator<T> comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Object2LongSortedMap<T> subMap(T fromKey, T toKey) { synchronized(mutex) { return Object2LongMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Object2LongSortedMap<T> headMap(T toKey)  { synchronized(mutex) { return Object2LongMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Object2LongSortedMap<T> tailMap(T fromKey) { synchronized(mutex) { return Object2LongMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public T firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		public T pollFirstKey() { synchronized(mutex) { return map.pollFirstKey(); } }
		@Override
		public T lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		public T pollLastKey() { synchronized(mutex) { return map.pollLastKey(); } }
		@Override
		public long firstLongValue() { synchronized(mutex) { return map.firstLongValue(); } }
		@Override
		public long lastLongValue() { synchronized(mutex) { return map.lastLongValue(); } }
		@Override
		public Object2LongSortedMap<T> copy() { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class SynchronizedMap<T> extends AbstractObject2LongMap<T> implements Object2LongMap<T> {
		Object2LongMap<T> map;
		LongCollection values;
		ObjectSet<T> keys;
		ObjectSet<Object2LongMap.Entry<T>> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Object2LongMap<T> map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Object2LongMap<T> map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public long getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractObject2LongMap<T> setDefaultReturnValue(long v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public long put(T key, long value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public long putIfAbsent(T key, long value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Object2LongMap<T> m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public long addTo(T key, long value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public long subFrom(T key, long value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Object2LongMap<T> m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Object2LongMap<T> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends T, ? extends Long> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(T[] keys, long[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsValue(long value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public long getLong(T key) { synchronized(mutex) { return map.getLong(key); } }
		@Override
		public long rem(T key) { synchronized(mutex) { return map.rem(key); } }
		@Override
		public long remOrDefault(T key, long defaultValue) { synchronized(mutex) { return map.remOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(T key, long value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(T key, long oldValue, long newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public long replace(T key, long value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceLongs(Object2LongMap<T> m) { synchronized(mutex) { map.replaceLongs(m); } }
		@Override
		public void replaceLongs(ObjectLongUnaryOperator<T> mappingFunction) { synchronized(mutex) { map.replaceLongs(mappingFunction); } }
		@Override
		public long computeLong(T key, ObjectLongUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeLong(key, mappingFunction); } }
		@Override
		public long computeLongIfAbsent(T key, Object2LongFunction<T> mappingFunction) { synchronized(mutex) { return map.computeLongIfAbsent(key, mappingFunction); } }
		@Override
		public long computeLongIfPresent(T key, ObjectLongUnaryOperator<T> mappingFunction) { synchronized(mutex) { return map.computeLongIfPresent(key, mappingFunction); } }
		@Override
		public long mergeLong(T key, long value, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeLong(key, value, mappingFunction); } }
		@Override
		public void mergeAllLong(Object2LongMap<T> m, LongLongUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllLong(m, mappingFunction); } }
		@Override
		public long getOrDefault(T key, long defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ObjectLongConsumer<T> action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return super.size(); } }
		@Override
		public Object2LongMap<T> copy() { throw new UnsupportedOperationException(); }
		@Override
		public ObjectSet<T> keySet() {
			if(keys == null) keys = ObjectSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public LongCollection values() {
			if(values == null) values = LongCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Object2LongMap.Entry<T>> object2LongEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.object2LongEntrySet(), mutex);
			return entrySet;
		}
		
	}
}