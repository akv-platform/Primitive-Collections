package speiger.src.collections.objects.maps.interfaces;

import speiger.src.collections.objects.utils.maps.Object2LongMaps;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
/**
 * A Special Map Interface giving Access to some really usefull functions
 * The Idea behind this interface is to allow access to functions that give control to the Order of elements.
 * Since Linked implementations as examples can be reordered outside of the Insertion Order.
 * This interface provides basic access to such functions while also providing some Sorted/NaivgableMap implementations that still fit into here.
 * 
 * @param <T> the type of elements maintained by this Collection
 */
public interface Object2LongOrderedMap<T> extends Object2LongMap<T>
{
	/**
	 * A customized put method that allows you to insert into the first index.
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public long putAndMoveToFirst(T key, long value);
	
	/**
	 * A customized put method that allows you to insert into the last index. (This may be nessesary depending on the implementation)
	 * @param key the key that should be inserted
	 * @param value the value that should be inserted
	 * @return the previous present or default return value
	 * @see java.util.Map#put(Object, Object)
	 */
	public long putAndMoveToLast(T key, long value);
	
	/**
	 * A specific move method to move a given key/value to the first index.
	 * @param key that should be moved to the first index
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToFirst(T key);
	/**
	 * A specific move method to move a given key/value to the last index.
	 * @param key that should be moved to the first last
	 * @return true if the value was moved.
	 * @note returns false if the value was not present in the first place
	 */
	public boolean moveToLast(T key);
	
	/**
	 * A Specific get method that allows to move teh given key/value int the first index.
	 * @param key that is searched for
	 * @return the given value for the requested key or default return value
	 */
	public long getAndMoveToFirst(T key);
	/**
	 * A Specific get method that allows to move teh given key/value int the last index.
	 * @param key that is searched for
	 * @return the given value for the requested key or default return value
	 */
	public long getAndMoveToLast(T key);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public T firstKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public T pollFirstKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public T lastKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public T pollLastKey();
	
	/**
	 * A method to get the first Value of a Map.
	 * @return the first key in the map
	 */
	public long firstLongValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public long lastLongValue();
	
	@Override
	public Object2LongOrderedMap<T> copy();
	@Override
	public ObjectOrderedSet<T> keySet();
	@Override
	public ObjectOrderedSet<Object2LongMap.Entry<T>> object2LongEntrySet();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	@Override
	public default Object2LongOrderedMap<T> synchronize() { return Object2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Object2LongMaps#synchronize
	 */
	@Override
	public default Object2LongOrderedMap<T> synchronize(Object mutex) { return Object2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Object2LongMaps#unmodifiable
	 */
	@Override
	public default Object2LongOrderedMap<T> unmodifiable() { return Object2LongMaps.unmodifiable(this); }
	
	/**
	 * Fast Ordered Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 * @param <T> the type of elements maintained by this Collection
	 */
	interface FastOrderedSet<T> extends Object2LongMap.FastEntrySet<T>, ObjectOrderedSet<Object2LongMap.Entry<T>> {
		@Override
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Object2LongMap.Entry<T>> fastIterator(T fromElement);
	}
}