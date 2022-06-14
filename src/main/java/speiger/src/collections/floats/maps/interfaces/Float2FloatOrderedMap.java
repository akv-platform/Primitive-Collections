package speiger.src.collections.floats.maps.interfaces;

import speiger.src.collections.floats.utils.maps.Float2FloatMaps;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
/**
 * A Special Map Interface giving Access to some really usefull functions
 * The Idea behind this interface is to allow access to functions that give control to the Order of elements.
 * Since Linked implementations as examples can be reordered outside of the Insertion Order.
 * This interface provides basic access to such functions while also providing some Sorted/NaivgableMap implementations that still fit into here.
 * 
 */
public interface Float2FloatOrderedMap extends Float2FloatMap
{
	/**
	 * A customized put method that allows you to insert into the first index.
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public float putAndMoveToFirst(float key, float value);
	
	/**
	 * A customized put method that allows you to insert into the last index. (This may be nessesary depending on the implementation)
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public float putAndMoveToLast(float key, float value);
	
	/**
	 * A specific move method to move a given key/value to the first index.
	 * @param key that should be moved to the first index
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToFirst(float key);
	/**
	 * A specific move method to move a given key/value to the last index.
	 * @param key that should be moved to the first last
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToLast(float key);
	
	/**
	 * A Specific get method that allows to move teh given key/value int the first index.
	 * @param key that is searched for
	 * @return the given value for the requested key or default return value
	 */
	public float getAndMoveToFirst(float key);
	/**
	 * A Specific get method that allows to move teh given key/value int the last index.
	 * @param key that is searched for
	 * @return the given value for the requested key or default return value
	 */
	public float getAndMoveToLast(float key);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public float firstFloatKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public float pollFirstFloatKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public float lastFloatKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public float pollLastFloatKey();
	
	/**
	 * A method to get the first Value of a Map.
	 * @return the first key in the map
	 */
	public float firstFloatValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public float lastFloatValue();
	
	@Override
	public Float2FloatOrderedMap copy();
	@Override
	public FloatOrderedSet keySet();
	@Override
	public ObjectOrderedSet<Float2FloatMap.Entry> float2FloatEntrySet();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Float2FloatMaps#synchronize
	 */
	@Override
	public default Float2FloatOrderedMap synchronize() { return Float2FloatMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Float2FloatMaps#synchronize
	 */
	@Override
	public default Float2FloatOrderedMap synchronize(Object mutex) { return Float2FloatMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Float2FloatMaps#unmodifiable
	 */
	@Override
	public default Float2FloatOrderedMap unmodifiable() { return Float2FloatMaps.unmodifiable(this); }
	
	/**
	 * Fast Ordered Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastOrderedSet extends Float2FloatMap.FastEntrySet, ObjectOrderedSet<Float2FloatMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Float2FloatMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Float2FloatMap.Entry> fastIterator(float fromElement);
	}
}