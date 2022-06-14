package speiger.src.collections.objects.functions.function;


/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 * @param <T> the type of elements maintained by this Collection
 */
@FunctionalInterface
public interface Object2IntFunction<T> extends java.util.function.ToIntFunction<T>
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public int getInt(T k);
	

	
	@Override
	public default int applyAsInt(T k) { return getInt(k); }
}