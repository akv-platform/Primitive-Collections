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
		suite.addTest(setSuite("FloatOpenHashSet", FloatOpenHashSet::new, getFeatures()));
		suite.addTest(orderedSetSuite("FloatLinkedOpenHashSet", FloatLinkedOpenHashSet::new, getFeatures()));
		suite.addTest(setSuite("FloatOpenCustomHashSet", T -> new FloatOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("FloatLinkedOpenCustomHashSet", T -> new FloatLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("ImmutableFloatOpenHashSet", ImmutableFloatOpenHashSet::new, getImmutableFeatures()));
		suite.addTest(setSuite("FloatArraySet", FloatArraySet::new, getFeatures()));
		suite.addTest(navigableSetSuite("FloatRBTreeSet", FloatRBTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("FloatAVLTreeSet", FloatAVLTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("Synchronized FloatRBTreeSet", T -> new FloatRBTreeSet(T).synchronize(), getFeatures()));
		suite.addTest(navigableSetSuite("Unmodifiable FloatRBTreeSet", T -> new FloatRBTreeSet(T).unmodifiable(), getImmutableFeatures()));
	}
		
	public static Test setSuite(String name, Function<float[], FloatSet> factory, Collection<Feature<?>> features) {
		return FloatSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<float[], FloatOrderedSet> factory, Collection<Feature<?>> features) {
		return FloatOrderedSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}

	
	public static Test navigableSetSuite(String name, Function<float[], FloatNavigableSet> factory, Collection<Feature<?>> features) {
		return FloatNavigableSetTestSuiteBuilder.using(new SimpleFloatTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements FloatStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(float o) { return Float.hashCode(o); }
		@Override
		public boolean equals(float key, float value) { return Float.floatToIntBits(key) == Float.floatToIntBits(value); }
	}

	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}
	
	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}