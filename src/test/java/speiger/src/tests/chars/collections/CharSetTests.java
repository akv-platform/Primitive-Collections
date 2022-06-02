package speiger.src.tests.chars.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.sets.ImmutableCharOpenHashSet;
import speiger.src.collections.chars.sets.CharAVLTreeSet;
import speiger.src.collections.chars.sets.CharArraySet;
import speiger.src.collections.chars.sets.CharLinkedOpenCustomHashSet;
import speiger.src.collections.chars.sets.CharLinkedOpenHashSet;
import speiger.src.collections.chars.sets.CharNavigableSet;
import speiger.src.collections.chars.sets.CharOpenCustomHashSet;
import speiger.src.collections.chars.sets.CharOpenHashSet;
import speiger.src.collections.chars.sets.CharOrderedSet;
import speiger.src.collections.chars.sets.CharRBTreeSet;
import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.collections.chars.utils.CharStrategy;
import speiger.src.testers.chars.builder.CharNavigableSetTestSuiteBuilder;
import speiger.src.testers.chars.builder.CharOrderedSetTestSuiteBuilder;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.impl.SimpleCharTestGenerator;
import speiger.src.testers.utils.SpecialFeature;


@SuppressWarnings("javadoc")
public class CharSetTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("CharSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("CharOpenHashSet", CharOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("CharLinkedOpenHashSet", CharLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("CharOpenCustomHashSet", T -> new CharOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("CharLinkedOpenCustomHashSet", T -> new CharLinkedOpenCustomHashSet(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableCharOpenHashSet", ImmutableCharOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("CharArraySet", CharArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("CharRBTreeSet", CharRBTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("CharAVLTreeSet", CharAVLTreeSet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("Synchronized CharRBTreeSet", T -> new CharRBTreeSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(navigableSetSuite("Unmodifiable CharRBTreeSet", T -> new CharRBTreeSet(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(setSuite("Empty CharSet", T -> CharSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton CharSet", T -> CharSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized CharLinkedOpenHashSet", T -> new CharLinkedOpenHashSet(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable CharLinkedOpenHashSet", T -> new CharLinkedOpenHashSet(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<char[], CharSet> factory, Collection<Feature<?>> features, int size) {
		return CharSetTestSuiteBuilder.using(new SimpleCharTestGenerator.Sets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test orderedSetSuite(String name, Function<char[], CharOrderedSet> factory, Collection<Feature<?>> features, int size) {
		return CharOrderedSetTestSuiteBuilder.using(new SimpleCharTestGenerator.OrderedSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	public static Test navigableSetSuite(String name, Function<char[], CharNavigableSet> factory, Collection<Feature<?>> features, int size) {
		return CharNavigableSetTestSuiteBuilder.using(new SimpleCharTestGenerator.NavigableSets(factory)).named(name)
			.withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static class HashStrategy implements CharStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(char o) { return Character.hashCode(o); }
		@Override
		public boolean equals(char key, char value) { return key == value; }
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