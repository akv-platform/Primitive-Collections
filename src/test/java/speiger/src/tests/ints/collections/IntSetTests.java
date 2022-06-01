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
		suite.addTest(setSuite("IntOpenHashSet", IntOpenHashSet::new, getFeatures()));
		suite.addTest(orderedSetSuite("IntLinkedOpenHashSet", IntLinkedOpenHashSet::new, getFeatures()));
		suite.addTest(setSuite("IntOpenCustomHashSet", T -> new IntOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("IntLinkedOpenCustomHashSet", T -> new IntLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("ImmutableIntOpenHashSet", ImmutableIntOpenHashSet::new, getImmutableFeatures()));
		suite.addTest(setSuite("IntArraySet", IntArraySet::new, getFeatures()));
		suite.addTest(navigableSetSuite("IntRBTreeSet", IntRBTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("IntAVLTreeSet", IntAVLTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("Synchronized IntRBTreeSet", T -> new IntRBTreeSet(T).synchronize(), getFeatures()));
		suite.addTest(navigableSetSuite("Unmodifiable IntRBTreeSet", T -> new IntRBTreeSet(T).unmodifiable(), getImmutableFeatures()));
	}
		
	public static Test setSuite(String name, Function<int[], IntSet> factory, Collection<Feature<?>> features) {
		return IntSetTestSuiteBuilder.using(new SimpleIntTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<int[], IntOrderedSet> factory, Collection<Feature<?>> features) {
		return IntOrderedSetTestSuiteBuilder.using(new SimpleIntTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}

	
	public static Test navigableSetSuite(String name, Function<int[], IntNavigableSet> factory, Collection<Feature<?>> features) {
		return IntNavigableSetTestSuiteBuilder.using(new SimpleIntTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements IntStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(int o) { return Integer.hashCode(o); }
		@Override
		public boolean equals(int key, int value) { return key == value; }
	}

	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}
	
	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}