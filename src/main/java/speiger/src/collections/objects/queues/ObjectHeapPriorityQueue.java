package speiger.src.collections.objects.queues;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.BiFunction;
import java.util.Objects;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.functions.consumer.ObjectObjectConsumer;
import speiger.src.collections.objects.functions.function.Object2BooleanFunction;
import speiger.src.collections.objects.functions.function.ObjectObjectUnaryOperator;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.collections.utils.SanityChecks;

/**
 * A Simple Heap base Priority Queue implementation
 * It is a ArrayBased Alternative to TreeSets that has less object allocations
 * @param <T> the type of elements maintained by this Collection
 */
public class ObjectHeapPriorityQueue<T> extends AbstractObjectPriorityQueue<T>
{
	/** The Backing Array */
	protected transient T[] array = (T[])ObjectArrays.EMPTY_ARRAY;
	/** The Amount of elements stored within the array */
	protected int size;
	/** The Sorter of the Array */
	protected Comparator<? super T> comparator;
	
	/**
	 * Default Constructor
	 */
	public ObjectHeapPriorityQueue() {
		this(0, null);
	}
	
	/**
	 * Constructor using custom sorter
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ObjectHeapPriorityQueue(Comparator<? super T> comp) {
		this(0, comp);
	}
	
	/**
	 * Constructor with a Min Capacity
	 * @param size the initial capacity of the backing array
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public ObjectHeapPriorityQueue(int size) {
		this(size, null);
	}
	
	/**
	 * Constructor with a Min Capacity and custom Sorter
	 * @param size the initial capacity of the backing array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws IllegalStateException if the initial size is smaller 0
	 */
	public ObjectHeapPriorityQueue(int size, Comparator<? super T> comp) {
		if(size > 0) array = (T[])new Object[size];
		comparator = comp;
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 */
	public ObjectHeapPriorityQueue(T[] array) {
		this(array, array.length);
	}
	
	/**
	 * Constructor using a initial array
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public ObjectHeapPriorityQueue(T[] array, int size) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		ObjectArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ObjectHeapPriorityQueue(T[] array, Comparator<? super T> comp) {
		this(array, array.length, comp);
	}
	
	/**
	 * Constructor using a initial array and a custom sorter
	 * @param array the Array that should be used
	 * @param size the amount of elements found within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @throws NegativeArraySizeException if size is smaller then 0
	 */
	public ObjectHeapPriorityQueue(T[] array, int size, Comparator<? super T> comp) {
		this.array = Arrays.copyOf(array, size);
		this.size = size;
		comparator = comp;
		ObjectArrays.heapify(array, size, comp);
	}
	
	/**
	 * Constructor using a Collection
	 * @param c the Collection that should be used
	 */
	public ObjectHeapPriorityQueue(ObjectCollection<T> c) {
		array = (T[])c.toArray();
		size = c.size();
		ObjectArrays.heapify(array, size, null);
	}
	
	/**
	 * Constructor using a Collection and a custom sorter
	 * @param c the Collection that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 */
	public ObjectHeapPriorityQueue(ObjectCollection<T> c, Comparator<? super T> comp) {
		array = (T[])c.toArray();
		size = c.size();
		comparator = comp;
		ObjectArrays.heapify(array, size, comp);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param <T> the type of elements maintained by this Collection
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static <T> ObjectHeapPriorityQueue<T> wrap(T[] array) { 
		return wrap(array, array.length);
	}
	
	/**
	 * Wrapping method to help serialization
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param <T> the type of elements maintained by this Collection
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static <T> ObjectHeapPriorityQueue<T> wrap(T[] array, int size) {
		ObjectHeapPriorityQueue<T> queue = new ObjectHeapPriorityQueue<>();
		queue.array = array;
		queue.size = size;
		ObjectArrays.heapify(array, size, null);
		return queue;
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param comp Comparator to sort the Array. Can be null
	 * @param <T> the type of elements maintained by this Collection
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static <T> ObjectHeapPriorityQueue<T> wrap(T[] array, Comparator<? super T> comp) {
		return wrap(array, array.length, comp);
	}
	
	/**
	 * Wrapping method to help serialization, using a custom sorter
	 * @param array the array that should be used
	 * @param size the amount of elements within the array
	 * @param comp Comparator to sort the Array. Can be null
	 * @param <T> the type of elements maintained by this Collection
	 * @return a HeapPriorityQueue containing the original input array
	 */
	public static <T> ObjectHeapPriorityQueue<T> wrap(T[] array, int size, Comparator<? super T> comp) {
		ObjectHeapPriorityQueue<T> queue = new ObjectHeapPriorityQueue<>(comp);
		queue.array = array;
		queue.size = size;
		ObjectArrays.heapify(array, size, comp);
		return queue;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	public void clear() {
		Arrays.fill(array, null);
		size = 0;
	}
	
	@Override
	public ObjectIterator<T> iterator() {
		return new Iter();
	}
	
	@Override
	public void enqueue(T e) {
		if(size == array.length) array = Arrays.copyOf(array, (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), (long)SanityChecks.MAX_ARRAY_SIZE), size+1));
		array[size++] = e;
		ObjectArrays.shiftUp(array, size-1, comparator);
	}
	
	@Override
	public T dequeue() {
		if(size <= 0) throw new NoSuchElementException();
		T value = array[0];
		array[0] = array[--size];
		array[size] = null;
		if(size != 0) ObjectArrays.shiftDown(array, size, 0, comparator);
		return value;
	}
	
	@Override
	public T peek(int index) {
		if(index < 0 || index >= size) throw new NoSuchElementException();
		return array[index];
	}
	
	@Override
	public boolean removeFirst(T e) {
		for(int i = 0;i<size;i++)
			if(Objects.equals(e, array[i])) return removeIndex(i);
		return false;
	}
	
	@Override
	public boolean removeLast(T e) {
		for(int i = size-1;i>=0;i--)
			if(Objects.equals(e, array[i])) return removeIndex(i);
		return false;
	}
	
	@Override
	public void forEach(Consumer<? super T> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(dequeue());
	}
	
	@Override
	public <E> void forEach(E input, ObjectObjectConsumer<E, T> action) {
		Objects.requireNonNull(action);
		for(int i = 0,m=size;i<m;i++) action.accept(input, dequeue());
	}
	
	@Override
	public boolean matchesAny(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(array[i])) return true;
		}
		return false;
	}
	
	@Override
	public boolean matchesNone(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public boolean matchesAll(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(!filter.getBoolean(array[i])) return false;
		}
		return true;
	}
	
	@Override
	public <E> E reduce(E identity, BiFunction<E, T, E> operator) {
		Objects.requireNonNull(operator);
		E state = identity;
		for(int i = 0;i<size;i++) {
			state = operator.apply(state, array[i]);
		}
		return state;
	}
	
	@Override
	public T reduce(ObjectObjectUnaryOperator<T, T> operator) {
		Objects.requireNonNull(operator);
		T state = null;
		boolean empty = true;
		for(int i = 0;i<size;i++) {
			if(empty) {
				empty = false;
				state = array[i];
				continue;
			}
			state = operator.apply(state, array[i]);
		}
		return state;
	}
	
	@Override
	public T findFirst(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(array[i])) {
				T data = array[i];
				removeIndex(i);
				return data;
			}
		}
		return null;
	}
	
	@Override
	public int count(Object2BooleanFunction<T> filter) {
		Objects.requireNonNull(filter);
		int result = 0;
		for(int i = 0;i<size;i++) {
			if(filter.getBoolean(array[i])) result++;
		}
		return result;
	}
	
	protected boolean removeIndex(int index) {
		array[index] = array[--size];
		array[size] = null;
		if(size != index) ObjectArrays.shiftDown(array, size, index, comparator);
		return true;
	}
	
	@Override
	public void onChanged() {
		if(size <= 0) return;
		ObjectArrays.shiftDown(array, size, 0, comparator);
	}
	
	@Override
	public ObjectHeapPriorityQueue<T> copy() {
		ObjectHeapPriorityQueue<T> queue = new ObjectHeapPriorityQueue<>();
		queue.size = size;
		queue.comparator = comparator;
		queue.array = Arrays.copyOf(array, array.length);
		return queue;
	}
	
	@Override
	public Comparator<? super T> comparator() {
		return comparator;
	}
	
	@Override
	public <E> E[] toArray(E[] input) {
		if(input == null || input.length < size()) input = (E[])new Object[size()];
		System.arraycopy(array, 0, input, 0, size());
		return input;
	}
	
	private class Iter implements ObjectIterator<T> {
		@Override
		public boolean hasNext() {
			return !isEmpty();
		}
		
		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			return dequeue();
		}
	}
}