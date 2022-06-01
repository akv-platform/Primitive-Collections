package speiger.src.tests.chars.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.maps.impl.concurrent.Char2CharConcurrentOpenHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2CharLinkedOpenHashMap;
import speiger.src.collections.chars.maps.impl.customHash.Char2CharOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.customHash.Char2CharLinkedOpenCustomHashMap;
import speiger.src.collections.chars.maps.impl.hash.Char2CharOpenHashMap;
import speiger.src.collections.chars.maps.impl.misc.Char2CharArrayMap;
import speiger.src.collections.chars.maps.impl.tree.Char2CharAVLTreeMap;
import speiger.src.collections.chars.maps.impl.tree.Char2CharRBTreeMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharMap;
import speiger.src.collections.chars.maps.interfaces.Char2CharSortedMap;
import speiger.src.collections.chars.utils.CharStrategy;
import speiger.src.testers.chars.builder.maps.Char2CharMapTestSuiteBuilder;
import speiger.src.testers.chars.builder.maps.Char2CharNavigableMapTestSuiteBuilder;
import speiger.src.testers.chars.impl.maps.SimpleChar2CharMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Char2CharMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Char2CharMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Char2CharOpenHashMap", Char2CharOpenHashMap::new));
		suite.addTest(mapSuite("Char2CharLinkedOpenHashMap", Char2CharLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Char2CharOpenCustomHashMap", (K, V) -> new Char2CharOpenCustomHashMap(K, V, HashStrategy.INSTANCE)));
		suite.addTest(mapSuite("Char2CharLinkedOpenCustomHashMap", (K, V) -> new Char2CharLinkedOpenCustomHashMap(K, V, HashStrategy.INSTANCE)));
		suite.addTest(mapSuite("Char2CharArrayMap", Char2CharArrayMap::new));
		suite.addTest(mapSuite("Char2CharConcurrentOpenHashMap", Char2CharConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Char2CharRBTreeMap", Char2CharRBTreeMap::new));
		suite.addTest(navigableMapSuite("Char2CharAVLTreeMap", Char2CharAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<char[], char[], Char2CharMap> factory) {
		Char2CharMapTestSuiteBuilder builder = Char2CharMapTestSuiteBuilder.using(new SimpleChar2CharMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<char[], char[], Char2CharSortedMap> factory) {
		Char2CharNavigableMapTestSuiteBuilder builder = Char2CharNavigableMapTestSuiteBuilder.using(new SimpleChar2CharMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static class HashStrategy implements CharStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(char o) { return Character.hashCode(o); }
		@Override
		public boolean equals(char key, char value) { return key == value; }
	}
}