package speiger.src.collections.floats.functions;

/**
 * Type-Specific Supplier interface that reduces (un)boxing and allows to merge other consumer types into this interface
 */
public interface FloatSupplier
{
	/**
	 * @return the supplied value
	 */
	public float getFloat();
}