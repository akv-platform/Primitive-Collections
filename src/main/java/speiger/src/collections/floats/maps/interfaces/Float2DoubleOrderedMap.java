package speiger.src.collections.floats.maps.interfaces;

import speiger.src.collections.floats.utils.maps.Float2DoubleMaps;
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
public interface Float2DoubleOrderedMap extends Float2DoubleMap
{
	/**
	 * A customized put method that allows you to insert into the first index.
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public double putAndMoveToFirst(float key, double value);
	
	/**
	 * A customized put method that allows you to insert into the last index. (This may be nessesary depending on the implementation)
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public double putAndMoveToLast(float key, double value);
	
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
	public double getAndMoveToFirst(float key);
	/**
	 * A Specific get method that allows to move teh given key/value int the last index.
	 * @param key that is searched for
	 * @return the given value for the requested key or default return value
	 */
	public double getAndMoveToLast(float key);
	
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
	public double firstDoubleValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public double lastDoubleValue();
	
	@Override
	public Float2DoubleOrderedMap copy();
	@Override
	public FloatOrderedSet keySet();
	@Override
	public ObjectOrderedSet<Float2DoubleMap.Entry> float2DoubleEntrySet();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Float2DoubleMaps#synchronize
	 */
	@Override
	public default Float2DoubleOrderedMap synchronize() { return Float2DoubleMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Float2DoubleMaps#synchronize
	 */
	@Override
	public default Float2DoubleOrderedMap synchronize(Object mutex) { return Float2DoubleMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Float2DoubleMaps#unmodifiable
	 */
	@Override
	public default Float2DoubleOrderedMap unmodifiable() { return Float2DoubleMaps.unmodifiable(this); }
	
	/**
	 * Fast Ordered Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastOrderedSet extends Float2DoubleMap.FastEntrySet, ObjectOrderedSet<Float2DoubleMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Float2DoubleMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Float2DoubleMap.Entry> fastIterator(float fromElement);
	}
}