package speiger.src.collections.booleans.lists;

import java.util.List;
import java.util.Comparator;

import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.collections.BooleanSplititerator;
import speiger.src.collections.booleans.functions.BooleanComparator;
import speiger.src.collections.booleans.utils.BooleanArrays;
import speiger.src.collections.booleans.utils.BooleanLists;
import speiger.src.collections.booleans.utils.BooleanSplititerators;

/**
 * A Type Specific List interface that reduces boxing/unboxing and adds a couple extra quality of life features
 */
public interface BooleanList extends BooleanCollection, List<Boolean>
{
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @return true if the list was modified
	 * @see List#add(Object)
	 */
	@Override
	public boolean add(boolean e);
	
	/**
	 * A Type-Specific add Function to reduce (un)boxing
	 * @param e the element to add
	 * @param index index at which the specified element is to be inserted
	 * @see List#add(int, Object)
	 */
	public void add(int index, boolean e);
	
	/**
	 * A Helper function that will only add elements if it is not present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfAbsent(boolean e) {
		if(indexOf(e) == -1) return add(e);
		return false;
	}
	
	/**
	 * A Helper function that will only add elements if it is present.
	 * @param e the element to add
	 * @return true if the list was modified
	 */
	public default boolean addIfPresent(boolean e) {
		if(indexOf(e) != -1) return add(e);
		return false;
	}
	
	/**
	 * A Type-Specific addAll Function to reduce (un)boxing
	 * @param c the elements that need to be added
	 * @param index index at which the specified elements is to be inserted
	 * @return true if the list was modified
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, BooleanCollection c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @return true if the list was modified
	 */
	public boolean addAll(BooleanList c);
	
	/**
	 * A Type-Specific and optimized addAll function that allows a faster transfer of elements
	 * @param c the elements that need to be added
	 * @param index index at which the specified elements is to be inserted
	 * @return true if the list was modified
	 */
	public boolean addAll(int index, BooleanList c);
	
	/**
	 * A Type-Specific get function to reduce (un)boxing
	 * @param index the index of the value that is requested
	 * @return the value at the given index
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#get(int)
	 */
	public boolean getBoolean(int index);
	
	/**
	 * A Type-Specific set function to reduce (un)boxing
	 * @param index index of the element to replace
	 * @param e element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is not within the list range
	 * @see List#set(int, Object)
	 */
	public boolean set(int index, boolean e);
	
	/**
	 * A Type-Specific remove function to reduce (un)boxing
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 * @see List#remove(int)
	 */
	public boolean removeBoolean(int index);
	
	/**
	 * A Type-Specific indexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the index of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int indexOf(boolean e);
	
	/**
	 * A Type-Specific lastIndexOf function to reduce (un)boxing
	 * @param e the element that is searched for
	 * @return the lastIndex of the element if found. (if not found then -1)
	 * @note does not support null values
	 */
	public int lastIndexOf(boolean e);

	/**
	 * A function to fast add elements to the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(boolean... a) { addElements(size(), a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public default void addElements(int from, boolean... a) { addElements(from, a, 0, a.length); }
	
	/**
	 * A function to fast add elements to the list
	 * @param from the index where the elements should be added into the list
	 * @param a the elements that should be added
	 * @param offset the start index of the array should be read from
	 * @param length how many elements should be read from
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 */
	public void addElements(int from, boolean[] a, int offset, int length);
	
	/**
	 * A function to fast fetch elements from the list
	 * @param from index where the list should be fetching elements from
	 * @param a the array where the values should be inserted to
	 * @return the inputArray
	 * @throws NullPointerException if the array is null
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 * @throws IllegalStateException if offset or length are smaller then 0 or exceed the array length
	 */
	public default boolean[] getElements(int from, boolean[] a) { return getElements(from, a, 0, a.length); }
	
	/**
	 * A function to fast fetch elements from the list
	 * @param from index where the list should be fetching elements from
	 * @param a the array where the values should be inserted to
	 * @param offset the startIndex of where the array should be written to
	 * @param length the number of elements the values should be fetched from
	 * @return the inputArray
	 * @throws NullPointerException if the array is null
	 * @throws IndexOutOfBoundsException if from is outside of the lists range
	 * @throws IllegalStateException if offset or length are smaller then 0 or exceed the array length
	 */
	public boolean[] getElements(int from, boolean[] a, int offset, int length);
	
	/**
	 * a function to fast remove elements from the list.
	 * @param from the start index of where the elements should be removed from (inclusive)
	 * @param to the end index of where the elements should be removed to (exclusive)
	 */
	public void removeElements(int from, int to);
	
	/**
	 * A Highly Optimized remove function that removes the desired element.
	 * But instead of shifting the elements to the left it moves the last element to the removed space.
	 * @param index the index of the element to be removed
	 * @return the element previously at the specified position
	 */
	public boolean swapRemove(int index);
	
	/**
	 * A Highly Optimized remove function that removes the desired element.
	 * But instead of shifting the elements to the left it moves the last element to the removed space.
	 * @param e the element that should be removed
	 * @return true if the element was removed
	 */
	public boolean swapRemoveBoolean(boolean e);
	
	/**
	 * A function to fast extract elements out of the list, this removes the elements that were fetched.
	 * @param from the start index of where the elements should be fetched from (inclusive)
	 * @param to the end index of where the elements should be fetched to (exclusive)
	 * @return a array of the elements that were fetched
	 */
	public boolean[] extractElements(int from, int to);
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	default void sort(Comparator<? super Boolean> c) {
		sort((K, V) -> c.compare(Boolean.valueOf(K), Boolean.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see BooleanArrays#stableSort(boolean[], BooleanComparator)
	 */
	public default void sort(BooleanComparator c) {
		boolean[] array = toBooleanArray();
		if(c != null) BooleanArrays.stableSort(array, c);
		else BooleanArrays.stableSort(array);
		BooleanListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextBoolean();
			iter.set(array[i]);
		}
	}
	
	/** 
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @param c the sorter of the elements, can be null
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Deprecated
	public default void unstableSort(Comparator<? super Boolean> c) {
		unstableSort((K, V) -> c.compare(Boolean.valueOf(K), Boolean.valueOf(V)));
	}
	
	/**
	 * Sorts the elements specified by the Natural order either by using the Comparator or the elements using a unstable sort
	 * @param c the sorter of the elements, can be null
	 * @see java.util.List#sort(Comparator)
	 * @see BooleanArrays#unstableSort(boolean[], BooleanComparator)
	 */
	public default void unstableSort(BooleanComparator c) {
		boolean[] array = toBooleanArray();
		if(c != null) BooleanArrays.unstableSort(array, c);
		else BooleanArrays.unstableSort(array);
		BooleanListIterator iter = listIterator();
		for (int i = 0,m=size();i<m && iter.hasNext();i++) {
			iter.nextBoolean();
			iter.set(array[i]);
		}
	}
	
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator
	 */
	@Override
	public BooleanListIterator listIterator();
	
	/**
	 * A Type-Specific Iterator of listIterator
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public BooleanListIterator listIterator(int index);
	
	/**
	 * A Type-Specific List of subList
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public BooleanList subList(int from, int to);
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @return a new List that is synchronized
	 * @see BooleanLists#synchronize
	 */
	public default BooleanList synchronize() { return BooleanLists.synchronize(this); }
	
	/**
	 * Creates a Wrapped List that is Synchronized
	 * @param mutex is the controller of the synchronization block
	 * @return a new List Wrapper that is synchronized
	 * @see BooleanLists#synchronize
	 */
	public default BooleanList synchronize(Object mutex) { return BooleanLists.synchronize(this, mutex); }
	
	/**
	 * Creates a Wrapped List that is unmodifiable
	 * @return a new List Wrapper that is unmodifiable
	 * @see BooleanLists#unmodifiable
	 */
	public default BooleanList unmodifiable() { return BooleanLists.unmodifiable(this); }
	
	/**
	 * A function to ensure the elements are within the requested size.
	 * If smaller then the stored elements they get removed as needed.
	 * If bigger it is ensured that enough room is provided depending on the implementation
	 * @param size the requested amount of elements/room for elements
	 */
	public void size(int size);
	
	@Override
	public BooleanList copy();
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean add(Boolean e) {
		return BooleanCollection.super.add(e);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Boolean get(int index) {
		return Boolean.valueOf(getBoolean(index));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Boolean set(int index, Boolean e) {
		return Boolean.valueOf(set(index, e.booleanValue()));
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int indexOf(Object o) {
		return indexOf(((Boolean)o).booleanValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default int lastIndexOf(Object o) {
		return lastIndexOf(((Boolean)o).booleanValue());
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean contains(Object o) {
		return BooleanCollection.super.contains(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default boolean remove(Object o) {
		return BooleanCollection.super.remove(o);
	}
	
	/** {@inheritDoc}
	 * <p>This default implementation delegates to the corresponding type-specific function.
	 * @deprecated Please use the corresponding type-specific function instead. 
	 */
	@Override
	@Deprecated
	public default Boolean remove(int index) {
		return Boolean.valueOf(removeBoolean(index));
	}
	
	/**
	 * A Type Specific Type Splititerator to reduce boxing/unboxing
	 * @return type specific splititerator
	 */
	@Override
	default BooleanSplititerator spliterator() { return BooleanSplititerators.createSplititerator(this, 0); }
}