package speiger.src.tests.floats.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.sets.ImmutableFloatOpenHashSet;
import speiger.src.collections.floats.sets.FloatAVLTreeSet;
import speiger.src.collections.floats.sets.FloatArraySet;
import speiger.src.collections.floats.sets.FloatLinkedOpenCustomHashSet;
import speiger.src.collections.floats.sets.FloatLinkedOpenHashSet;
import speiger.src.collections.floats.sets.FloatNavigableSet;
import speiger.src.collections.floats.sets.FloatOpenCustomHashSet;
import speiger.src.collections.floats.sets.FloatOpenHashSet;
import speiger.src.collections.floats.sets.FloatOrderedSet;
import speiger.src.collections.floats.sets.FloatRBTreeSet;
import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.testers.floats.builder.FloatNavigableSetTestSuiteBuilder;
import speiger.src.testers.floats.builder.FloatOrderedSetTestSuiteBuilder;
import speiger.src.testers.floats.builder.FloatSetTestSuiteBuilder;
import speiger.src.testers.floats.impl.SimpleFloatTestGenerator;
import speiger.src.testers.utils.SpecialFeature;


@SuppressWarnings("javadoc")
public class FloatSetTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("FloatSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("FloatOpenHashSet", FloatOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("FloatLinkedOpenHashSet", FloatLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("FloatOpenCustomHashSet", T -> new FloatOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("FloatLinkedOpenCustomHashSet", T -> new FloatLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableFloatOpenHashSet", ImmutableFloatOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("FloatArraySet", FloatArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("FloatRBTreeSet", FloatRBTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("FloatAVLTreeSet", FloatAVLTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("Synchronized FloatRBTreeSet", T -> new FloatRBTreeSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(navigableSetSuite("Unmodifiable FloatRBTreeSet", T -> new FloatRBTreeSet(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(setSuite("Empty FloatSet", T -> FloatSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton FloatSet", T -> FloatSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized FloatLinkedOpenHashSet", T -> new FloatLinkedOpenHashSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable FloatLinkedOpenHashSet", T -> new FloatLinkedOpenHashSet(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<float[], FloatSet> factory, Collection<Feature<?>> features, int size) {
		return FloatSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.Sets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<float[], FloatOrderedSet> factory, Collection<Feature<?>> features, int size) {
		return FloatOrderedSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<float[], FloatNavigableSet> factory, Collection<Feature<?>> features, int size) {
		return FloatNavigableSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements FloatStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(float o) { return Float.hashCode(o); }
		@Override
		public boolean equals(float key, float value) { return Float.floatToIntBits(key) == Float.floatToIntBits(value); }
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