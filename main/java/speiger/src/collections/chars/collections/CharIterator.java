package speiger.src.collections.chars.collections;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import speiger.src.collections.chars.functions.CharConsumer;

import speiger.src.collections.objects.functions.consumer.ObjectCharConsumer;

/**
 * A Type-Specific {@link Iterator} that reduces (un)boxing
 */
public interface CharIterator extends Iterator<Character>
{
	/**
 	* Returns the next element in the iteration.
 	*
 	* @return the next element in the iteration
 	* @throws java.util.NoSuchElementException if the iteration has no more elements
 	* @see Iterator#next()
 	*/
	public char nextChar();
	
	/** {@inheritDoc}
	* <p>This default implementation delegates to the corresponding type-specific function.
	* @deprecated Please use the corresponding type-specific function instead. 
	*/
	@Override
	@Deprecated
	public default Character next() { return Character.valueOf(nextChar()); }
	
	/**
	 * Performs the given action for each remaining element until all elements
	 * have been processed or the action throws an exception.  Actions are
	 * performed in the order of iteration, if that order is specified.
	 * Exceptions thrown by the action are relayed to the caller.
	 *
	 * @implSpec
	 * <p>The default implementation behaves as if:
	 * <pre>{@code
	 *	while (hasNext()) action.accept(nextChar());
	 * }</pre>
	 *
	 * @param action The action to be performed for each element
	 * @throws java.lang.NullPointerException if the specified action is null
	 * @see Iterator#forEachRemaining(Consumer)
	 */
	public default void forEachRemaining(CharConsumer action) {
		Objects.requireNonNull(action);
		while(hasNext()) { action.accept(nextChar()); }
	}
	
	/** {@inheritDoc}
 	* <p>This default implementation delegates to the corresponding type-specific function.
 	* @deprecated Please use the corresponding type-specific function instead. 
 	*/
	@Deprecated
	@Override
	default void forEachRemaining(Consumer<? super Character> action) {
		Objects.requireNonNull(action);
		forEachRemaining(action::accept);
	}
	
	/**
	 * Helper function to reduce Lambda usage and allow for more method references, since these are faster/cleaner.
	 * @param input the object that should be included
	 * @param action The action to be performed for each element
	 * @param <E> the generic type of the Object
	 * @throws java.lang.NullPointerException if the specified action is null
	 */
	default <E> void forEachRemaining(E input, ObjectCharConsumer<E> action) {
		Objects.requireNonNull(action);
		while(hasNext()) { action.accept(input, nextChar()); }
	}
	
	/**
	 * Skips the Given amount of elements if possible. A Optimization function to skip elements faster if the implementation allows it.
	 * @param amount the amount of elements that should be skipped
	 * @return the amount of elements that were skipped
	 */
	default int skip(int amount) {
		if(amount < 0) throw new IllegalStateException("Negative Numbers are not allowed");
		int i = 0;
		for(;i<amount && hasNext();nextChar(), i++);
		return i;
	}
}