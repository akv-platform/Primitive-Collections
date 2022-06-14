package speiger.src.collections.doubles.maps.interfaces;

import java.util.SortedMap;

import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.doubles.functions.DoubleComparator;
import speiger.src.collections.doubles.sets.DoubleSortedSet;
import speiger.src.collections.doubles.utils.maps.Double2CharMaps;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.collections.ObjectBidirectionalIterator;

/**
 * A Type Specific {@link SortedMap} interface to reduce boxing/unboxing, with a couple extra methods that allow greater control over maps.
 * 
 * @note Double2CharOrderedMap is only extended until 0.6.0 for Compat reasons.
 * The supported classes already implement Double2CharOrderedMap directly and will remove Double2CharSortedMap implementations in favor of Double2CharOrderedMap instead
 */
public interface Double2CharSortedMap extends SortedMap<Double, Character>, Double2CharMap
{
	@Override
	public DoubleComparator comparator();
	
	@Override
	public Double2CharSortedMap copy();
	
	@Override
	public DoubleSortedSet keySet();
	@Override
	public CharCollection values();
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @return a new SortedMap that is synchronized
	 * @see Double2CharMaps#synchronize
	 */
	public default Double2CharSortedMap synchronize() { return Double2CharMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped SortedMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new SortedMap Wrapper that is synchronized
	 * @see Double2CharMaps#synchronize
	 */
	public default Double2CharSortedMap synchronize(Object mutex) { return Double2CharMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped SortedMap that is unmodifiable
	 * @return a new SortedMap Wrapper that is unmodifiable
	 * @see Double2CharMaps#unmodifiable
	 */
	public default Double2CharSortedMap unmodifiable() { return Double2CharMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param toKey where the subMap should end
	 * @return a SubMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2CharSortedMap subMap(double fromKey, double toKey);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the headMap should end
	 * @return a HeadMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2CharSortedMap headMap(double toKey);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @return a TailMap that is within the range of the desired range
	 * @note Some implementations may not support this method.
	 * @note Some implementations may not keep the desired range when the original is changed.
	 */
	public Double2CharSortedMap tailMap(double fromKey);
	
	/**
	 * A method to get the first Key of a Map.
	 * @return the first key in the map
	 */
	public double firstDoubleKey();
	/**
	 * A method to get and remove the first Key of a Map.
	 * @return the first key in the map
	 */
	public double pollFirstDoubleKey();
	/**
	 * A method to get the last Key of a Map.
	 * @return the last key in the map
	 */
	public double lastDoubleKey();
	/**
	 * A method to get and remove the last Key of a Map.
	 * @return the last key in the map
	 */
	public double pollLastDoubleKey();
	
	/**
	 * A method to get the first Value of a Map.
	 * @return the first key in the map
	 */
	public char firstCharValue();
	/**
	 * A method to get the last Value of a Map.
	 * @return the last key in the map
	 */
	public char lastCharValue();
	
	@Override
	@Deprecated
	public default Double firstKey() { return Double.valueOf(firstDoubleKey()); }
	@Override
	@Deprecated
	public default Double lastKey() { return Double.valueOf(lastDoubleKey()); }
	
	@Override
	@Deprecated
	public default Double2CharSortedMap subMap(Double fromKey, Double toKey) { return subMap(fromKey.doubleValue(), toKey.doubleValue()); }
	@Override
	@Deprecated
	public default Double2CharSortedMap headMap(Double toKey) { return headMap(toKey.doubleValue()); }
	@Override
	@Deprecated
	public default Double2CharSortedMap tailMap(Double fromKey) { return tailMap(fromKey.doubleValue()); }
	
	/**
	 * Fast Sorted Entry Set that allows for a faster Entry Iterator by recycling the Entry Object and just exchanging 1 internal value
	 */
	interface FastSortedSet extends Double2CharMap.FastEntrySet, ObjectSortedSet<Double2CharMap.Entry> {
		@Override
		public ObjectBidirectionalIterator<Double2CharMap.Entry> fastIterator();
		/**
		 * Fast iterator that recycles the given Entry object to improve speed and reduce object allocation
		 * @param fromElement that is going to be started from.
		 * @return a improved iterator that starts from the desired element
		 */
		public ObjectBidirectionalIterator<Double2CharMap.Entry> fastIterator(double fromElement);
	}
}