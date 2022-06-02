package speiger.src.tests.bytes.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.sets.ImmutableByteOpenHashSet;
import speiger.src.collections.bytes.sets.ByteAVLTreeSet;
import speiger.src.collections.bytes.sets.ByteArraySet;
import speiger.src.collections.bytes.sets.ByteLinkedOpenCustomHashSet;
import speiger.src.collections.bytes.sets.ByteLinkedOpenHashSet;
import speiger.src.collections.bytes.sets.ByteNavigableSet;
import speiger.src.collections.bytes.sets.ByteOpenCustomHashSet;
import speiger.src.collections.bytes.sets.ByteOpenHashSet;
import speiger.src.collections.bytes.sets.ByteOrderedSet;
import speiger.src.collections.bytes.sets.ByteRBTreeSet;
import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.collections.bytes.utils.ByteStrategy;
import speiger.src.testers.bytes.builder.ByteNavigableSetTestSuiteBuilder;
import speiger.src.testers.bytes.builder.ByteOrderedSetTestSuiteBuilder;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.impl.SimpleByteTestGenerator;
import speiger.src.testers.utils.SpecialFeature;


@SuppressWarnings("javadoc")
public class ByteSetTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("ByteSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("ByteOpenHashSet", ByteOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("ByteLinkedOpenHashSet", ByteLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("ByteOpenCustomHashSet", T -> new ByteOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ByteLinkedOpenCustomHashSet", T -> new ByteLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableByteOpenHashSet", ImmutableByteOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("ByteArraySet", ByteArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("ByteRBTreeSet", ByteRBTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("ByteAVLTreeSet", ByteAVLTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("Synchronized ByteRBTreeSet", T -> new ByteRBTreeSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(navigableSetSuite("Unmodifiable ByteRBTreeSet", T -> new ByteRBTreeSet(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(setSuite("Empty ByteSet", T -> ByteSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton ByteSet", T -> ByteSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized ByteLinkedOpenHashSet", T -> new ByteLinkedOpenHashSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable ByteLinkedOpenHashSet", T -> new ByteLinkedOpenHashSet(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<byte[], ByteSet> factory, Collection<Feature<?>> features, int size) {
		return ByteSetTestSuiteBuilder.using(new SimpleByteTestGenerator.Sets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<byte[], ByteOrderedSet> factory, Collection<Feature<?>> features, int size) {
		return ByteOrderedSetTestSuiteBuilder.using(new SimpleByteTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<byte[], ByteNavigableSet> factory, Collection<Feature<?>> features, int size) {
		return ByteNavigableSetTestSuiteBuilder.using(new SimpleByteTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements ByteStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(byte o) { return Byte.hashCode(o); }
		@Override
		public boolean equals(byte key, byte value) { return key == value; }
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