package speiger.src.collections.shorts.maps.interfaces;

import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;


import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.shorts.functions.consumer.ShortDoubleConsumer;
import speiger.src.collections.shorts.functions.function.Short2DoubleFunction;
import speiger.src.collections.shorts.functions.function.ShortDoubleUnaryOperator;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.maps.impl.customHash.Short2DoubleLinkedOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.customHash.Short2DoubleOpenCustomHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2DoubleLinkedOpenHashMap;
import speiger.src.collections.shorts.maps.impl.hash.Short2DoubleOpenHashMap;
import speiger.src.collections.shorts.maps.impl.immutable.ImmutableShort2DoubleOpenHashMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2DoubleAVLTreeMap;
import speiger.src.collections.shorts.maps.impl.tree.Short2DoubleRBTreeMap;
import speiger.src.collections.shorts.maps.impl.misc.Short2DoubleArrayMap;
import speiger.src.collections.shorts.maps.impl.concurrent.Short2DoubleConcurrentOpenHashMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.shorts.utils.ShortStrategy;
import speiger.src.collections.shorts.utils.maps.Short2DoubleMaps;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.doubles.functions.function.DoubleDoubleUnaryOperator;
import speiger.src.collections.doubles.functions.DoubleSupplier;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.utils.HashUtil;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Type Specific Map that reduces memory overhead due to boxing/unboxing of Primitives
 * and some extra helper functions.
 */
public interface Short2DoubleMap extends Map<Short, Double>, Short2DoubleFunction
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
	public double getDefaultReturnValue();
	/**
	 * Method to define the default return value if a requested key isn't present
	 * @param v value that should be the default return value
	 * @return itself
	 */
	public Short2DoubleMap setDefaultReturnValue(double v);
	
	/**
	 * A Function that does a shallow clone of the Map itself.
	 * This function is more optimized then a copy constructor since the Map does not have to be unsorted/resorted.
	 * It can be compared to Cloneable but with less exception risk
	 * @return a Shallow Copy of the Map
	 * @note Wrappers and view Maps will not support this feature
	 */
	public Short2DoubleMap copy();
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#put(Object, Object)
	 */
	public double put(short key, double value);
	
	/**
	 * Type Specific array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(short[] keys, double[] values) {
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
	public void putAll(short[] keys, double[] values, int offset, int size);
	
	/**
	 * Type Specific Object array method to bulk add elements into a map without creating a wrapper and increasing performances
	 * @param keys the keys that should be added
	 * @param values the values that should be added
	 * @see Map#putAll(Map)
	 * @throws IllegalStateException if the arrays are not the same size
	 */
	public default void putAll(Short[] keys, Double[] values) {
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
	public void putAll(Short[] keys, Double[] values, int offset, int size);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted
	 * @return the last present value or default return value.
	 * @see Map#putIfAbsent(Object, Object)
	 */
	public double putIfAbsent(short key, double value);
	
	/**
	 * Type-Specific bulk put method put elements into the map if not present.
	 * @param m elements that should be added if not present.
	 */
	public void putAllIfAbsent(Short2DoubleMap m);
	
	/**
	 * A Helper method to add a primitives together. If key is not present then this functions as a put.
	 * @param key the key that should be inserted,
	 * @param value the value that should be inserted / added
	 * @return the last present value or default return value.
	 */
	public double addTo(short key, double value);
	
	/**
	 * A Helper method to bulk add primitives together.
	 * @param m the values that should be added/inserted
	 */
	public void addToAll(Short2DoubleMap m);
	
	/**
	 * A Helper method to subtract from primitive from each other. If the key is not present it will just return the defaultValue
	 * How the implementation works is that it will subtract from the current value (if not present it will do nothing) and fence it to the {@link #getDefaultReturnValue()}
	 * If the fence is reached the element will be automaticall removed
	 * 
	 * @param key that should be subtract from
	 * @param value that should be subtract
	 * @return the last present or default return value
	 */
	public double subFrom(short key, double value);
	
	/**
	 * Type Specific function for the bull putting of values
	 * @param m the elements that should be inserted
	 */
	public void putAll(Short2DoubleMap m);
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param key element that is searched for
	 * @return if the key is present
	 */
	public boolean containsKey(short key);
	
	/**
	 * @see Map#containsKey(Object)
	 * @param key that is searched for.
	 * @return true if found
	 * @note in some implementations key does not have to be Short but just have to support equals with Short.
	 */
	@Override
	public default boolean containsKey(Object key) {
		return key instanceof Short && containsKey(((Short)key).shortValue());
	}
	
	/**
	 * Type Specific method to reduce boxing/unboxing of values
	 * @param value element that is searched for
	 * @return if the value is present
	 */
	public boolean containsValue(double value);
	
	/**
 	* @see Map#containsValue(Object)
 	* @param value that is searched for.
 	* @return true if found
 	* @note in some implementations key does not have to be CLASS_VALUE but just have to support equals with CLASS_VALUE.
 	*/
	@Override
	public default boolean containsValue(Object value) {
		return value instanceof Double && containsValue(((Double)value).doubleValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 */
	public double remove(short key);
	
	/**
	 * @see Map#remove(Object)
	 * @param key the element that should be removed
	 * @return the value that was removed or default return value
	 * @note in some implementations key does not have to be Short but just have to support equals with Short.
	 */
	@Override
	public default Double remove(Object key) {
		return key instanceof Short ? Double.valueOf(remove(((Short)key).shortValue())) : Double.valueOf(getDefaultReturnValue());
	}
	
	/**
	 * Type Specific remove function to reduce boxing/unboxing
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
	 * @see Map#remove(Object, Object)
	 */
	public boolean remove(short key, double value);
	
	/**
 	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param value the expected value that should be found
 	 * @return true if the key and value was found and removed
 	 */
	@Override
	public default boolean remove(Object key, Object value) {
		return key instanceof Short && value instanceof Double && remove(((Short)key).shortValue(), ((Double)value).doubleValue());
	}
	
	/**
	 * Type-Specific Remove function with a default return value if wanted.
	 * @see Map#remove(Object, Object)
 	 * @param key the element that should be removed
 	 * @param defaultValue the value that should be returned if the entry doesn't exist
	 * @return the value that was removed or default value
	 */
	public double removeOrDefault(short key, double defaultValue);
	/**
	 * A Type Specific replace method to replace an existing value
	 * @param key the element that should be searched for
	 * @param oldValue the expected value to be replaced
	 * @param newValue the value to replace the oldValue with.
	 * @return true if the value got replaced
	 * @note this fails if the value is not present even if it matches the oldValue
	 */
	public boolean replace(short key, double oldValue, double newValue);
	/**
	 * A Type Specific replace method to reduce boxing/unboxing replace an existing value
	 * @param key the element that should be searched for
	 * @param value the value to replace with.
	 * @return the present value or default return value
	 * @note this fails if the value is not present
	 */
	public double replace(short key, double value);
	
	/**
	 * Type-Specific bulk replace method. Could be seen as putAllIfPresent
	 * @param m elements that should be replaced.
	 */
	public void replaceDoubles(Short2DoubleMap m);
	/**
	 * A Type Specific mass replace method to reduce boxing/unboxing
	 * @param mappingFunction operation to replace all values
	 */
	public void replaceDoubles(ShortDoubleUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value
	 * @return the result of the computation
	 */
	public double computeDouble(short key, ShortDoubleUnaryOperator mappingFunction);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if not present
	 * @return the result of the computed value or present value
	 */
	public double computeDoubleIfAbsent(short key, Short2DoubleFunction mappingFunction);
	
	/**
	 * A Supplier based computeIfAbsent function to fill the most used usecase of this function
	 * @param key the key that should be computed
	 * @param valueProvider the value if not present
	 * @return the result of the computed value or present value
	 */	
	public double supplyDoubleIfAbsent(short key, DoubleSupplier valueProvider);
	/**
	 * A Type Specific compute method to reduce boxing/unboxing
	 * @param key the key that should be computed
	 * @param mappingFunction the operator that should generate the value if present
	 * @return the result of the default return value or present value
	 * @note if not present then compute is not executed
	 */
	public double computeDoubleIfPresent(short key, ShortDoubleUnaryOperator mappingFunction);
	/**
	 * A Type Specific merge method to reduce boxing/unboxing
	 * @param key the key that should be be searched for
	 * @param value the value that should be merged with
	 * @param mappingFunction the operator that should generate the new Value
	 * @return the result of the merge
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public double mergeDouble(short key, double value, DoubleDoubleUnaryOperator mappingFunction);
	/**
	 * A Bulk method for merging Maps.
	 * @param m the entries that should be bulk added
	 * @param mappingFunction the operator that should generate the new Value
	 * @note if the result matches the default return value then the key is removed from the map
	 */
	public void mergeAllDouble(Short2DoubleMap m, DoubleDoubleUnaryOperator mappingFunction);
	
	@Override
	@Deprecated
	public default boolean replace(Short key, Double oldValue, Double newValue) {
		return replace(key.shortValue(), oldValue.doubleValue(), newValue.doubleValue());
	}
	
	@Override
	@Deprecated
	public default Double replace(Short key, Double value) {
		return Double.valueOf(replace(key.shortValue(), value.doubleValue()));
	}
	
	/**
	 * A Type Specific get method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @return the searched value or default return value
	 */
	@Override
	public double get(short key);
	/**
	 * A Type Specific getOrDefault method to reduce boxing/unboxing
	 * @param key the key that is searched for
	 * @param defaultValue the value that should be returned if the key is not present
	 * @return the searched value or defaultValue value
	 */
	public double getOrDefault(short key, double defaultValue);
	
	@Override
	@Deprecated
	public default Double get(Object key) {
		return Double.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
	}
	
	@Override
	@Deprecated
	public default Double getOrDefault(Object key, Double defaultValue) {
		Double value = Double.valueOf(key instanceof Short ? get(((Short)key).shortValue()) : getDefaultReturnValue());
		return Double.doubleToLongBits(value) != Double.doubleToLongBits(getDefaultReturnValue()) || containsKey(key) ? value : defaultValue;
	}
	
	@Override
	@Deprecated
	public default void replaceAll(BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		replaceDoubles(mappingFunction instanceof ShortDoubleUnaryOperator ? (ShortDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Double.valueOf(V)).doubleValue());
	}
	
	@Override
	@Deprecated
	public default Double compute(Short key, BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(computeDouble(key.shortValue(), mappingFunction instanceof ShortDoubleUnaryOperator ? (ShortDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Double.valueOf(V)).doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double computeIfAbsent(Short key, Function<? super Short, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(computeDoubleIfAbsent(key.shortValue(), mappingFunction instanceof Short2DoubleFunction ? (Short2DoubleFunction)mappingFunction : K -> mappingFunction.apply(Short.valueOf(K)).doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double computeIfPresent(Short key, BiFunction<? super Short, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(computeDoubleIfPresent(key.shortValue(), mappingFunction instanceof ShortDoubleUnaryOperator ? (ShortDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Short.valueOf(K), Double.valueOf(V)).doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double merge(Short key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> mappingFunction) {
		Objects.requireNonNull(mappingFunction);
		return Double.valueOf(mergeDouble(key.shortValue(), value.doubleValue(), mappingFunction instanceof DoubleDoubleUnaryOperator ? (DoubleDoubleUnaryOperator)mappingFunction : (K, V) -> mappingFunction.apply(Double.valueOf(K), Double.valueOf(V)).doubleValue()));
	}
	
	/**
	 * Type Specific forEach method to reduce boxing/unboxing
	 * @param action processor of the values that are iterator over
	 */
	public void forEach(ShortDoubleConsumer action);
	
	@Override
	@Deprecated
	public default void forEach(BiConsumer<? super Short, ? super Double> action) {
		Objects.requireNonNull(action);
		forEach(action instanceof ShortDoubleConsumer ? (ShortDoubleConsumer)action : (K, V) -> action.accept(Short.valueOf(K), Double.valueOf(V)));
	}
	
	@Override
	public ShortSet keySet();
	@Override
	public DoubleCollection values();
	@Override
	@Deprecated
	public ObjectSet<Map.Entry<Short, Double>> entrySet();
	/**
	 * Type Sensitive EntrySet to reduce boxing/unboxing and optionally Temp Object Allocation.
	 * @return a EntrySet of the collection
	 */
	public ObjectSet<Entry> short2DoubleEntrySet();
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @return a new Map that is synchronized
	 * @see Short2DoubleMaps#synchronize
	 */
	public default Short2DoubleMap synchronize() { return Short2DoubleMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped Map that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new Map Wrapper that is synchronized
	 * @see Short2DoubleMaps#synchronize
	 */
	public default Short2DoubleMap synchronize(Object mutex) { return Short2DoubleMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped Map that is unmodifiable
	 * @return a new Map Wrapper that is unmodifiable
	 * @see Short2DoubleMaps#unmodifiable
	 */
	public default Short2DoubleMap unmodifiable() { return Short2DoubleMaps.unmodifiable(this); }
	
	@Override
	@Deprecated
	public default Double put(Short key, Double value) {
		return Double.valueOf(put(key.shortValue(), value.doubleValue()));
	}
	
	@Override
	@Deprecated
	public default Double putIfAbsent(Short key, Double value) {
		return Double.valueOf(put(key.shortValue(), value.doubleValue()));
	}
	/**
	 * Fast Entry set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	public interface FastEntrySet extends ObjectSet<Short2DoubleMap.Entry>
	{
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @return a Recycling ObjectIterator of the given set
		 */
		public ObjectIterator<Short2DoubleMap.Entry> fastIterator();
		/**
		 * Fast for each that recycles the given Entry object to improve speed and reduce object allocation
		 * @param action the action that should be applied to each given entry
		 */
		public default void fastForEach(Consumer<? super Short2DoubleMap.Entry> action) {
			forEach(action);
		}
	}
	
	/**
	 * Type Specific Map Entry that reduces boxing/unboxing
	 */
	public interface Entry extends Map.Entry<Short, Double>
	{
		/**
		 * Type Specific getKey method that reduces boxing/unboxing
		 * @return the key of a given Entry
		 */
		public short getShortKey();
		public default Short getKey() { return Short.valueOf(getShortKey()); }
		
		/**
		 * Type Specific getValue method that reduces boxing/unboxing
		 * @return the value of a given Entry
		 */
		public double getDoubleValue();
		/**
		 * Type Specific setValue method that reduces boxing/unboxing
		 * @param value the new Value that should be placed in the given entry
		 * @return the old value of a given entry
		 * @throws UnsupportedOperationException if the Entry is immutable or not supported
		 */
		public double setValue(double value);
		@Override
		public default Double getValue() { return Double.valueOf(getDoubleValue()); }
		@Override
		public default Double setValue(Double value) { return Double.valueOf(setValue(value.doubleValue())); }
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
		 * @return a MapBuilder
		 */
		public BuilderCache start() {
			return new BuilderCache();
		}
		
		/**
		 * Starts a Map Builder that allows you to create maps as Constants a lot easier
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param size the expected minimum size of Elements in the Map, default is 16
		 * @return a MapBuilder
		 */
		public BuilderCache start(int size) {
			return new BuilderCache(size);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(short key, double value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		 * Starts a Map builder and puts in the Key and Value into it
		 * Keys and Values are stored as Array and then inserted using the putAllMethod when the mapType is choosen
		 * @param key the key that should be added
		 * @param value the value that should be added
		 * @return a MapBuilder with the key and value stored in it.
		 */
		public BuilderCache put(Short key, Double value) {
			return new BuilderCache().put(key, value);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Short2DoubleOpenHashMap map() {
			return new Short2DoubleOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Short2DoubleOpenHashMap map(int size) {
			return new Short2DoubleOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Short2DoubleOpenHashMap map(short[] keys, double[] values) {
			return new Short2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2DoubleOpenHashMap map(Short[] keys, Double[] values) {
			return new Short2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Short2DoubleOpenHashMap map(Short2DoubleMap map) {
			return new Short2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2DoubleOpenHashMap map(Map<? extends Short, ? extends Double> map) {
			return new Short2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @return a LinkedOpenHashMap
		*/
		public Short2DoubleLinkedOpenHashMap linkedMap() {
			return new Short2DoubleLinkedOpenHashMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a LinkedOpenHashMap with a mimimum capacity
		*/
		public Short2DoubleLinkedOpenHashMap linkedMap(int size) {
			return new Short2DoubleLinkedOpenHashMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		*/
		public Short2DoubleLinkedOpenHashMap linkedMap(short[] keys, double[] values) {
			return new Short2DoubleLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a LinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2DoubleLinkedOpenHashMap linkedMap(Short[] keys, Double[] values) {
			return new Short2DoubleLinkedOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Short2DoubleLinkedOpenHashMap linkedMap(Short2DoubleMap map) {
			return new Short2DoubleLinkedOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a LinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableShort2DoubleOpenHashMap linkedMap(Map<? extends Short, ? extends Double> map) {
			return new ImmutableShort2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		*/
		public ImmutableShort2DoubleOpenHashMap immutable(short[] keys, double[] values) {
			return new ImmutableShort2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a ImmutableOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public ImmutableShort2DoubleOpenHashMap immutable(Short[] keys, Double[] values) {
			return new ImmutableShort2DoubleOpenHashMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		*/
		public ImmutableShort2DoubleOpenHashMap immutable(Short2DoubleMap map) {
			return new ImmutableShort2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a ImmutableOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public ImmutableShort2DoubleOpenHashMap immutable(Map<? extends Short, ? extends Double> map) {
			return new ImmutableShort2DoubleOpenHashMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap
		*/
		public Short2DoubleOpenCustomHashMap customMap(ShortStrategy strategy) {
			return new Short2DoubleOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap with a mimimum capacity
		*/
		public Short2DoubleOpenCustomHashMap customMap(int size, ShortStrategy strategy) {
			return new Short2DoubleOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		*/
		public Short2DoubleOpenCustomHashMap customMap(short[] keys, double[] values, ShortStrategy strategy) {
			return new Short2DoubleOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2DoubleOpenCustomHashMap customMap(Short[] keys, Double[] values, ShortStrategy strategy) {
			return new Short2DoubleOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		*/
		public Short2DoubleOpenCustomHashMap customMap(Short2DoubleMap map, ShortStrategy strategy) {
			return new Short2DoubleOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2DoubleOpenCustomHashMap customMap(Map<? extends Short, ? extends Double> map, ShortStrategy strategy) {
			return new Short2DoubleOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap
		*/
		public Short2DoubleLinkedOpenCustomHashMap customLinkedMap(ShortStrategy strategy) {
			return new Short2DoubleLinkedOpenCustomHashMap(strategy);
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap with a mimimum capacity
		*/
		public Short2DoubleLinkedOpenCustomHashMap customLinkedMap(int size, ShortStrategy strategy) {
			return new Short2DoubleLinkedOpenCustomHashMap(size, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		*/
		public Short2DoubleLinkedOpenCustomHashMap customLinkedMap(short[] keys, double[] values, ShortStrategy strategy) {
			return new Short2DoubleLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param strategy the Hash Controller
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a CustomLinkedOpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2DoubleLinkedOpenCustomHashMap customLinkedMap(Short[] keys, Double[] values, ShortStrategy strategy) {
			return new Short2DoubleLinkedOpenCustomHashMap(keys, values, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		*/
		public Short2DoubleLinkedOpenCustomHashMap customLinkedMap(Short2DoubleMap map, ShortStrategy strategy) {
			return new Short2DoubleLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param strategy the Hash Controller
		* @return a CustomLinkedOpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2DoubleLinkedOpenCustomHashMap customLinkedMap(Map<? extends Short, ? extends Double> map, ShortStrategy strategy) {
			return new Short2DoubleLinkedOpenCustomHashMap(map, strategy);
		}
		
		/**
		* Helper function to unify code
		* @return a OpenHashMap
		*/
		public Short2DoubleArrayMap arrayMap() {
			return new Short2DoubleArrayMap();
		}
		
		/**
		* Helper function to unify code
		* @param size the minimum capacity of the Map
		* @return a OpenHashMap with a mimimum capacity
		*/
		public Short2DoubleArrayMap arrayMap(int size) {
			return new Short2DoubleArrayMap(size);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		*/
		public Short2DoubleArrayMap arrayMap(short[] keys, double[] values) {
			return new Short2DoubleArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a OpenHashMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2DoubleArrayMap arrayMap(Short[] keys, Double[] values) {
			return new Short2DoubleArrayMap(keys, values);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		*/
		public Short2DoubleArrayMap arrayMap(Short2DoubleMap map) {
			return new Short2DoubleArrayMap(map);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @return a OpenHashMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2DoubleArrayMap arrayMap(Map<? extends Short, ? extends Double> map) {
			return new Short2DoubleArrayMap(map);
		}
		
		
		/**
		* Helper function to unify code
		* @return a RBTreeMap
		*/
		public Short2DoubleRBTreeMap rbTreeMap() {
			return new Short2DoubleRBTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap
		*/
		public Short2DoubleRBTreeMap rbTreeMap(ShortComparator comp) {
			return new Short2DoubleRBTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		*/
		public Short2DoubleRBTreeMap rbTreeMap(short[] keys, double[] values, ShortComparator comp) {
			return new Short2DoubleRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a RBTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2DoubleRBTreeMap rbTreeMap(Short[] keys, Double[] values, ShortComparator comp) {
			return new Short2DoubleRBTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		*/
		public Short2DoubleRBTreeMap rbTreeMap(Short2DoubleMap map, ShortComparator comp) {
			return new Short2DoubleRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a RBTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2DoubleRBTreeMap rbTreeMap(Map<? extends Short, ? extends Double> map, ShortComparator comp) {
			return new Short2DoubleRBTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @return a AVLTreeMap
		*/
		public Short2DoubleAVLTreeMap avlTreeMap() {
			return new Short2DoubleAVLTreeMap();
		}
		
		/**
		* Helper function to unify code
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap
		*/
		public Short2DoubleAVLTreeMap avlTreeMap(ShortComparator comp) {
			return new Short2DoubleAVLTreeMap(comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		*/
		public Short2DoubleAVLTreeMap avlTreeMap(short[] keys, double[] values, ShortComparator comp) {
			return new Short2DoubleAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param keys the keys that should be inserted
		* @param values the values that should be inserted
		* @param comp the Sorter of the TreeMap
		* @throws IllegalStateException if the keys and values do not match in length
		* @return a AVLTreeMap thats contains the injected values
		* @note the keys and values will be unboxed
		*/
		public Short2DoubleAVLTreeMap avlTreeMap(Short[] keys, Double[] values, ShortComparator comp) {
			return new Short2DoubleAVLTreeMap(keys, values, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		*/
		public Short2DoubleAVLTreeMap avlTreeMap(Short2DoubleMap map, ShortComparator comp) {
			return new Short2DoubleAVLTreeMap(map, comp);
		}
		
		/**
		* Helper function to unify code
		* @param map that should be cloned
		* @param comp the Sorter of the TreeMap
		* @return a AVLTreeMap thats copies the contents of the provided map
		* @note the map will be unboxed
		*/
		public Short2DoubleAVLTreeMap avlTreeMap(Map<? extends Short, ? extends Double> map, ShortComparator comp) {
			return new Short2DoubleAVLTreeMap(map, comp);
		}
	}
	
	/**
	 * Builder Cache for allowing to buildMaps
	 */
	public static class BuilderCache
	{
		short[] keys;
		double[] values;
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
			keys = new short[initialSize];
			values = new double[initialSize];
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
		public BuilderCache put(short key, double value) {
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
		public BuilderCache put(Short key, Double value) {
			return put(key.shortValue(), value.doubleValue());
		}
		
		/**
		 * Helper function to add a Entry into the Map
		 * @param entry the Entry that should be added
		 * @return self
		 */
		public BuilderCache put(Short2DoubleMap.Entry entry) {
			return put(entry.getShortKey(), entry.getDoubleValue());
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Short2DoubleMap map) {
			return putAll(Short2DoubleMaps.fastIterable(map));
		}
		
		/**
		 * Helper function to add a Map to the Map
		 * @param map that should be added
		 * @return self
		 */
		public BuilderCache putAll(Map<? extends Short, ? extends Double> map) {
			for(Map.Entry<? extends Short, ? extends Double> entry : map.entrySet())
				put(entry.getKey(), entry.getValue());
			return this;
		}
		
		/**
		 * Helper function to add a Collection of Entries to the Map
		 * @param c that should be added
		 * @return self
		 */
		public BuilderCache putAll(ObjectIterable<Short2DoubleMap.Entry> c) {
			if(c instanceof Collection)
				ensureSize(size+((Collection<Short2DoubleMap.Entry>)c).size());
			
			for(Short2DoubleMap.Entry entry : c) 
				put(entry);
			
			return this;
		}
		
		private <E extends Short2DoubleMap> E putElements(E e){
			e.putAll(keys, values, 0, size);
			return e;
		}
		
		/**
		 * Builds the Keys and Values into a Hash Map
		 * @return a Short2DoubleOpenHashMap
		 */
		public Short2DoubleOpenHashMap map() {
			return putElements(new Short2DoubleOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Hash Map
		 * @return a Short2DoubleLinkedOpenHashMap
		 */
		public Short2DoubleLinkedOpenHashMap linkedMap() {
			return putElements(new Short2DoubleLinkedOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Immutable Hash Map
		 * @return a ImmutableShort2DoubleOpenHashMap
		 */
		public ImmutableShort2DoubleOpenHashMap immutable() {
			return new ImmutableShort2DoubleOpenHashMap(Arrays.copyOf(keys, size), Arrays.copyOf(values, size));
		}
		
		/**
		 * Builds the Keys and Values into a Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2DoubleOpenCustomHashMap
		 */
		public Short2DoubleOpenCustomHashMap customMap(ShortStrategy strategy) {
			return putElements(new Short2DoubleOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Linked Custom Hash Map
		 * @param strategy the that controls the keys and values
		 * @return a Short2DoubleLinkedOpenCustomHashMap
		 */
		public Short2DoubleLinkedOpenCustomHashMap customLinkedMap(ShortStrategy strategy) {
			return putElements(new Short2DoubleLinkedOpenCustomHashMap(size, strategy));
		}
		
		/**
		 * Builds the Keys and Values into a Concurrent Hash Map
		 * @return a Short2DoubleConcurrentOpenHashMap
		 */
		public Short2DoubleConcurrentOpenHashMap concurrentMap() {
			return putElements(new Short2DoubleConcurrentOpenHashMap(size));
		}
		
		/**
		 * Builds the Keys and Values into a Array Map
		 * @return a Short2DoubleArrayMap
		 */
		public Short2DoubleArrayMap arrayMap() {
			return new Short2DoubleArrayMap(keys, values, size);
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @return a Short2DoubleRBTreeMap
		 */
		public Short2DoubleRBTreeMap rbTreeMap() {
			return putElements(new Short2DoubleRBTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a RedBlack TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2DoubleRBTreeMap
		 */
		public Short2DoubleRBTreeMap rbTreeMap(ShortComparator comp) {
			return putElements(new Short2DoubleRBTreeMap(comp));
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @return a Short2DoubleAVLTreeMap
		 */
		public Short2DoubleAVLTreeMap avlTreeMap() {
			return putElements(new Short2DoubleAVLTreeMap());
		}
		
		/**
		 * Builds the Keys and Values into a AVL TreeMap
		 * @param comp the Comparator that sorts the Tree
		 * @return a Short2DoubleAVLTreeMap
		 */
		public Short2DoubleAVLTreeMap avlTreeMap(ShortComparator comp) {
			return putElements(new Short2DoubleAVLTreeMap(comp));
		}
	}
}