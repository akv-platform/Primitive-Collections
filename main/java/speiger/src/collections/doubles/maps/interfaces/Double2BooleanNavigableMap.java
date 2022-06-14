package speiger.src.collections.doubles.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.utils.maps.Double2BooleanMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 */
public interface Double2BooleanNavigableMap extends Double2BooleanSortedMap, NavigableMap<Double, Boolean>
{
	@Override
	public Double2BooleanNavigableMap copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Double2BooleanNavigableMap descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public DoubleNavigableSet navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public DoubleNavigableSet descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Double2BooleanMap.Entry firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Double2BooleanMap.Entry lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Double2BooleanMap.Entry pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Double2BooleanMap.Entry pollLastEntry();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public DoubleNavigableSet keySet();
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Double2BooleanMaps#synchronize
	 */
	public default Double2BooleanNavigableMap synchronize() { return Double2BooleanMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Double2BooleanMaps#synchronize
	 */
	public default Double2BooleanNavigableMap synchronize(Object mutex) { return Double2BooleanMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Double2BooleanMaps#unmodifiable
	 */
	public default Double2BooleanNavigableMap unmodifiable() { return Double2BooleanMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param fromInclusive if the fromKey is inclusive or not
	 * @param toKey where the subMap should end
	 * @param toInclusive if the toKey is inclusive or not
	 * @return a SubMap that is within the range of the desired range
	 */
	public Double2BooleanNavigableMap subMap(double fromKey, boolean fromInclusive, double toKey, boolean toInclusive);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the HeadMap should end
	 * @param inclusive if the toKey is inclusive or not
	 * @return a HeadMap that is within the range of the desired range
	 */
	public Double2BooleanNavigableMap headMap(double toKey, boolean inclusive);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @param inclusive if the fromKey is inclusive or not
	 * @return a TailMap that is within the range of the desired range
	 */
	public Double2BooleanNavigableMap tailMap(double fromKey, boolean inclusive);
	
	@Override
	public default Double2BooleanNavigableMap subMap(double fromKey, double toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Double2BooleanNavigableMap headMap(double toKey) { return headMap(toKey, false); }
	@Override
	public default Double2BooleanNavigableMap tailMap(double fromKey) { return tailMap(fromKey, true); }
	
	/**
	 * A Helper method to set the max value for SubMaps. (Default: double.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(double e);
	/**
	 * A Helper method to get the max value for SubMaps.
	 * @return the default max value.
	 */
	public double getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubMaps. (Default: double.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(double e);
	/**
	 * A Helper method to get the min value for SubMaps.
	 * @return the default min value.
	 */
	public double getDefaultMinValue();
	
	/**
	 * A Type Specific lowerKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public double lowerKey(double key);
	/**
	 * A Type Specific higherKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public double higherKey(double key);
	/**
	 * A Type Specific floorKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public double floorKey(double key);
	/**
	 * A Type Specific ceilingKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public double ceilingKey(double key);
	
	/**
	 * A Type Specific lowerEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower entry that can be found, or null
	 */
	public Double2BooleanMap.Entry lowerEntry(double key);
	/**
	 * A Type Specific higherEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher entry that can be found, or null
	 */
	public Double2BooleanMap.Entry higherEntry(double key);
	/**
	 * A Type Specific floorEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal entry that can be found, or null
	 */
	public Double2BooleanMap.Entry floorEntry(double key);
	/**
	 * A Type Specific ceilingEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal entry that can be found, or null
	 */
	public Double2BooleanMap.Entry ceilingEntry(double key);
	
	@Override
	@Deprecated
	public default Double lowerKey(Double key) { return Double.valueOf(lowerKey(key.doubleValue()));}
	@Override
	@Deprecated
	public default Double floorKey(Double key) { return Double.valueOf(floorKey(key.doubleValue()));}
	@Override
	@Deprecated
	public default Double ceilingKey(Double key) { return Double.valueOf(ceilingKey(key.doubleValue()));}
	@Override
	@Deprecated
	public default Double higherKey(Double key) { return Double.valueOf(higherKey(key.doubleValue()));}
	
	@Override
	@Deprecated
	default Double2BooleanMap.Entry lowerEntry(Double key) { return lowerEntry(key.doubleValue()); }
	@Override
	@Deprecated
	default Double2BooleanMap.Entry floorEntry(Double key) { return floorEntry(key.doubleValue()); }
	@Override
	@Deprecated
	default Double2BooleanMap.Entry ceilingEntry(Double key) { return ceilingEntry(key.doubleValue()); }
	@Override
	@Deprecated
	default Double2BooleanMap.Entry higherEntry(Double key) { return higherEntry(key.doubleValue()); }
	
	@Override
	@Deprecated
	default Double2BooleanNavigableMap subMap(Double fromKey, boolean fromInclusive, Double toKey, boolean toInclusive) { return subMap(fromKey.doubleValue(), fromInclusive, toKey.doubleValue(), toInclusive); }
	@Override
	@Deprecated
	default Double2BooleanNavigableMap headMap(Double toKey, boolean inclusive) { return headMap(toKey.doubleValue(), inclusive); }
	@Override
	@Deprecated
	default Double2BooleanNavigableMap tailMap(Double fromKey, boolean inclusive) { return tailMap(fromKey.doubleValue(), inclusive); }
	@Override
	@Deprecated
	default Double2BooleanNavigableMap subMap(Double fromKey, Double toKey) { return subMap(fromKey.doubleValue(), true, toKey.doubleValue(), false); }
	@Override
	@Deprecated
	default Double2BooleanNavigableMap headMap(Double toKey) { return headMap(toKey.doubleValue(), false); }
	@Override
	@Deprecated
	default Double2BooleanNavigableMap tailMap(Double fromKey) { return tailMap(fromKey.doubleValue(), true); }
}