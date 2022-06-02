package speiger.src.tests.ints.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.sets.ImmutableIntOpenHashSet;
import speiger.src.collections.ints.sets.IntAVLTreeSet;
import speiger.src.collections.ints.sets.IntArraySet;
import speiger.src.collections.ints.sets.IntLinkedOpenCustomHashSet;
import speiger.src.collections.ints.sets.IntLinkedOpenHashSet;
import speiger.src.collections.ints.sets.IntNavigableSet;
import speiger.src.collections.ints.sets.IntOpenCustomHashSet;
import speiger.src.collections.ints.sets.IntOpenHashSet;
import speiger.src.collections.ints.sets.IntOrderedSet;
import speiger.src.collections.ints.sets.IntRBTreeSet;
import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.collections.ints.utils.IntStrategy;
import speiger.src.testers.ints.builder.IntNavigableSetTestSuiteBuilder;
import speiger.src.testers.ints.builder.IntOrderedSetTestSuiteBuilder;
import speiger.src.testers.ints.builder.IntSetTestSuiteBuilder;
import speiger.src.testers.ints.impl.SimpleIntTestGenerator;
import speiger.src.testers.utils.SpecialFeature;


@SuppressWarnings("javadoc")
public class IntSetTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("IntSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("IntOpenHashSet", IntOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("IntLinkedOpenHashSet", IntLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("IntOpenCustomHashSet", T -> new IntOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("IntLinkedOpenCustomHashSet", T -> new IntLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableIntOpenHashSet", ImmutableIntOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("IntArraySet", IntArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("IntRBTreeSet", IntRBTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("IntAVLTreeSet", IntAVLTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("Synchronized IntRBTreeSet", T -> new IntRBTreeSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(navigableSetSuite("Unmodifiable IntRBTreeSet", T -> new IntRBTreeSet(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(setSuite("Empty IntSet", T -> IntSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton IntSet", T -> IntSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized IntLinkedOpenHashSet", T -> new IntLinkedOpenHashSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable IntLinkedOpenHashSet", T -> new IntLinkedOpenHashSet(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<int[], IntSet> factory, Collection<Feature<?>> features, int size) {
		return IntSetTestSuiteBuilder.using(new SimpleIntTestGenerator.Sets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<int[], IntOrderedSet> factory, Collection<Feature<?>> features, int size) {
		return IntOrderedSetTestSuiteBuilder.using(new SimpleIntTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<int[], IntNavigableSet> factory, Collection<Feature<?>> features, int size) {
		return IntNavigableSetTestSuiteBuilder.using(new SimpleIntTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements IntStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(int o) { return Integer.hashCode(o); }
		@Override
		public boolean equals(int key, int value) { return key == value; }
	}
	
	private static Collection<CollectionSize> getSizes(int size) {
		switch(size) {
			case 0: return Arrays.asList(CollectionSize.ZERO);
			case 1: return Arrays.asList(CollectionSize.ONE);
			case 2: return Arrays.asList(CollectionSize.ZERO, CollectionSize.ONE);
			case 3: return Arrays.asList(CollectionSize.SEVERAL);
			case 4: return Arrays.asList(CollectionSize.ZERO, CollectionSize.SEVERAL);
			case 5: return Arrays.asList(CollectionSize.ONE, CollectionSize.SEVERAL);
			default: return Arrays.asList(CollectionSize.ANY);
		}
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}
	
	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}