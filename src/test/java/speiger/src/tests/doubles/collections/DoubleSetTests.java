package speiger.src.tests.doubles.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.sets.ImmutableDoubleOpenHashSet;
import speiger.src.collections.doubles.sets.DoubleAVLTreeSet;
import speiger.src.collections.doubles.sets.DoubleArraySet;
import speiger.src.collections.doubles.sets.DoubleLinkedOpenCustomHashSet;
import speiger.src.collections.doubles.sets.DoubleLinkedOpenHashSet;
import speiger.src.collections.doubles.sets.DoubleNavigableSet;
import speiger.src.collections.doubles.sets.DoubleOpenCustomHashSet;
import speiger.src.collections.doubles.sets.DoubleOpenHashSet;
import speiger.src.collections.doubles.sets.DoubleOrderedSet;
import speiger.src.collections.doubles.sets.DoubleRBTreeSet;
import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleStrategy;
import speiger.src.testers.doubles.builder.DoubleNavigableSetTestSuiteBuilder;
import speiger.src.testers.doubles.builder.DoubleOrderedSetTestSuiteBuilder;
import speiger.src.testers.doubles.builder.DoubleSetTestSuiteBuilder;
import speiger.src.testers.doubles.impl.SimpleDoubleTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class DoubleSetTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("DoubleSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("DoubleOpenHashSet", DoubleOpenHashSet::new, getFeatures()));
		suite.addTest(orderedSetSuite("DoubleLinkedOpenHashSet", DoubleLinkedOpenHashSet::new, getFeatures()));
		suite.addTest(setSuite("DoubleOpenCustomHashSet", T -> new DoubleOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("DoubleLinkedOpenCustomHashSet", T -> new DoubleLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("ImmutableDoubleOpenHashSet", ImmutableDoubleOpenHashSet::new, getImmutableFeatures()));
		suite.addTest(setSuite("DoubleArraySet", DoubleArraySet::new, getFeatures()));
		suite.addTest(navigableSetSuite("DoubleRBTreeSet", DoubleRBTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("DoubleAVLTreeSet", DoubleAVLTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("Synchronized DoubleRBTreeSet", T -> new DoubleRBTreeSet(T).synchronize(), getFeatures()));
		suite.addTest(navigableSetSuite("Unmodifiable DoubleRBTreeSet", T -> new DoubleRBTreeSet(T).unmodifiable(), getImmutableFeatures()));
	}
		
	public static Test setSuite(String name, Function<double[], DoubleSet> factory, Collection<Feature<?>> features) {
		return DoubleSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<double[], DoubleOrderedSet> factory, Collection<Feature<?>> features) {
		return DoubleOrderedSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}

	
	public static Test navigableSetSuite(String name, Function<double[], DoubleNavigableSet> factory, Collection<Feature<?>> features) {
		return DoubleNavigableSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements DoubleStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(double o) { return Double.hashCode(o); }
		@Override
		public boolean equals(double key, double value) { return Double.doubleToLongBits(key) == Double.doubleToLongBits(value); }
	}

	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}
	
	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}