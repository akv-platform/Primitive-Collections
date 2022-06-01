package speiger.src.tests.longs.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.sets.ImmutableLongOpenHashSet;
import speiger.src.collections.longs.sets.LongAVLTreeSet;
import speiger.src.collections.longs.sets.LongArraySet;
import speiger.src.collections.longs.sets.LongLinkedOpenCustomHashSet;
import speiger.src.collections.longs.sets.LongLinkedOpenHashSet;
import speiger.src.collections.longs.sets.LongNavigableSet;
import speiger.src.collections.longs.sets.LongOpenCustomHashSet;
import speiger.src.collections.longs.sets.LongOpenHashSet;
import speiger.src.collections.longs.sets.LongOrderedSet;
import speiger.src.collections.longs.sets.LongRBTreeSet;
import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongStrategy;
import speiger.src.testers.longs.builder.LongNavigableSetTestSuiteBuilder;
import speiger.src.testers.longs.builder.LongOrderedSetTestSuiteBuilder;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.impl.SimpleLongTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class LongSetTests extends TestCase
{
	
	public static Test suite() {
		TestSuite suite = new TestSuite("LongSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("LongOpenHashSet", LongOpenHashSet::new, getFeatures()));
		suite.addTest(orderedSetSuite("LongLinkedOpenHashSet", LongLinkedOpenHashSet::new, getFeatures()));
		suite.addTest(setSuite("LongOpenCustomHashSet", T -> new LongOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("LongLinkedOpenCustomHashSet", T -> new LongLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures()));
		suite.addTest(orderedSetSuite("ImmutableLongOpenHashSet", ImmutableLongOpenHashSet::new, getImmutableFeatures()));
		suite.addTest(setSuite("LongArraySet", LongArraySet::new, getFeatures()));
		suite.addTest(navigableSetSuite("LongRBTreeSet", LongRBTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("LongAVLTreeSet", LongAVLTreeSet::new, getFeatures()));
		suite.addTest(navigableSetSuite("Synchronized LongRBTreeSet", T -> new LongRBTreeSet(T).synchronize(), getFeatures()));
		suite.addTest(navigableSetSuite("Unmodifiable LongRBTreeSet", T -> new LongRBTreeSet(T).unmodifiable(), getImmutableFeatures()));
	}
		
	public static Test setSuite(String name, Function<long[], LongSet> factory, Collection<Feature<?>> features) {
		return LongSetTestSuiteBuilder.using(new SimpleLongTestGenerator.Sets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<long[], LongOrderedSet> factory, Collection<Feature<?>> features) {
		return LongOrderedSetTestSuiteBuilder.using(new SimpleLongTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}

	
	public static Test navigableSetSuite(String name, Function<long[], LongNavigableSet> factory, Collection<Feature<?>> features) {
		return LongNavigableSetTestSuiteBuilder.using(new SimpleLongTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements LongStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(long o) { return Long.hashCode(o); }
		@Override
		public boolean equals(long key, long value) { return key == value; }
	}

	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}
	
	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}