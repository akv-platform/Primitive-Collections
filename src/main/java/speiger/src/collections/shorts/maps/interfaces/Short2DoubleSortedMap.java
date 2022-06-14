package speiger.src.collections.shorts.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.doubles.collections.DoubleCollection;
import speiger.src.collections.shorts.functions.ShortComparator;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.collections.shorts.utils.maps.Short2DoubleMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Short2DoubleOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Short2DoubleOrderedMap directly and will remove Short2DoubleSortedMap implementations in favor of Short2DoubleOrderedMap instead
 */
public interface Short2DoubleSortedMap extends SortedMap<Short, Double>, Short2DoubleMap
{
	@Override
	public ShortComparator comparator();
	
	@Override
	public Short2DoubleSortedMap copy();
	
	@Override
	public ShortSortedSet keySet();
	@Override
	public DoubleCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Short2DoubleMaps#synchronize
	 */
	public default Short2DoubleSortedMap synchronize() { return Short2DoubleMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Short2DoubleMaps#synchronize
	 */
	public default Short2DoubleSortedMap synchronize(Object mutex) { return Short2DoubleMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Short2DoubleMaps#unmodifiable
	 */
	public default Short2DoubleSortedMap unmodifiable() { return Short2DoubleMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Short2DoubleSortedMap subMap(short fromKey, short toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Short2DoubleSortedMap headMap(short toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Short2DoubleSortedMap tailMap(short fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public short firstShortKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public short pollFirstShortKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public short lastShortKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public short pollLastShortKey();
	
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
	@Deprecated
	public default Short firstKey() { return Short.valueOf(firstShortKey()); }
	@Override
	@Deprecated
	public default Short lastKey() { return Short.valueOf(lastShortKey()); }
	
	@Override
	@Deprecated
	public default Short2DoubleSortedMap subMap(Short fromKey, Short toKey) { return subMap(fromKey.shortValue(), toKey.shortValue()); }
	@Override
	@Deprecated
	public default Short2DoubleSortedMap headMap(Short toKey) { return headMap(toKey.shortValue()); }
	@Override
	@Deprecated
	public default Short2DoubleSortedMap tailMap(Short fromKey) { return tailMap(fromKey.shortValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Short2DoubleMap.FastEntrySet, ObjectSortedSet<Short2DoubleMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Short2DoubleMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Short2DoubleMap.Entry> fastIterator(short fromElement);
	}
}