package speiger.src.tests.shorts.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.sets.ImmutableShortOpenHashSet;
import speiger.src.collections.shorts.sets.ShortAVLTreeSet;
import speiger.src.collections.shorts.sets.ShortArraySet;
import speiger.src.collections.shorts.sets.ShortLinkedOpenCustomHashSet;
import speiger.src.collections.shorts.sets.ShortLinkedOpenHashSet;
import speiger.src.collections.shorts.sets.ShortNavigableSet;
import speiger.src.collections.shorts.sets.ShortOpenCustomHashSet;
import speiger.src.collections.shorts.sets.ShortOpenHashSet;
import speiger.src.collections.shorts.sets.ShortOrderedSet;
import speiger.src.collections.shorts.sets.ShortRBTreeSet;
import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.collections.shorts.utils.ShortStrategy;
import speiger.src.testers.shorts.builder.ShortNavigableSetTestSuiteBuilder;
import speiger.src.testers.shorts.builder.ShortOrderedSetTestSuiteBuilder;
import speiger.src.testers.shorts.builder.ShortSetTestSuiteBuilder;
import speiger.src.testers.shorts.impl.SimpleShortTestGenerator;
import speiger.src.testers.utils.SpecialFeature;


@SuppressWarnings("javadoc")
public class ShortSetTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("ShortSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("ShortOpenHashSet", ShortOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("ShortLinkedOpenHashSet", ShortLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("ShortOpenCustomHashSet", T -> new ShortOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ShortLinkedOpenCustomHashSet", T -> new ShortLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableShortOpenHashSet", ImmutableShortOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("ShortArraySet", ShortArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("ShortRBTreeSet", ShortRBTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("ShortAVLTreeSet", ShortAVLTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("Synchronized ShortRBTreeSet", T -> new ShortRBTreeSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(navigableSetSuite("Unmodifiable ShortRBTreeSet", T -> new ShortRBTreeSet(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(setSuite("Empty ShortSet", T -> ShortSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton ShortSet", T -> ShortSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized ShortLinkedOpenHashSet", T -> new ShortLinkedOpenHashSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable ShortLinkedOpenHashSet", T -> new ShortLinkedOpenHashSet(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<short[], ShortSet> factory, Collection<Feature<?>> features, int size) {
		return ShortSetTestSuiteBuilder.using(new SimpleShortTestGenerator.Sets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<short[], ShortOrderedSet> factory, Collection<Feature<?>> features, int size) {
		return ShortOrderedSetTestSuiteBuilder.using(new SimpleShortTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<short[], ShortNavigableSet> factory, Collection<Feature<?>> features, int size) {
		return ShortNavigableSetTestSuiteBuilder.using(new SimpleShortTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements ShortStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(short o) { return Short.hashCode(o); }
		@Override
		public boolean equals(short key, short value) { return key == value; }
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