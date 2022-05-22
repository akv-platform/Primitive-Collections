package speiger.src.testers.shorts.impl;

import java.util.List;
import java.util.function.Function;

import speiger.src.collections.shorts.collections.ShortCollection;
import speiger.src.collections.shorts.collections.ShortIterable;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.sets.ShortSortedSet;
import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.shorts.generators.TestShortListGenerator;
import speiger.src.testers.shorts.generators.TestShortNavigableSetGenerator;
import speiger.src.testers.shorts.generators.TestShortOrderedSetGenerator;
import speiger.src.testers.shorts.generators.TestShortSetGenerator;
import speiger.src.testers.shorts.generators.TestShortSortedSetGenerator;
import speiger.src.testers.shorts.utils.ShortSamples;

public class SimpleShortTestGenerator<T extends ShortCollection> {
	Function<short[], T> mapper;
	
	public SimpleShortTestGenerator(Function<short[], T> mapper) {
		this.mapper = mapper;
	}
	
	public ShortSamples getSamples() {
		return new ShortSamples((short)1, (short)0, (short)2, (short)3, (short)4);
	}
	
	public T create(short... elements) {
		return mapper.apply(elements);
	}
	
	public T create(Object... elements) {
		short[] array = new short[elements.length];
		int i = 0;
		for (Object e : elements) {
			array[i++] = ((Short)e).shortValue();
		}
		return mapper.apply(array);
	}
	
	public ShortIterable order(ShortList insertionOrder) {
		return insertionOrder;
	}
	
	public Iterable<Short> order(List<Short> insertionOrder) {
		return insertionOrder;
	}
	
	public static class Collections extends SimpleShortTestGenerator<ShortCollection> implements TestShortCollectionGenerator
	{
		public Collections(Function<short[], ShortCollection> mapper) {
			super(mapper);
		}
	}
	
	public static class Lists extends SimpleShortTestGenerator<ShortList> implements TestShortListGenerator
	{
		public Lists(Function<short[], ShortList> mapper) {
			super(mapper);
		}
	}
	
	public static class Sets extends SimpleShortTestGenerator<ShortSet> implements TestShortSetGenerator
	{
		public Sets(Function<short[], ShortSet> mapper) {
			super(mapper);
		}
	}
	
	public static class OrderedSets extends SimpleShortTestGenerator<ShortOrderedSet> implements TestShortOrderedSetGenerator
	{
		public OrderedSets(Function<short[], ShortOrderedSet> mapper) {
			super(mapper);
		}
	}
	
	public static class SortedSets extends SimpleShortTestGenerator<ShortSortedSet> implements TestShortSortedSetGenerator
	{
		public SortedSets(Function<short[], ShortSortedSet> mapper) {
			super(mapper);
		}
		
		@Override
		public ShortIterable order(ShortList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Short> order(List<Short> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public short belowSamplesLesser() { return -2; }
		@Override
		public short belowSamplesGreater() { return -1; }
		@Override
		public short aboveSamplesLesser() { return 5; }
		@Override
		public short aboveSamplesGreater() { return 6; }
	}
	
	public static class NavigableSets extends SimpleShortTestGenerator<ShortNavigableSet> implements TestShortNavigableSetGenerator
	{
		public NavigableSets(Function<short[], ShortNavigableSet> mapper) {
			super(mapper);
		}
		
		@Override
		public ShortIterable order(ShortList insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public Iterable<Short> order(List<Short> insertionOrder) {
			insertionOrder.sort(null);
			return insertionOrder;
		}
		
		@Override
		public short belowSamplesLesser() { return -2; }
		@Override
		public short belowSamplesGreater() { return -1; }
		@Override
		public short aboveSamplesLesser() { return 5; }
		@Override
		public short aboveSamplesGreater() { return 6; }
	}
}