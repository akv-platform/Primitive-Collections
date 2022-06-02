package speiger.src.collections.bytes.utils.maps;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.bytes.functions.ByteComparator;
import speiger.src.collections.bytes.functions.consumer.ByteFloatConsumer;
import speiger.src.collections.bytes.functions.function.Byte2FloatFunction;
import speiger.src.collections.bytes.functions.function.ByteFloatUnaryOperator;
import speiger.src.collections.bytes.maps.abstracts.AbstractByte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatNavigableMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatSortedMap;
import speiger.src.collections.bytes.maps.interfaces.Byte2FloatOrderedMap;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteSortedSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.functions.function.FloatFloatUnaryOperator;
import speiger.src.collections.floats.functions.FloatSupplier;
import speiger.src.collections.floats.utils.FloatCollections;
import speiger.src.collections.floats.utils.FloatSets;

/**
 * A Helper class that provides you with Singleton/Empty/Synchronized/Unmodifyable Maps
 */
public class Byte2FloatMaps
{
	/**
	 * Empty Map Variable
	 */
	private static final Byte2FloatMap EMPTY = new EmptyMap();
	
	/**
	 * Empty Map getter function that autocasts to the desired Key and Value
	 * @return empty map of desired type
	 */
	public static Byte2FloatMap empty() { 
		return EMPTY;
	}
	
	/**
	 * Helper method that provides the fastIterator that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterator should be accessed from
	 * @return either a normal iterator if it does not support this feature to a fastIterator
	 */
	public static ObjectIterator<Byte2FloatMap.Entry> fastIterator(Byte2FloatMap map) {
		ObjectSet<Byte2FloatMap.Entry> entries = map.byte2FloatEntrySet();
		return entries instanceof Byte2FloatMap.FastEntrySet ? ((Byte2FloatMap.FastEntrySet)entries).fastIterator() : entries.iterator();
	}
	
	/**
	 * Helper method that provides the fastIterable that recycles a single Entry to increase throughput.
	 * @param map the map the fastIterable should be accessed from
	 * @return either a normal iterable if it does not support this feature to a fastIterable
	 */
	public static ObjectIterable<Byte2FloatMap.Entry> fastIterable(Byte2FloatMap map) {
		ObjectSet<Byte2FloatMap.Entry> entries = map.byte2FloatEntrySet();
		return map instanceof Byte2FloatMap.FastEntrySet ? new ObjectIterable<Byte2FloatMap.Entry>(){
			@Override
			public ObjectIterator<Byte2FloatMap.Entry> iterator() { return ((Byte2FloatMap.FastEntrySet)entries).fastIterator(); }
			@Override
			public void forEach(Consumer<? super Byte2FloatMap.Entry> action) { ((Byte2FloatMap.FastEntrySet)entries).fastForEach(action); }
		} : entries;
	}
	
	/**
	 * A Helper function that provides a faster forEach iterator implementation that recycles the entry to increase throughput
	 * @param map the map the fast forEach should be accessed from
	 * @param action the action that should be performed on each entry
	 * @note if the fast forEach is not supported will default to a normal forEach
	 */
	public static void fastForEach(Byte2FloatMap map, Consumer<Byte2FloatMap.Entry> action) {
		ObjectSet<Byte2FloatMap.Entry> entries = map.byte2FloatEntrySet();
		if(entries instanceof Byte2FloatMap.FastEntrySet) ((Byte2FloatMap.FastEntrySet)entries).fastForEach(action); 
		else entries.forEach(action);
	}
	
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the map.
	 * @param map the map that should be synchronized
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatMap synchronize(Byte2FloatMap map) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the map.
	 * @param map the map that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized Map
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatMap synchronize(Byte2FloatMap map, Object mutex) { return map instanceof SynchronizedMap ? map : new SynchronizedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatSortedMap synchronize(Byte2FloatSortedMap map) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the SortedMap.
	 * @param map the SortedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized SortedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatSortedMap synchronize(Byte2FloatSortedMap map, Object mutex) { return map instanceof SynchronizedSortedMap ? map : new SynchronizedSortedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatOrderedMap synchronize(Byte2FloatOrderedMap map) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the OrderedMap.
	 * @param map the OrderedMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized OrderedMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatOrderedMap synchronize(Byte2FloatOrderedMap map, Object mutex) { return map instanceof SynchronizedOrderedMap ? map : new SynchronizedOrderedMap(map, mutex); }
	
	/**
	 * Helper function that creates a Helper wrapper to synchronize access into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatNavigableMap synchronize(Byte2FloatNavigableMap map) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map); }
	/**
	 * Helper function that creates a Helper wrapper to synchronize access with custom access control into the NavigableMap.
	 * @param map the NavigableMap that should be synchronized
	 * @param mutex the object that controls access
	 * @return a synchronized NavigableMap
	 * @note if the inputted map is already synchronized then it will just return it instead
	 * @note iterators do not support synchronization
	 */
	public static Byte2FloatNavigableMap synchronize(Byte2FloatNavigableMap map, Object mutex) { return map instanceof SynchronizedNavigableMap ? map : new SynchronizedNavigableMap(map, mutex); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the Map
	 * @param map the map that should be made Unmodifiable
	 * @return a unmodifiable Map
	 * @note if the inputted map is already unmodifiable then it will just return it instead
	 */
	public static Byte2FloatMap unmodifiable(Byte2FloatMap map) { return map instanceof UnmodifyableMap ? map : new UnmodifyableMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the OrderedMap
	 * @param map the OrderedMap that should be made Unmodifiable
	 * @return a unmodifiable OrderedMap
	 * @note if the inputted OrderedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2FloatOrderedMap unmodifiable(Byte2FloatOrderedMap map) { return map instanceof UnmodifyableOrderedMap ? map : new UnmodifyableOrderedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into the SortedMap
	 * @param map the SortedMap that should be made Unmodifiable
	 * @return a unmodifiable SortedMap
	 * @note if the inputted SortedMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2FloatSortedMap unmodifiable(Byte2FloatSortedMap map) { return map instanceof UnmodifyableSortedMap ? map : new UnmodifyableSortedMap(map); }
	
	/**
	 * A Helper function that creates a Helper wrapper to only allow Read Access into NavigableMap Map
	 * @param map the NavigableMap that should be made Unmodifiable
	 * @return a unmodifiable NavigableMap
	 * @note if the inputted NavigableMap is already unmodifiable then it will just return it instead
	 */
	public static Byte2FloatNavigableMap unmodifiable(Byte2FloatNavigableMap map) { return map instanceof UnmodifyableNavigableMap ? map : new UnmodifyableNavigableMap(map); }
	
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2FloatMap.Entry unmodifiable(Byte2FloatMap.Entry entry) { return entry instanceof UnmodifyableEntry ? entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	/**
	 * A Helper function that creates a Unmodifyable Entry
	 * @param entry the Entry that should be made unmodifiable
	 * @return a Unmodifyable Entry
	 */
	public static Byte2FloatMap.Entry unmodifiable(Map.Entry<Byte, Float> entry) { return entry instanceof UnmodifyableEntry ? (UnmodifyableEntry)entry : (entry == null ? null : new UnmodifyableEntry(entry)); }
	
	/**
	 * Creates a Singleton map from the provided values.
	 * This reduces overhead that normal Map implementations have.
	 * @param key the key that should be turned into a singleton
	 * @param value the value that should be turned into a singleton
	 * @return a unmodifiable Singleton map.
	 */
	public static Byte2FloatMap singleton(byte key, float value) { return new SingletonMap(key, value); }
	
	/**
	 * Singleton Map instance that is used in the helper method
	 */
	public static class SingletonMap extends AbstractByte2FloatMap {
		final byte key;
		final float value;
		ByteSet keySet;
		FloatCollection values;
		ObjectSet<Byte2FloatMap.Entry> entrySet;
		
		SingletonMap(byte key, float value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public float put(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(byte key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(byte key) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : getDefaultReturnValue(); }
		@Override
		public float getOrDefault(byte key, float defaultValue) { return Objects.equals(this.key, Byte.valueOf(key)) ? value : defaultValue; }
		@Override
		public float computeFloat(byte key, ByteFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(byte key, Byte2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(byte key, ByteFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(byte key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(byte key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Byte2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public SingletonMap copy() { return new SingletonMap(key, value); }
		@Override
		public ByteSet keySet() { 
			if(keySet == null) keySet = ByteSets.singleton(key);
			return keySet;
		}
		
		@Override
		public FloatCollection values() { 
			if(values == null) values = FloatSets.singleton(value);
			return values;
		}
		@Override
		public ObjectSet<Byte2FloatMap.Entry> byte2FloatEntrySet() { 
			if(entrySet == null) entrySet = ObjectSets.singleton(new AbstractByte2FloatMap.BasicEntry(key, value));
			return entrySet;
		}
	}
	
	/**
	 * Empty Map impementation that is used for the emptyMap() function
	 */
	public static class EmptyMap extends AbstractByte2FloatMap {
		@Override
		public float put(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float addTo(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(byte key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(byte key) { return getDefaultReturnValue(); }
		@Override
		public float getOrDefault(byte key, float defaultValue) { return defaultValue; }
		@Override
		public float computeFloat(byte key, ByteFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(byte key, Byte2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(byte key, ByteFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(byte key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(byte key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Byte2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public ByteSet keySet() { return ByteSets.empty(); }
		@Override
		public FloatCollection values() { return FloatCollections.empty(); }
		@Override
		public ObjectSet<Byte2FloatMap.Entry> byte2FloatEntrySet() { return ObjectSets.empty(); }
		@Override
		public EmptyMap copy() { return this; }
	}
	
	/**
	 * The Unmodifyable Entry implementation for the helper function unmodifiableEntry()
	 */
	public static class UnmodifyableEntry extends AbstractByte2FloatMap.BasicEntry {
		
		UnmodifyableEntry(Map.Entry<Byte, Float> entry) {
			super(entry.getKey(), entry.getValue());
		}
		
		UnmodifyableEntry(Byte2FloatMap.Entry entry) {
			super(entry.getByteKey(), entry.getFloatValue());
		}
		
		@Override
		public void set(byte key, float value) { throw new UnsupportedOperationException(); }
	}
	
	/**
	 * The Unmodifyable Navigable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableNavigableMap extends UnmodifyableSortedMap implements Byte2FloatNavigableMap {
		Byte2FloatNavigableMap map;
		
		UnmodifyableNavigableMap(Byte2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public Byte2FloatNavigableMap descendingMap() { return Byte2FloatMaps.unmodifiable(map.descendingMap()); }
		@Override
		public ByteNavigableSet navigableKeySet() { return ByteSets.unmodifiable(map.navigableKeySet()); }
		@Override
		public ByteNavigableSet keySet() { return ByteSets.unmodifiable(map.keySet()); }
		@Override
		public ByteNavigableSet descendingKeySet() { return ByteSets.unmodifiable(map.descendingKeySet()); }
		@Override
		public Byte2FloatMap.Entry firstEntry() { return Byte2FloatMaps.unmodifiable(map.firstEntry()); }
		@Override
		public Byte2FloatMap.Entry lastEntry() { return Byte2FloatMaps.unmodifiable(map.lastEntry()); }
		@Override
		public Byte2FloatMap.Entry pollFirstEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2FloatMap.Entry pollLastEntry() { throw new UnsupportedOperationException(); }
		@Override
		public Byte2FloatNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { return Byte2FloatMaps.unmodifiable(map.subMap(fromKey, fromInclusive, toKey, toInclusive)); }
		@Override
		public Byte2FloatNavigableMap headMap(byte toKey, boolean inclusive) { return Byte2FloatMaps.unmodifiable(map.headMap(toKey, inclusive)); }
		@Override
		public Byte2FloatNavigableMap tailMap(byte fromKey, boolean inclusive) { return Byte2FloatMaps.unmodifiable(map.tailMap(fromKey, inclusive)); }
		@Override
		public Byte2FloatNavigableMap subMap(byte fromKey, byte toKey) { return Byte2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2FloatNavigableMap headMap(byte toKey) { return Byte2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2FloatNavigableMap tailMap(byte fromKey) { return Byte2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public void setDefaultMaxValue(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public byte getDefaultMaxValue() { return map.getDefaultMaxValue(); }
		@Override
		public void setDefaultMinValue(byte e) { throw new UnsupportedOperationException(); }
		@Override
		public byte getDefaultMinValue() { return map.getDefaultMinValue(); }
		@Override
		public byte lowerKey(byte key) { return map.lowerKey(key); }
		@Override
		public byte higherKey(byte key) { return map.higherKey(key); }
		@Override
		public byte floorKey(byte key) { return map.floorKey(key); }
		@Override
		public byte ceilingKey(byte key) { return map.ceilingKey(key); }
		@Override
		public Byte2FloatMap.Entry lowerEntry(byte key) { return Byte2FloatMaps.unmodifiable(map.lowerEntry(key)); }
		@Override
		public Byte2FloatMap.Entry higherEntry(byte key) { return Byte2FloatMaps.unmodifiable(map.higherEntry(key)); }
		@Override
		public Byte2FloatMap.Entry floorEntry(byte key) { return Byte2FloatMaps.unmodifiable(map.floorEntry(key)); }
		@Override
		public Byte2FloatMap.Entry ceilingEntry(byte key) { return Byte2FloatMaps.unmodifiable(map.ceilingEntry(key)); }
		@Override
		public Byte2FloatNavigableMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Ordered Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableOrderedMap extends UnmodifyableMap implements Byte2FloatOrderedMap {
		Byte2FloatOrderedMap map;
		
		UnmodifyableOrderedMap(Byte2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putAndMoveToLast(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public boolean moveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToFirst(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public float getAndMoveToLast(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Byte2FloatOrderedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Sorted Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableSortedMap extends UnmodifyableMap implements Byte2FloatSortedMap {
		Byte2FloatSortedMap map;
		
		UnmodifyableSortedMap(Byte2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { return map.comparator(); }
		@Override
		public Byte2FloatSortedMap subMap(byte fromKey, byte toKey) { return Byte2FloatMaps.unmodifiable(map.subMap(fromKey, toKey)); }
		@Override
		public Byte2FloatSortedMap headMap(byte toKey) { return Byte2FloatMaps.unmodifiable(map.headMap(toKey)); }
		@Override
		public Byte2FloatSortedMap tailMap(byte fromKey) { return Byte2FloatMaps.unmodifiable(map.tailMap(fromKey)); }
		@Override
		public ByteSortedSet keySet() { return ByteSets.unmodifiable(map.keySet()); }

		@Override
		public byte firstByteKey() { return map.firstByteKey(); }
		@Override
		public byte pollFirstByteKey() { return map.pollFirstByteKey(); }
		@Override
		public byte lastByteKey() { return map.lastByteKey(); }
		@Override
		public byte pollLastByteKey() { return map.pollLastByteKey(); }
		@Override
		public float firstFloatValue() { return map.firstFloatValue(); }
		@Override
		public float lastFloatValue() { return map.lastFloatValue(); }
		@Override
		public Byte2FloatSortedMap copy() { return map.copy(); }
	}
	
	/**
	 * The Unmodifyable Map implementation that is sued for the unmodifyableMap function
	 */
	public static class UnmodifyableMap extends AbstractByte2FloatMap implements Byte2FloatMap {
		Byte2FloatMap map;
		FloatCollection values;
		ByteSet keys;
		ObjectSet<Byte2FloatMap.Entry> entrySet;
		
		UnmodifyableMap(Byte2FloatMap map) {
			this.map = map;
		}
		
		@Override
		public float put(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float putIfAbsent(byte key, float value){ throw new UnsupportedOperationException(); }
		@Override
		public float addTo(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float subFrom(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float remove(byte key) { throw new UnsupportedOperationException(); }
		@Override
		public float removeOrDefault(byte key, float defaultValue) { throw new UnsupportedOperationException(); }
		@Override
		public boolean remove(byte key, float value) { throw new UnsupportedOperationException(); }
		@Override
		public float get(byte key) {
			float type = map.get(key);
			return Float.floatToIntBits(type) == Float.floatToIntBits(map.getDefaultReturnValue()) && !map.containsKey(key) ? getDefaultReturnValue() : type;
		}
		@Override
		public float getOrDefault(byte key, float defaultValue) { return map.getOrDefault(key, defaultValue); }
		@Override
		public float computeFloat(byte key, ByteFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfAbsent(byte key, Byte2FloatFunction mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float computeFloatIfPresent(byte key, ByteFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public float supplyFloatIfAbsent(byte key, FloatSupplier valueProvider) { throw new UnsupportedOperationException(); }
		@Override
		public float mergeFloat(byte key, float value, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public void mergeAllFloat(Byte2FloatMap m, FloatFloatUnaryOperator mappingFunction) { throw new UnsupportedOperationException(); }
		@Override
		public Byte2FloatMap copy() { return map.copy(); }
		@Override
		public void clear() { throw new UnsupportedOperationException(); }
		
		@Override
		public ByteSet keySet() { 
			if(keys == null) keys = ByteSets.unmodifiable(map.keySet()); 
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.unmodifiable(map.values());
			return values;
		}
		
		@Override
		public ObjectSet<Byte2FloatMap.Entry> byte2FloatEntrySet() {
			if(entrySet == null) entrySet = new UnmodifyableEntrySet(map.byte2FloatEntrySet());
			return entrySet;
		}
	}
	
	/**
	 * The Unmodifyable Set implementation for the Unmodifyable Map implementation
	 */
	public static class UnmodifyableEntrySet extends ObjectSets.UnmodifiableSet<Byte2FloatMap.Entry>
	{
		ObjectSet<Byte2FloatMap.Entry> s;
		
		UnmodifyableEntrySet(ObjectSet<Byte2FloatMap.Entry> c)
		{
			super(c);
			s = c;
		}
		
		@Override
		public void forEach(Consumer<? super Byte2FloatMap.Entry> action) {
			s.forEach(T -> action.accept(Byte2FloatMaps.unmodifiable(T)));
		}
		
		@Override
		public ObjectIterator<Byte2FloatMap.Entry> iterator() {
			return new ObjectIterator<Byte2FloatMap.Entry>() {
				ObjectIterator<Byte2FloatMap.Entry> iter = s.iterator();
				@Override
				public boolean hasNext() { return iter.hasNext(); }
				@Override
				public Byte2FloatMap.Entry next() { return Byte2FloatMaps.unmodifiable(iter.next()); }
			};
		}
		
	}
	
	/**
	 * The Synchronized Navigable Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedNavigableMap extends SynchronizedSortedMap implements Byte2FloatNavigableMap {
		Byte2FloatNavigableMap map;
		
		SynchronizedNavigableMap(Byte2FloatNavigableMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedNavigableMap(Byte2FloatNavigableMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public Byte2FloatNavigableMap descendingMap() { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.descendingMap(), mutex); } }
		@Override
		public ByteNavigableSet navigableKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.navigableKeySet(), mutex); } }
		@Override
		public ByteNavigableSet descendingKeySet() { synchronized(mutex) { return ByteSets.synchronize(map.descendingKeySet(), mutex); } }
		@Override
		public ByteNavigableSet keySet() { synchronized(mutex) { return ByteSets.synchronize(map.keySet(), mutex); } }
		@Override
		public Byte2FloatMap.Entry firstEntry() { synchronized(mutex) { return map.firstEntry(); } }
		@Override
		public Byte2FloatMap.Entry lastEntry() { synchronized(mutex) { return map.lastEntry(); } }
		@Override
		public Byte2FloatMap.Entry pollFirstEntry() { synchronized(mutex) { return map.pollFirstEntry(); } }
		@Override
		public Byte2FloatMap.Entry pollLastEntry() { synchronized(mutex) { return map.pollLastEntry(); } }
		@Override
		public Byte2FloatNavigableMap subMap(byte fromKey, boolean fromInclusive, byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		public Byte2FloatNavigableMap headMap(byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		public Byte2FloatNavigableMap tailMap(byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		public Byte2FloatNavigableMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2FloatNavigableMap headMap(byte toKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2FloatNavigableMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public byte lowerKey(byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		public byte higherKey(byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		public byte floorKey(byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		public byte ceilingKey(byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		public Byte2FloatMap.Entry lowerEntry(byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		public Byte2FloatMap.Entry higherEntry(byte key) { synchronized(mutex) { return map.higherEntry(key); } }
		@Override
		public Byte2FloatMap.Entry floorEntry(byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		public Byte2FloatMap.Entry ceilingEntry(byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		public Byte2FloatNavigableMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Byte2FloatNavigableMap subMap(Byte fromKey, boolean fromInclusive, Byte toKey, boolean toInclusive) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.subMap(fromKey, fromInclusive, toKey, toInclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2FloatNavigableMap headMap(Byte toKey, boolean inclusive) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.headMap(toKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2FloatNavigableMap tailMap(Byte fromKey, boolean inclusive) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.tailMap(fromKey, inclusive), mutex); } }
		@Override
		@Deprecated
		public Byte2FloatNavigableMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2FloatNavigableMap headMap(Byte toKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2FloatNavigableMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public void setDefaultMaxValue(byte e) { synchronized(mutex) { map.setDefaultMaxValue(e); } }
		@Override
		public byte getDefaultMaxValue() { synchronized(mutex) { return map.getDefaultMaxValue(); } }
		@Override
		public void setDefaultMinValue(byte e) { synchronized(mutex) { map.setDefaultMinValue(e); } }
		@Override
		public byte getDefaultMinValue() { synchronized(mutex) { return map.getDefaultMinValue(); } }
		@Override
		@Deprecated
		public Byte lowerKey(Byte key) { synchronized(mutex) { return map.lowerKey(key); } }
		@Override
		@Deprecated
		public Byte floorKey(Byte key) { synchronized(mutex) { return map.floorKey(key); } }
		@Override
		@Deprecated
		public Byte ceilingKey(Byte key) { synchronized(mutex) { return map.ceilingKey(key); } }
		@Override
		@Deprecated
		public Byte higherKey(Byte key) { synchronized(mutex) { return map.higherKey(key); } }
		@Override
		@Deprecated
		public Byte2FloatMap.Entry lowerEntry(Byte key) { synchronized(mutex) { return map.lowerEntry(key); } }
		@Override
		@Deprecated
		public Byte2FloatMap.Entry floorEntry(Byte key) { synchronized(mutex) { return map.floorEntry(key); } }
		@Override
		@Deprecated
		public Byte2FloatMap.Entry ceilingEntry(Byte key) { synchronized(mutex) { return map.ceilingEntry(key); } }
		@Override
		@Deprecated
		public Byte2FloatMap.Entry higherEntry(Byte key) { synchronized(mutex) { return map.higherEntry(key); } }
	}
	
	/**
	 * The Synchronized Ordered Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedOrderedMap extends SynchronizedMap implements Byte2FloatOrderedMap {
		Byte2FloatOrderedMap map;
		
		SynchronizedOrderedMap(Byte2FloatOrderedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedOrderedMap(Byte2FloatOrderedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public float putAndMoveToFirst(byte key, float value) { synchronized(mutex) { return map.putAndMoveToFirst(key, value); } }
		@Override
		public float putAndMoveToLast(byte key, float value) { synchronized(mutex) { return map.putAndMoveToLast(key, value); } }
		@Override
		public boolean moveToFirst(byte key) { synchronized(mutex) { return map.moveToFirst(key); } }
		@Override
		public boolean moveToLast(byte key) { synchronized(mutex) { return map.moveToLast(key); } }
		@Override
		public float getAndMoveToFirst(byte key) { synchronized(mutex) { return map.getAndMoveToFirst(key); } }
		@Override
		public float getAndMoveToLast(byte key) { synchronized(mutex) { return map.getAndMoveToLast(key); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Byte2FloatOrderedMap copy() { synchronized(mutex) { return map.copy(); } }
	}
	
	/**
	 * The Synchronized Sorted Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedSortedMap extends SynchronizedMap implements Byte2FloatSortedMap {
		Byte2FloatSortedMap map;
		
		SynchronizedSortedMap(Byte2FloatSortedMap map) {
			super(map);
			this.map = map;
		}
		
		SynchronizedSortedMap(Byte2FloatSortedMap map, Object mutex) {
			super(map, mutex);
			this.map = map;
		}
		
		@Override
		public ByteComparator comparator() { synchronized(mutex) { return map.comparator(); } }
		@Override
		public Byte2FloatSortedMap subMap(byte fromKey, byte toKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		public Byte2FloatSortedMap headMap(byte toKey)  { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		public Byte2FloatSortedMap tailMap(byte fromKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
		@Override
		public ByteSortedSet keySet() { synchronized(mutex) { return ByteSets.synchronize(map.keySet(), mutex); } }
		@Override
		public byte firstByteKey() { synchronized(mutex) { return map.firstByteKey(); } }
		@Override
		public byte pollFirstByteKey() { synchronized(mutex) { return map.pollFirstByteKey(); } }
		@Override
		public byte lastByteKey() { synchronized(mutex) { return map.lastByteKey(); } }
		@Override
		public byte pollLastByteKey() { synchronized(mutex) { return map.pollLastByteKey(); } }
		@Override
		public float firstFloatValue() { synchronized(mutex) { return map.firstFloatValue(); } }
		@Override
		public float lastFloatValue() { synchronized(mutex) { return map.lastFloatValue(); } }
		@Override
		public Byte2FloatSortedMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		@Deprecated
		public Byte firstKey() { synchronized(mutex) { return map.firstKey(); } }
		@Override
		@Deprecated
		public Byte lastKey() { synchronized(mutex) { return map.lastKey(); } }
		@Override
		@Deprecated
		public Byte2FloatSortedMap subMap(Byte fromKey, Byte toKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.subMap(fromKey, toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2FloatSortedMap headMap(Byte toKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.headMap(toKey), mutex); } }
		@Override
		@Deprecated
		public Byte2FloatSortedMap tailMap(Byte fromKey) { synchronized(mutex) { return Byte2FloatMaps.synchronize(map.tailMap(fromKey), mutex); } }
	}
	
	/**
	 * The Synchronized Map implementation used by the synchronizedMap helper function
	 */
	public static class SynchronizedMap extends AbstractByte2FloatMap implements Byte2FloatMap {
		Byte2FloatMap map;
		FloatCollection values;
		ByteSet keys;
		ObjectSet<Byte2FloatMap.Entry> entrySet;
		
		protected Object mutex;
		
		SynchronizedMap(Byte2FloatMap map) {
			this.map = map;
			mutex = this;
		}
		
		SynchronizedMap(Byte2FloatMap map, Object mutex) {
			this.map = map;
			this.mutex = mutex;
		}
		
		@Override
		public float getDefaultReturnValue() { synchronized(mutex) { return map.getDefaultReturnValue(); } }
		@Override
		public AbstractByte2FloatMap setDefaultReturnValue(float v) {
			synchronized(mutex) {
				map.setDefaultReturnValue(v);
				return this;
			}
		}
		@Override
		public float put(byte key, float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		public float putIfAbsent(byte key, float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		public void putAllIfAbsent(Byte2FloatMap m) { synchronized(mutex) { map.putAllIfAbsent(m); } }
		@Override
		public float addTo(byte key, float value) { synchronized(mutex) { return map.addTo(key, value); } }
		@Override
		public float subFrom(byte key, float value) { synchronized(mutex) { return map.subFrom(key, value); } }
		public void addToAll(Byte2FloatMap m) { synchronized(mutex) { map.addToAll(m); } }
		@Override
		public void putAll(Byte2FloatMap m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(Map<? extends Byte, ? extends Float> m) { synchronized(mutex) { map.putAll(m); } }
		@Override
		public void putAll(byte[] keys, float[] values, int offset, int size) { synchronized(mutex) { map.putAll(keys, values, offset, size); } }
		
		@Override
		public boolean containsKey(byte key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		public boolean containsValue(float value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		public float get(byte key) { synchronized(mutex) { return map.get(key); } }
		@Override
		public float remove(byte key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		public float removeOrDefault(byte key, float defaultValue) { synchronized(mutex) { return map.removeOrDefault(key, defaultValue); } }
		@Override
		public boolean remove(byte key, float value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		public boolean replace(byte key, float oldValue, float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		public float replace(byte key, float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		public void replaceFloats(Byte2FloatMap m) { synchronized(mutex) { map.replaceFloats(m); } }
		@Override
		public void replaceFloats(ByteFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.replaceFloats(mappingFunction); } }
		@Override
		public float computeFloat(byte key, ByteFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloat(key, mappingFunction); } }
		@Override
		public float computeFloatIfAbsent(byte key, Byte2FloatFunction mappingFunction) { synchronized(mutex) { return map.computeFloatIfAbsent(key, mappingFunction); } }
		@Override
		public float computeFloatIfPresent(byte key, ByteFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.computeFloatIfPresent(key, mappingFunction); } }
		@Override
		public float supplyFloatIfAbsent(byte key, FloatSupplier valueProvider) { synchronized(mutex) { return map.supplyFloatIfAbsent(key, valueProvider); } }
		@Override
		public float mergeFloat(byte key, float value, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { return map.mergeFloat(key, value, mappingFunction); } }
		@Override
		public void mergeAllFloat(Byte2FloatMap m, FloatFloatUnaryOperator mappingFunction) { synchronized(mutex) { map.mergeAllFloat(m, mappingFunction); } }
		@Override
		public float getOrDefault(byte key, float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		public void forEach(ByteFloatConsumer action) { synchronized(mutex) { map.forEach(action); } }
		@Override
		public int size() { synchronized(mutex) { return map.size(); } }
		@Override
		public Byte2FloatMap copy() { synchronized(mutex) { return map.copy(); } }
		@Override
		public ByteSet keySet() {
			if(keys == null) keys = ByteSets.synchronize(map.keySet(), mutex);
			return keys;
		}
		
		@Override
		public FloatCollection values() {
			if(values == null) values = FloatCollections.synchronize(map.values(), mutex);
			return values;
		}

		@Override
		public ObjectSet<Byte2FloatMap.Entry> byte2FloatEntrySet() {
			if(entrySet == null) entrySet = ObjectSets.synchronize(map.byte2FloatEntrySet(), mutex);
			return entrySet;
		}
		
		@Override
		@Deprecated
		public Float get(Object key) { synchronized(mutex) { return map.get(key); } }
		@Override
		@Deprecated
		public Float getOrDefault(Object key, Float defaultValue) { synchronized(mutex) { return map.getOrDefault(key, defaultValue); } }
		@Override
		@Deprecated
		public boolean containsValue(Object value) { synchronized(mutex) { return map.containsValue(value); } }
		@Override
		@Deprecated
		public boolean containsKey(Object key) { synchronized(mutex) { return map.containsKey(key); } }
		@Override
		@Deprecated
		public Float put(Byte key, Float value) { synchronized(mutex) { return map.put(key, value); } }
		@Override
		@Deprecated
		public Float remove(Object key) { synchronized(mutex) { return map.remove(key); } }
		@Override
		@Deprecated
		public void clear() { synchronized(mutex) { map.clear(); } }
		@Override
		@Deprecated
		public Float putIfAbsent(Byte key, Float value) { synchronized(mutex) { return map.putIfAbsent(key, value); } }
		@Override
		@Deprecated
		public boolean remove(Object key, Object value) { synchronized(mutex) { return map.remove(key, value); } }
		@Override
		@Deprecated
		public boolean replace(Byte key, Float oldValue, Float newValue) { synchronized(mutex) { return map.replace(key, oldValue, newValue); } }
		@Override
		@Deprecated
		public Float replace(Byte key, Float value) { synchronized(mutex) { return map.replace(key, value); } }
		@Override
		@Deprecated
		public void replaceAll(BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { map.replaceAll(mappingFunction); } }
		@Override
		@Deprecated
		public Float compute(Byte key, BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.compute(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfAbsent(Byte key, Function<? super Byte, ? extends Float> mappingFunction) { synchronized(mutex) { return map.computeIfAbsent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float computeIfPresent(Byte key, BiFunction<? super Byte, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return computeIfPresent(key, mappingFunction); } }
		@Override
		@Deprecated
		public Float merge(Byte key, Float value, BiFunction<? super Float, ? super Float, ? extends Float> mappingFunction) { synchronized(mutex) { return map.merge(key, value, mappingFunction); } }
		@Override
		@Deprecated
		public void forEach(BiConsumer<? super Byte, ? super Float> action) { synchronized(mutex) { map.forEach(action); } }
	}
}