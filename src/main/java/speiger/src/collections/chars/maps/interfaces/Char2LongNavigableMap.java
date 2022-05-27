package speiger.src.collections.chars.maps.interfaces;

import java.util.NavigableMap;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.utils.maps.Char2LongMaps;

/**
 * A Type Specific Navigable Map interface with a couple helper methods
 */
public interface Char2LongNavigableMap extends Char2LongSortedMap, NavigableMap<Character, Long>
{
	@Override
	public Char2LongNavigableMap copy();
	/** @return a Type Specific desendingMap */
	@Override
	public Char2LongNavigableMap descendingMap();
	/** @return a Type Specific Navigable Key Set */
	@Override
	public CharNavigableSet navigableKeySet();
	/** @return a Type Specific Desending Key Set */
	@Override
	public CharNavigableSet descendingKeySet();
	/** @return a Type Specific firstEntry */
	@Override
	public Char2LongMap.Entry firstEntry();
	/** @return a Type Specific lastEntry */
	@Override
	public Char2LongMap.Entry lastEntry();
	/** @return a Type Specific pollFirstEntry */
	@Override
	public Char2LongMap.Entry pollFirstEntry();
	/** @return a Type Specific pollLastEntry */
	@Override
	public Char2LongMap.Entry pollLastEntry();
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @return a new NavigableMap that is synchronized
	 * @see Char2LongMaps#synchronize
	 */
	public default Char2LongNavigableMap synchronize() { return Char2LongMaps.synchronize(this); }
	
	/**
	 * Creates a Wrapped NavigableMap that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new NavigableMap Wrapper that is synchronized
	 * @see Char2LongMaps#synchronize
	 */
	public default Char2LongNavigableMap synchronize(Object mutex) { return Char2LongMaps.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped NavigableMap that is unmodifiable
	 * @return a new NavigableMap Wrapper that is unmodifiable
	 * @see Char2LongMaps#unmodifiable
	 */
	public default Char2LongNavigableMap unmodifiable() { return Char2LongMaps.unmodifiable(this); }
	
	/**
	 * A Type Specific SubMap method to reduce boxing/unboxing
	 * @param fromKey where the submap should start
	 * @param fromInclusive if the fromKey is inclusive or not
	 * @param toKey where the subMap should end
	 * @param toInclusive if the toKey is inclusive or not
	 * @return a SubMap that is within the range of the desired range
	 */
	public Char2LongNavigableMap subMap(char fromKey, boolean fromInclusive, char toKey, boolean toInclusive);
	/**
	 * A Type Specific HeadMap method to reduce boxing/unboxing
	 * @param toKey where the HeadMap should end
	 * @param inclusive if the toKey is inclusive or not
	 * @return a HeadMap that is within the range of the desired range
	 */
	public Char2LongNavigableMap headMap(char toKey, boolean inclusive);
	/**
	 * A Type Specific TailMap method to reduce boxing/unboxing
	 * @param fromKey where the TailMap should start
	 * @param inclusive if the fromKey is inclusive or not
	 * @return a TailMap that is within the range of the desired range
	 */
	public Char2LongNavigableMap tailMap(char fromKey, boolean inclusive);
	
	@Override
	public default Char2LongNavigableMap subMap(char fromKey, char toKey) { return subMap(fromKey, true, toKey, false); }
	@Override
	public default Char2LongNavigableMap headMap(char toKey) { return headMap(toKey, false); }
	@Override
	public default Char2LongNavigableMap tailMap(char fromKey) { return tailMap(fromKey, true); }
	
	/**
	 * A Helper method to set the max value for SubMaps. (Default: char.MIN_VALUE)
	 * @param e the new max value
	 */
	public void setDefaultMaxValue(char e);
	/**
	 * A Helper method to get the max value for SubMaps.
	 * @return the default max value.
	 */
	public char getDefaultMaxValue();
	
	/**
	 * A Helper method to set the min value for SubMaps. (Default: char.MAX_VALUE)
	 * @param e the new min value
	 */
	public void setDefaultMinValue(char e);
	/**
	 * A Helper method to get the min value for SubMaps.
	 * @return the default min value.
	 */
	public char getDefaultMinValue();
	
	/**
	 * A Type Specific lowerKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower key that can be found
	 */
	public char lowerKey(char key);
	/**
	 * A Type Specific higherKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher key that can be found
	 */
	public char higherKey(char key);
	/**
	 * A Type Specific floorKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal key that can be found
	 */
	public char floorKey(char key);
	/**
	 * A Type Specific ceilingKey method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal key that can be found
	 */
	public char ceilingKey(char key);
	
	/**
	 * A Type Specific lowerEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower entry that can be found, or null
	 */
	public Char2LongMap.Entry lowerEntry(char key);
	/**
	 * A Type Specific higherEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher entry that can be found, or null
	 */
	public Char2LongMap.Entry higherEntry(char key);
	/**
	 * A Type Specific floorEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the greatest lower or equal entry that can be found, or null
	 */
	public Char2LongMap.Entry floorEntry(char key);
	/**
	 * A Type Specific ceilingEntry method to reduce boxing/unboxing.
	 * @param key that should be compared with.
	 * @return the lowest higher or equal entry that can be found, or null
	 */
	public Char2LongMap.Entry ceilingEntry(char key);
	
	@Override
	@Deprecated
	public default Character lowerKey(Character key) { return Character.valueOf(lowerKey(key.charValue()));}
	@Override
	@Deprecated
	public default Character floorKey(Character key) { return Character.valueOf(floorKey(key.charValue()));}
	@Override
	@Deprecated
	public default Character ceilingKey(Character key) { return Character.valueOf(ceilingKey(key.charValue()));}
	@Override
	@Deprecated
	public default Character higherKey(Character key) { return Character.valueOf(higherKey(key.charValue()));}
	
	@Override
	@Deprecated
	default Char2LongMap.Entry lowerEntry(Character key) { return lowerEntry(key.charValue()); }
	@Override
	@Deprecated
	default Char2LongMap.Entry floorEntry(Character key) { return floorEntry(key.charValue()); }
	@Override
	@Deprecated
	default Char2LongMap.Entry ceilingEntry(Character key) { return ceilingEntry(key.charValue()); }
	@Override
	@Deprecated
	default Char2LongMap.Entry higherEntry(Character key) { return higherEntry(key.charValue()); }
	
	@Override
	@Deprecated
	default Char2LongNavigableMap subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) { return subMap(fromKey.charValue(), fromInclusive, toKey.charValue(), toInclusive); }
	@Override
	@Deprecated
	default Char2LongNavigableMap headMap(Character toKey, boolean inclusive) { return headMap(toKey.charValue(), inclusive); }
	@Override
	@Deprecated
	default Char2LongNavigableMap tailMap(Character fromKey, boolean inclusive) { return tailMap(fromKey.charValue(), inclusive); }
	@Override
	@Deprecated
	default Char2LongNavigableMap subMap(Character fromKey, Character toKey) { return subMap(fromKey.charValue(), true, toKey.charValue(), false); }
	@Override
	@Deprecated
	default Char2LongNavigableMap headMap(Character toKey) { return headMap(toKey.charValue(), false); }
	@Override
	@Deprecated
	default Char2LongNavigableMap tailMap(Character fromKey) { return tailMap(fromKey.charValue(), true); }
}