package speiger.src.collections.ints.misc.pairs;

import speiger.src.collections.ints.misc.pairs.impl.IntDoubleImmutablePair;
import speiger.src.collections.ints.misc.pairs.impl.IntDoubleMutablePair;
/**
 * Key Value Pair Interface that allows to reduce boxing/unboxing.
 */
public interface IntDoublePair
{
	/**
	 * Empty Reference for Immutable Pairs
	 */
	public static final IntDoublePair EMPTY = new IntDoubleImmutablePair();
	
	/**
	 * @return empty Immutable Pair
	 */
	public static IntDoublePair of() {
		return EMPTY;
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Immutable Pair of Key
	 */	
	public static IntDoublePair ofKey(int key) {
		return new IntDoubleImmutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of Value
	 */	
	public static IntDoublePair ofValue(double value) {
		return new IntDoubleImmutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Immutable Pair of key and value
	 */
	public static IntDoublePair of(int key, double value) {
		return new IntDoubleImmutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be immutably copied
	 * @return a Immutable Copy of the Provided Pair
	 */
	public static IntDoublePair of(IntDoublePair pair) {
		return new IntDoubleImmutablePair(pair.getIntKey(), pair.getDoubleValue());
	}
	
	/**
	 * @return empty Mutable Pair
	 */
	public static IntDoublePair mutable() {
		return new IntDoubleMutablePair();
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @return Mutable Pair of key
	 */
	public static IntDoublePair mutableKey(int key) {
		return new IntDoubleMutablePair(key, 0D);
	}
	
	/**
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of value
	 */
	public static IntDoublePair mutableValue(double value) {
		return new IntDoubleMutablePair(0, value);
	}
	
	/**
	 * @param key the key that should be in the pair
	 * @param value the value that should be in the pair
	 * @return Mutable Pair of key and value
	 */
	public static IntDoublePair mutable(int key, double value) {
		return new IntDoubleMutablePair(key, value);
	}
	
	/**
	 * @param pair the Pair that should be copied
	 * @return a Mutable Copy of the Provided Pair
	 */
	public static IntDoublePair mutable(IntDoublePair pair) {
		return new IntDoubleMutablePair(pair.getIntKey(), pair.getDoubleValue());
	}
	
	/**
	 * Sets the Key of the Pair.
	 * @param key the key that should be set.
	 * @return self or a new Pair instance with the new key.
	 */
	public IntDoublePair setIntKey(int key);
	/**
	 * @return the Key of the Pair
	 */
	public int getIntKey();
	
	/**
	 * Sets the Value of the Pair.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new value.
	 */	
	public IntDoublePair setDoubleValue(double value);
	
	/**
	 * @return the Value of the Pair
	 */
	public double getDoubleValue();
	
	/**
	 * Sets key and value of the Pair
	 * @param key the key that should be set.
	 * @param value the value that should be set.
	 * @return self or a new Pair instance with the new key and value.
	 */
	public IntDoublePair set(int key, double value);
	
	/**
	 * Clones the Pair if it is mutable.
	 * @return a New Mutable Instance if it is mutable
	 */
	public IntDoublePair shallowCopy();
}