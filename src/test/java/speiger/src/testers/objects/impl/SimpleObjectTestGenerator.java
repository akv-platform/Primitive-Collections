package speiger.src.testers.objects.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.sets.ObjectSortedSet;
import speiger.src.collections.objects.utils.ObjectArrays;
import speiger.src.testers.objects.generators.TestObjectCollectionGenerator;
import speiger.src.testers.objects.generators.TestObjectListGenerator;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.TestObjectOrderedSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSortedSetGenerator;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class SimpleObjectTestGenerator<T, E extends ObjectCollection<T>> {
	Function<T[], E> mapper;
	T[] keys;
	
	public SimpleObjectTestGenerator(Function<T[], E> mapper) {
		this.mapper = mapper;
	}
	
	public SimpleObjectTestGenerator<T, E> setElements(T...keys) {
		this.keys = keys;
		return this;
	}
	
	public ObjectSamples<T> getSamples() {
		return new ObjectSamples<>(keys[0], keys[1], keys[2], keys[3], keys[4]);
	}
	
	public E create(Object... elements) {
		T[] array = (T[])ObjectArrays.newArray(keys[0].getClass(), elements.length);
		int i = 0;
		for (Object e : elements) {
			array[i++] = (T)e;
		}
		return mapper.apply(array);
	}

	public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<T> order(List<T> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections<T> extends SimpleObjectTestGenerator<T, ObjectCollection<T>> implements TestObjectCollectionGenerator<T>
	{
		public Collections(Function<T[], ObjectCollection<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists<T> extends SimpleObjectTestGenerator<T, ObjectList<T>> implements TestObjectListGenerator<T>
	{
		public Lists(Function<T[], ObjectList<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets<T> extends SimpleObjectTestGenerator<T, ObjectSet<T>> implements TestObjectSetGenerator<T>
	{
		public Sets(Function<T[], ObjectSet<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets<T> extends SimpleObjectTestGenerator<T, ObjectOrderedSet<T>> implements TestObjectOrderedSetGenerator<T>
	{
		public OrderedSets(Function<T[], ObjectOrderedSet<T>> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets<T> extends SimpleObjectTestGenerator<T, ObjectSortedSet<T>> implements TestObjectSortedSetGenerator<T>
	{
		public SortedSets(Function<T[], ObjectSortedSet<T>> mapper) {
			super(mapper);
		}
		
		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public T belowSamplesLesser() { return keys[5]; }
		@Override
		public T belowSamplesGreater() { return keys[6]; }
		@Override
		public T aboveSamplesLesser() { return keys[7]; }
		@Override
		public T aboveSamplesGreater() { return keys[8]; }
	}
	
	public static class NavigableSets<T> extends SimpleObjectTestGenerator<T, ObjectNavigableSet<T>> implements TestObjectNavigableSetGenerator<T>
	{
		public NavigableSets(Function<T[], ObjectNavigableSet<T>> mapper) {
			super(mapper);
		}
		
		@Override
		public ObjectIterable<T> order(ObjectList<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<T> order(List<T> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public T belowSamplesLesser() { return keys[5]; }
		@Override
		public T belowSamplesGreater() { return keys[6]; }
		@Override
		public T aboveSamplesLesser() { return keys[7]; }
		@Override
		public T aboveSamplesGreater() { return keys[8]; }
	}
}