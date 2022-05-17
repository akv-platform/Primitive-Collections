package speiger.src.collections.bytes.collections;

import java.util.Collection;
import java.util.Objects;
import java.util.AbstractCollection;

import speiger.src.collections.bytes.functions.ByteConsumer;
import speiger.src.collections.bytes.utils.ByteIterators;

/**
 * Abstract Type Specific Collection that reduces boxing/unboxing
 */
public abstract class AbstractByteCollection extends AbstractCollection<Byte> implements ByteCollection
{
	@Override
	public abstract ByteIterator iterator();
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean add(Byte e) { return ByteCollection.super.add(e); }

	@Override
	public boolean addAll(ByteCollection c) {
		boolean modified = false;
		for(ByteIterator iter = c.iterator();iter.hasNext();modified |= add(iter.nextByte()));
		return modified;
	}
	
	@Override
	public ByteCollection copy() { throw new UnsupportedOperationException(); }
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean contains(Object e) { return ByteCollection.super.contains(e); }
	
	/**
	 * A Type-Specific implementation of contains. This implementation iterates over the elements and returns true if the value match.
	 * @param e the element that should be searched for.
	 * @return true if the value was found.
	 */
	@Override
	public boolean contains(byte e) {
		for(ByteIterator iter = iterator();iter.hasNext();) { if(iter.nextByte() == e) return true; }
		return false;
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean addAll(Collection<? extends Byte> c)
	{
		return c instanceof ByteCollection ? addAll((ByteCollection)c) : super.addAll(c);
	}
	
	/**
	 * A Type-Specific implementation of containsAll. This implementation iterates over all elements and checks all elements are present in the other collection.
	 * @param c the collection that should be checked if it contains all elements.
	 * @return true if all elements were found in the collection
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean containsAll(ByteCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return true;
		for(ByteIterator iter = c.iterator();iter.hasNext();)
			if(!contains(iter.nextByte()))
				return false;
		return true;
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		Objects.requireNonNull(c);
		return c instanceof ByteCollection ? containsAll((ByteCollection)c) : super.containsAll(c);
	}
	
	/**
	 * This implementation iterates over the elements of the collection and checks if they are stored in this collection
	 * @param c the elements that should be checked for
	 * @return true if any element is in this collection
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	@Deprecated
	public boolean containsAny(Collection<?> c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		for(Object e : c) 
			if(contains(e))
				return true;
		return false;
	}
	
	/**
	 * This implementation iterates over the elements of the collection and checks if they are stored in this collection.
	 * @param c the elements that should be checked for
	 * @return true if any element is in this collection
 	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean containsAny(ByteCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		for(ByteIterator iter = c.iterator();iter.hasNext();) 
			if(contains(iter.nextByte()))
				return true;
		return false;
	}
		
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public boolean remove(Object e) { return ByteCollection.super.remove(e); }
	
	/**
	 * A Type-Specific implementation of remove. This implementation iterates over the elements until it finds the element that is searched for or it runs out of elements.
	 * It stops after finding the first element
	 * @param e the element that is searched for
	 * @return true if the element was found and removed.
	 */
	@Override
	public boolean remByte(byte e) {
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(iter.nextByte() == e) {
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * A Type-Specific implementation of removeAll. This Implementation iterates over all elements and removes them as they were found in the other collection.
	 * @param c the elements that should be deleted
	 * @return true if the collection was modified.
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean removeAll(ByteCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		boolean modified = false;
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(c.contains(iter.nextByte())) {
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}

	@Override
	public boolean removeAll(ByteCollection c, ByteConsumer r) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) return false;
		Objects.requireNonNull(r);
		boolean modified = false;
		for(ByteIterator iter = iterator();iter.hasNext();) {
			byte e = iter.nextByte();
			if(c.contains(e)) {
				r.accept(e);
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * A Type-Specific implementation of retainAll. This Implementation iterates over all elements and removes them as they were not found in the other collection.
	 * @param c the elements that should be kept
	 * @return true if the collection was modified.
	 * @throws java.lang.NullPointerException if the collection is null
	 */
	@Override
	public boolean retainAll(ByteCollection c) {
		Objects.requireNonNull(c);
		if(c.isEmpty()) {
			boolean modified = !isEmpty();
			clear();
			return modified;
		}
		boolean modified = false;
		for(ByteIterator iter = iterator();iter.hasNext();) {
			if(!c.contains(iter.nextByte())) {
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	@Override
	public boolean retainAll(ByteCollection c, ByteConsumer r) {
		Objects.requireNonNull(c);
		Objects.requireNonNull(r);
		if(c.isEmpty()) {
			boolean modified = !isEmpty();
			forEach(r);
			clear();
			return modified;
		}
		boolean modified = false;
		for(ByteIterator iter = iterator();iter.hasNext();) {
			byte e = iter.nextByte();
			if(!c.contains(e)) {
				iter.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * A Type-Specific implementation of toArray that links to {@link #toByteArray(byte[])} with a newly created array.
	 * @return an array containing all of the elements in this collection
	 */
	@Override
	public byte[] toByteArray() {
		return toByteArray(new byte[size()]);
	}
	
	/**
	 * A Type-Specific implementation of toArray. This implementation iterates over all elements and unwraps them into primitive type.
	 * @param a array that the elements should be injected to. If null or to small a new array with the right size is created
	 * @return an array containing all of the elements in this collection
	 */
	@Override
	public byte[] toByteArray(byte[] a) {
		if(a == null || a.length < size()) a = new byte[size()];
		ByteIterators.unwrap(a, iterator());
		return a;
	}
}