package speiger.src.collections.objects.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.Comparator;

import speiger.src.collections.longs.collections.LongCollection;
import speiger.src.collections.objects.functions.consumer.ObjectLongConsumer;
import speiger.src.collections.objects.functions.function.Object2LongFunction;
import speiger.src.collections.objects.functions.function.ObjectLongUnaryOperator;
import speiger.src.collections.objects.maps.impl.customHash.Object2LongLinkedOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.customHash.Object2LongOpenCustomHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2LongLinkedOpenHashMap;
import speiger.src.collections.objects.maps.impl.hash.Object2LongOpenHashMap;
import speiger.src.collections.objects.maps.impl.immutable.ImmutableObject2LongOpenHashMap;
import speiger.src.collections.objects.maps.impl.tree.Object2LongAVLTreeMap;
import speiger.src.collections.objects.maps.impl.tree.Object2LongRBTreeMap;
import speiger.src.collections.objects.maps.impl.misc.Object2LongArrayMap;
import speiger.src.collections.objects.maps.impl.concurrent.Object2LongConcurrentOpenHashMap;
import speiger.src.collections.objects.maps.impl.misc.Enum2LongMap;
import speiger.src.collections.objects.maps.impl.misc.LinkedEnum2LongMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.collections.objects.utils.maps.Object2LongMaps;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.longs.functions.function.LongLongUnaryOperator;
import speiger.src.collections.longs.functions.LongSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2LongMap<T> extends Map<T, Long>, Object2LongFunction<T>
{
	/**
	 * Helper Class that allows to create Maps without requiring a to type out the entire implementation or know it.
	 * @return a MapBuilder
	 */
	public static MapBuilder builder() {
		return MapBuilder.INSTANCE;
	}
	
	/**
	 * Method to see what the default return value is.
	 * @return default return value
	 */
	public long getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Object2LongMap<T> setDefaultReturnValue(long v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Object2LongMap<T> copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public long put(T key, long value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, long[] values) {
		if(keys.length != values.length) throw new IllegalStateException("Array sizes do not match");
		putAll(keys, values, 0, keys.length);
	}
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @param offset where the to start in the array
	 * @param size how many elements should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not within the range
	 */
	public void putAll(T[] keys, long[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(T[] keys, Long[] values) {
		if(keys.length != values.length) throw new IllegalStateException("Array sizes do not match");
		putAll(keys, values, 0, keys.length);
	}
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @param offset where the to start in the array
	 * @param size how many elements should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not within the range
	 */
	public void putAll(T[] keys, Long[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public long putIfAbsent(T key, long value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Object2LongMap<T> m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public long addTo(T key, long value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Object2LongMap<T> m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public long subFrom(T key, long value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Object2LongMap<T> m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(long value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Long && containsValue(((Long)value).longValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public long rem(T key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be T but just have to support equals with T.
	 */
	@Override
	public default Long remove(Object key) {
		return Long.valueOf(rem((T)key));
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(T key, long value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return value instanceof Long && remove((T)key, ((Long)value).longValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public long remOrDefault(T key, long defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(T key, long oldValue, long newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public long replace(T key, long value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceLongs(Object2LongMap<T> m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceLongs(ObjectLongUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public long computeLong(T key, ObjectLongUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public long computeLongIfAbsent(T key, Object2LongFunction<T> mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public long supplyLongIfAbsent(T key, LongSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public long computeLongIfPresent(T key, ObjectLongUnaryOperator<T> mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public long mergeLong(T key, long value, LongLongUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllLong(Object2LongMap<T> m, LongLongUnaryOperator mappingFunction);
	
	@Override
	public default boolean replace(T key, Long oldValue, Long newValue) {
		return replace(key, oldValue.longValue(), newValue.longValue());
	}
	
	@Override
	public default Long replace(T key, Long value) {
		return Long.valueOf(replace(key, value.longValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public long getLong(T key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public long getOrDefault(T key, long defaultValue);
	
	@Override
	public default Long get(Object key) {
		return Long.valueOf(getLong((T)key));
	}
	
	@Override
	public default Long getOrDefault(Object key, Long defaultValue) {
		Long value = Long.valueOf(getLong((T)key));
		return value != getDefaultReturnValue() || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	public default void replaceAll(BiFunction<? super T, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceLongs(mappingFunction instanceof ObjectLongUnaryOperator ? (ObjectLongUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Long.valueOf(V)).longValue());
	}
	
	@Override
	public default Long compute(T key, BiFunction<? super T, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLong(key, mappingFunction instanceof ObjectLongUnaryOperator ? (ObjectLongUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Long.valueOf(V)).longValue()));
	}
	
	@Override
	public default Long computeIfAbsent(T key, Function<? super T, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfAbsent(key, mappingFunction instanceof Object2LongFunction ? (Object2LongFunction<T>)mappingFunction : K -> mappingFunction.apply(K).longValue()));
	}
	
	@Override
	public default Long computeIfPresent(T key, BiFunction<? super T, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(computeLongIfPresent(key, mappingFunction instanceof ObjectLongUnaryOperator ? (ObjectLongUnaryOperator<T>)mappingFunction : (K, V) -> mappingFunction.apply(K, Long.valueOf(V)).longValue()));
	}
	
	@Override
	public default Long merge(T key, Long value, BiFunction<? super Long, ? super Long, ? extends Long> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Long.valueOf(mergeLong(key, value.longValue(), mappingFunction instanceof LongLongUnaryOperator ? (LongLongUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Long.valueOf(K), Long.valueOf(V)).longValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ObjectLongConsumer<T> action);
	
	@Override
	public default void forEach(BiConsumer<? super T, ? super Long> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ObjectLongConsumer ? (ObjectLongConsumer<T>)action : (K, V) -> action.accept(K, Long.valueOf(V)));
	}
	
	@Override
	public ObjectSet<T> keySet();
	@Override
	public LongCollection values();
	@Override
	public ObjectSet<Map.Entry<T, Long>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry<T>> object2LongEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	public default Object2LongMap<T> synchronize() { return Object2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	public default Object2LongMap<T> synchronize(Object mutex) { return Object2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Object2LongMaps#unmodifiable
	 */
	public default Object2LongMap<T> unmodifiable() { return Object2LongMaps.unmodifiable(this); }
	
	@Override
	public default Long put(T key, Long value) {
		return Long.valueOf(put(key, value.longValue()));
	}
	
	@Override
	public default Long putIfAbsent(T key, Long value) {
		return Long.valueOf(put(key, value.longValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface FastEntrySet<T> extends ObjectSet<Object2LongMap.Entry<T>>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Object2LongMap.Entry<T>> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Object2LongMap.Entry<T>> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 * @param <T> the type of elements maintained by this Collection
	 */
	public interface Entry<T> extends Map.Entry<T, Long>
	{
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public long getLongValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public long setValue(long value);
		@Override
		public default Long getValue() { return Long.valueOf(getLongValue()); }
		@Override
		public default Long setValue(Long value) { return Long.valueOf(setValue(value.longValue())); }
	}
	
	/**
	 * Helper class that reduces the method spam of the Map Class.
	 */
	public static final class MapBuilder
	{
		static final MapBuilder INSTANCE = new MapBuilder();
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <T> BuilderCache<T> start() {
			return new BuilderCache<T>();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder
		 */
		public <T> BuilderCache<T> start(int size) {
			return new BuilderCache<T>(size);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, long value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @param <T> the type of elements maintained by this Collection
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public <T> BuilderCache<T> put(T key, Long value) {
			return new BuilderCache<T>().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2LongOpenHashMap<T> map() {
			return new Object2LongOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2LongOpenHashMap<T> map(int size) {
			return new Object2LongOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2LongOpenHashMap<T> map(T[] keys, long[] values) {
			return new Object2LongOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2LongOpenHashMap<T> map(T[] keys, Long[] values) {
			return new Object2LongOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2LongOpenHashMap<T> map(Object2LongMap<T> map) {
			return new Object2LongOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2LongOpenHashMap<T> map(Map<? extends T, ? extends Long> map) {
			return new Object2LongOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap
		*/
		public <T> Object2LongLinkedOpenHashMap<T> linkedMap() {
			return new Object2LongLinkedOpenHashMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2LongLinkedOpenHashMap<T> linkedMap(int size) {
			return new Object2LongLinkedOpenHashMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2LongLinkedOpenHashMap<T> linkedMap(T[] keys, long[] values) {
			return new Object2LongLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2LongLinkedOpenHashMap<T> linkedMap(T[] keys, Long[] values) {
			return new Object2LongLinkedOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2LongLinkedOpenHashMap<T> linkedMap(Object2LongMap<T> map) {
			return new Object2LongLinkedOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2LongOpenHashMap<T> linkedMap(Map<? extends T, ? extends Long> map) {
			return new ImmutableObject2LongOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public <T> ImmutableObject2LongOpenHashMap<T> immutable(T[] keys, long[] values) {
			return new ImmutableObject2LongOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> ImmutableObject2LongOpenHashMap<T> immutable(T[] keys, Long[] values) {
			return new ImmutableObject2LongOpenHashMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public <T> ImmutableObject2LongOpenHashMap<T> immutable(Object2LongMap<T> map) {
			return new ImmutableObject2LongOpenHashMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> ImmutableObject2LongOpenHashMap<T> immutable(Map<? extends T, ? extends Long> map) {
			return new ImmutableObject2LongOpenHashMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty EnumMap
		 */
		public <T extends Enum<T>> Enum2LongMap<T> enumMap(Class<T> keyType) {
			 return new Enum2LongMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> Enum2LongMap<T> enumMap(T[] keys, Long[] values) {
			return new Enum2LongMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a EnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> Enum2LongMap<T> enumMap(T[] keys, long[] values) {
			return new Enum2LongMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a EnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> Enum2LongMap<T> enumMap(Map<? extends T, ? extends Long> map) {
			return new Enum2LongMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a EnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> Enum2LongMap<T> enumMap(Object2LongMap<T> map) {
			return new Enum2LongMap<>(map);
		}
		
		/**
		 * Helper function to unify code
		 * @param keyType the EnumClass that should be used
		 * @param <T> the type of elements maintained by this Collection
		 * @return a Empty LinkedEnumMap
		 */
		public <T extends Enum<T>> LinkedEnum2LongMap<T> linkedEnumMap(Class<T> keyType) {
			 return new LinkedEnum2LongMap<>(keyType);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 * @note the keys and values will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2LongMap<T> linkedEnumMap(T[] keys, Long[] values) {
			return new LinkedEnum2LongMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param keys the keys that should be inserted
		 * @param values the values that should be inserted
		 * @param <T> the type of elements maintained by this Collection
		 * @throws IllegalStateException if the keys and values do not match in length
		 * @throws IllegalArgumentException if the keys are in length 0
		 * @return a LinkedEnumMap thats contains the injected values
		 */
		public <T extends Enum<T>> LinkedEnum2LongMap<T> linkedEnumMap(T[] keys, long[] values) {
			return new LinkedEnum2LongMap<>(keys, values);
		}
		
		/**
		 * Helper function to unify code
		 * @param map that should be cloned
		 * @param <T> the type of elements maintained by this Collection
		 * @return a LinkedEnumMap thats copies the contents of the provided map
		 * @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		 * @note the map will be unboxed
		 */
		public <T extends Enum<T>> LinkedEnum2LongMap<T> linkedEnumMap(Map<? extends T, ? extends Long> map) {
			return new LinkedEnum2LongMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalArgumentException if the map is Empty and is not a EnumMap
		* @return a LinkedEnumMap thats copies the contents of the provided map
		*/
		public <T extends Enum<T>> LinkedEnum2LongMap<T> linkedEnumMap(Object2LongMap<T> map) {
			return new LinkedEnum2LongMap<>(map);
		}
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap
		*/
		public <T> Object2LongOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return new Object2LongOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public <T> Object2LongOpenCustomHashMap<T> customMap(int size, ObjectStrategy<T> strategy) {
			return new Object2LongOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public <T> Object2LongOpenCustomHashMap<T> customMap(T[] keys, long[] values, ObjectStrategy<T> strategy) {
			return new Object2LongOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2LongOpenCustomHashMap<T> customMap(T[] keys, Long[] values, ObjectStrategy<T> strategy) {
			return new Object2LongOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2LongOpenCustomHashMap<T> customMap(Object2LongMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2LongOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2LongOpenCustomHashMap<T> customMap(Map<? extends T, ? extends Long> map, ObjectStrategy<T> strategy) {
			return new Object2LongOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap
		*/
		public <T> Object2LongLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return new Object2LongLinkedOpenCustomHashMap<>(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public <T> Object2LongLinkedOpenCustomHashMap<T> customLinkedMap(int size, ObjectStrategy<T> strategy) {
			return new Object2LongLinkedOpenCustomHashMap<>(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public <T> Object2LongLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, long[] values, ObjectStrategy<T> strategy) {
			return new Object2LongLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2LongLinkedOpenCustomHashMap<T> customLinkedMap(T[] keys, Long[] values, ObjectStrategy<T> strategy) {
			return new Object2LongLinkedOpenCustomHashMap<>(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2LongLinkedOpenCustomHashMap<T> customLinkedMap(Object2LongMap<T> map, ObjectStrategy<T> strategy) {
			return new Object2LongLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @param <T> the type of elements maintained by this Collection
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2LongLinkedOpenCustomHashMap<T> customLinkedMap(Map<? extends T, ? extends Long> map, ObjectStrategy<T> strategy) {
			return new Object2LongLinkedOpenCustomHashMap<>(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap
		*/
		public <T> Object2LongArrayMap<T> arrayMap() {
			return new Object2LongArrayMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap with a mimimum capacity
		*/
		public <T> Object2LongArrayMap<T> arrayMap(int size) {
			return new Object2LongArrayMap<>(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public <T> Object2LongArrayMap<T> arrayMap(T[] keys, long[] values) {
			return new Object2LongArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2LongArrayMap<T> arrayMap(T[] keys, Long[] values) {
			return new Object2LongArrayMap<>(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public <T> Object2LongArrayMap<T> arrayMap(Object2LongMap<T> map) {
			return new Object2LongArrayMap<>(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param <T> the type of elements maintained by this Collection
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2LongArrayMap<T> arrayMap(Map<? extends T, ? extends Long> map) {
			return new Object2LongArrayMap<>(map);
		}
		
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2LongRBTreeMap<T> rbTreeMap() {
			return new Object2LongRBTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap
		*/
		public <T> Object2LongRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return new Object2LongRBTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public <T> Object2LongRBTreeMap<T> rbTreeMap(T[] keys, long[] values, Comparator<T> comp) {
			return new Object2LongRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2LongRBTreeMap<T> rbTreeMap(T[] keys, Long[] values, Comparator<T> comp) {
			return new Object2LongRBTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2LongRBTreeMap<T> rbTreeMap(Object2LongMap<T> map, Comparator<T> comp) {
			return new Object2LongRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2LongRBTreeMap<T> rbTreeMap(Map<? extends T, ? extends Long> map, Comparator<T> comp) {
			return new Object2LongRBTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2LongAVLTreeMap<T> avlTreeMap() {
			return new Object2LongAVLTreeMap<>();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap
		*/
		public <T> Object2LongAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return new Object2LongAVLTreeMap<>(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public <T> Object2LongAVLTreeMap<T> avlTreeMap(T[] keys, long[] values, Comparator<T> comp) {
			return new Object2LongAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public <T> Object2LongAVLTreeMap<T> avlTreeMap(T[] keys, Long[] values, Comparator<T> comp) {
			return new Object2LongAVLTreeMap<>(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public <T> Object2LongAVLTreeMap<T> avlTreeMap(Object2LongMap<T> map, Comparator<T> comp) {
			return new Object2LongAVLTreeMap<>(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @param <T> the type of elements maintained by this Collection
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public <T> Object2LongAVLTreeMap<T> avlTreeMap(Map<? extends T, ? extends Long> map, Comparator<T> comp) {
			return new Object2LongAVLTreeMap<>(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 * @param <T> the type of elements maintained by this Collection
	 */
	public static class BuilderCache<T>
	{
		T[] keys;
		long[] values;
		int size;
		
		/**
		 * Default Constructor
		 */
		public BuilderCache() {
			this(HashUtil.DEFAULT_MIN_CAPACITY);
		}
		
		/**
		 * Constructor providing a Minimum Capcity
		 * @param initialSize the requested start capacity
		 */
		public BuilderCache(int initialSize) {
			if(initialSize < 0) throw new IllegalStateException("Minimum Capacity is negative. This is not allowed");
			keys = (T[])new Object[initialSize];
			values = new long[initialSize];
		}
		
		private void ensureSize(int newSize) {
			if(keys.length >= newSize) return;
			newSize = (int)Math.max(Math.min((long)keys.length + (keys.length >> 1), SanityChecks.MAX_ARRAY_SIZE), newSize);
			keys = Arrays.copyOf(keys, newSize);
			values = Arrays.copyOf(values, newSize);
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return self
		 */
		public BuilderCache<T> put(T key, long value) {
			ensureSize(size+1);
			keys[size] = key;
			values[size] = value;
			size++;
			return this;
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return self
		 */
		public BuilderCache<T> put(T key, Long value) {
			return put(key, value.longValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache<T> put(Object2LongMap.Entry<T> entry) {
			return put(entry.getKey(), entry.getLongValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Object2LongMap<T> map) {
			return putAll(Object2LongMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(Map<? extends T, ? extends Long> map) {
			for(Map.Entry<? extends T, ? extends Long> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache<T> putAll(ObjectIterable<Object2LongMap.Entry<T>> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Object2LongMap.Entry<T>>)c).size());
			
			for(Object2LongMap.Entry<T> entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Object2LongMap<T>> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Object2LongOpenHashMap
		 */
		public Object2LongOpenHashMap<T> map() {
			return putElements(new Object2LongOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Object2LongLinkedOpenHashMap
		 */
		public Object2LongLinkedOpenHashMap<T> linkedMap() {
			return putElements(new Object2LongLinkedOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableObject2LongOpenHashMap
		 */
		public ImmutableObject2LongOpenHashMap<T> immutable() {
			return new ImmutableObject2LongOpenHashMap<>(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2LongOpenCustomHashMap
		 */
		public Object2LongOpenCustomHashMap<T> customMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2LongOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Object2LongLinkedOpenCustomHashMap
		 */
		public Object2LongLinkedOpenCustomHashMap<T> customLinkedMap(ObjectStrategy<T> strategy) {
			return putElements(new Object2LongLinkedOpenCustomHashMap<>(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Object2LongConcurrentOpenHashMap
		 */
		public Object2LongConcurrentOpenHashMap<T> concurrentMap() {
			return putElements(new Object2LongConcurrentOpenHashMap<>(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Object2LongArrayMap
		 */
		public Object2LongArrayMap<T> arrayMap() {
			return new Object2LongArrayMap<>(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Object2LongRBTreeMap
		 */
		public Object2LongRBTreeMap<T> rbTreeMap() {
			return putElements(new Object2LongRBTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2LongRBTreeMap
		 */
		public Object2LongRBTreeMap<T> rbTreeMap(Comparator<T> comp) {
			return putElements(new Object2LongRBTreeMap<>(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Object2LongAVLTreeMap
		 */
		public Object2LongAVLTreeMap<T> avlTreeMap() {
			return putElements(new Object2LongAVLTreeMap<>());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Object2LongAVLTreeMap
		 */
		public Object2LongAVLTreeMap<T> avlTreeMap(Comparator<T> comp) {
			return putElements(new Object2LongAVLTreeMap<>(comp));
		}
	}
}