package speiger.src.collections.ints.sets;

import java.util.EnumSet;

import speiger.src.collections.ints.base.BaseIntCollectionTest;
import speiger.src.collections.ints.base.BaseIntSortedSetTest;
import speiger.src.collections.ints.collections.IntCollection;
import speiger.src.collections.ints.utils.IntStrategy;
import speiger.src.collections.tests.CollectionTest;
import speiger.src.collections.tests.SortedSetTest;

@SuppressWarnings("javadoc")
public class IntHashSetTests
{
	public static abstract class BaseIntOpenHashSetTests extends BaseIntSortedSetTest
	{
		@Override
		protected EnumSet<SortedSetTest> getValidSortedSetTests() { return EnumSet.of(SortedSetTest.ADD_MOVE, SortedSetTest.MOVE, SortedSetTest.PEEK, SortedSetTest.POLL); }
	}
	
	public static class IntOpenHashSetTests extends BaseIntCollectionTest
	{
		@Override
		protected IntCollection create(int[] data) { return new IntOpenHashSet(data); }
		
		@Override
		protected EnumSet<CollectionTest> getValidCollectionTests()
		{
			EnumSet<CollectionTest> tests = super.getValidCollectionTests();
			tests.remove(CollectionTest.TO_STRING);
			return tests;
		}
	}
	
	public static class IntLinkedOpenHashSetTests extends BaseIntOpenHashSetTests
	{
		@Override
		protected IntSortedSet create(int[] data) { return new IntLinkedOpenHashSet(data); }
	}
	
	public static class IntOpenCustomHashSetTests extends BaseIntCollectionTest
	{
		@Override
		protected IntCollection create(int[] data) { return new IntOpenCustomHashSet(data, new DefaultStrategy()); }
		
		@Override
		protected EnumSet<CollectionTest> getValidCollectionTests()
		{
			EnumSet<CollectionTest> tests = super.getValidCollectionTests();
			tests.remove(CollectionTest.TO_STRING);
			return tests;
		}
	}
	
	public static class IntLinkedOpenCustomHashSetTests extends BaseIntOpenHashSetTests
	{
		@Override
		protected IntSortedSet create(int[] data) { return new IntLinkedOpenCustomHashSet(data, new DefaultStrategy()); }
	}
	
	public static class DefaultStrategy implements IntStrategy
	{
		@Override
		public int hashCode(int o) { return Integer.hashCode(o); }
		@Override
		public boolean equals(int key, int value) { return key == value; }
	}
}
