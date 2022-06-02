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
import speiger.src.collections.doubles.utils.DoubleSets;
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
		suite.addTest(setSuite("DoubleOpenHashSet", DoubleOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("DoubleLinkedOpenHashSet", DoubleLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("DoubleOpenCustomHashSet", T -> new DoubleOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("DoubleLinkedOpenCustomHashSet", T -> new DoubleLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableDoubleOpenHashSet", ImmutableDoubleOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("DoubleArraySet", DoubleArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("DoubleRBTreeSet", DoubleRBTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("DoubleAVLTreeSet", DoubleAVLTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("Synchronized DoubleRBTreeSet", T -> new DoubleRBTreeSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(navigableSetSuite("Unmodifiable DoubleRBTreeSet", T -> new DoubleRBTreeSet(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(setSuite("Empty DoubleSet", T -> DoubleSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton DoubleSet", T -> DoubleSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized DoubleLinkedOpenHashSet", T -> new DoubleLinkedOpenHashSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable DoubleLinkedOpenHashSet", T -> new DoubleLinkedOpenHashSet(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<double[], DoubleSet> factory, Collection<Feature<?>> features, int size) {
		return DoubleSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Sets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<double[], DoubleOrderedSet> factory, Collection<Feature<?>> features, int size) {
		return DoubleOrderedSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<double[], DoubleNavigableSet> factory, Collection<Feature<?>> features, int size) {
		return DoubleNavigableSetTestSuiteBuilder.using(new SimpleDoubleTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements DoubleStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(double o) { return Double.hashCode(o); }
		@Override
		public boolean equals(double key, double value) { return Double.doubleToLongBits(key) == Double.doubleToLongBits(value); }
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