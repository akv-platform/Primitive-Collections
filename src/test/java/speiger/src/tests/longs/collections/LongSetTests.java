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
import speiger.src.collections.longs.utils.LongSets;
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
		suite.addTest(setSuite("LongOpenHashSet", LongOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("LongLinkedOpenHashSet", LongLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("LongOpenCustomHashSet", T -> new LongOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("LongLinkedOpenCustomHashSet", T -> new LongLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableLongOpenHashSet", ImmutableLongOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("LongArraySet", LongArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("LongRBTreeSet", LongRBTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("LongAVLTreeSet", LongAVLTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("Synchronized LongRBTreeSet", T -> new LongRBTreeSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(navigableSetSuite("Unmodifiable LongRBTreeSet", T -> new LongRBTreeSet(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(setSuite("Empty LongSet", T -> LongSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton LongSet", T -> LongSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized LongLinkedOpenHashSet", T -> new LongLinkedOpenHashSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable LongLinkedOpenHashSet", T -> new LongLinkedOpenHashSet(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<long[], LongSet> factory, Collection<Feature<?>> features, int size) {
		return LongSetTestSuiteBuilder.using(new SimpleLongTestGenerator.Sets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<long[], LongOrderedSet> factory, Collection<Feature<?>> features, int size) {
		return LongOrderedSetTestSuiteBuilder.using(new SimpleLongTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<long[], LongNavigableSet> factory, Collection<Feature<?>> features, int size) {
		return LongNavigableSetTestSuiteBuilder.using(new SimpleLongTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements LongStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(long o) { return Long.hashCode(o); }
		@Override
		public boolean equals(long key, long value) { return key == value; }
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