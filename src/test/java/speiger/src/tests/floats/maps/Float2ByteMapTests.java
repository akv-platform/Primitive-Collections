package speiger.src.tests.floats.maps;

import java.util.function.BiFunction;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.maps.impl.concurrent.Float2ByteConcurrentOpenHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ByteLinkedOpenHashMap;
import speiger.src.collections.floats.maps.impl.customHash.Float2ByteOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.customHash.Float2ByteLinkedOpenCustomHashMap;
import speiger.src.collections.floats.maps.impl.hash.Float2ByteOpenHashMap;
import speiger.src.collections.floats.maps.impl.misc.Float2ByteArrayMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ByteAVLTreeMap;
import speiger.src.collections.floats.maps.impl.tree.Float2ByteRBTreeMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteMap;
import speiger.src.collections.floats.maps.interfaces.Float2ByteSortedMap;
import speiger.src.collections.floats.utils.FloatStrategy;
import speiger.src.testers.floats.builder.maps.Float2ByteMapTestSuiteBuilder;
import speiger.src.testers.floats.builder.maps.Float2ByteNavigableMapTestSuiteBuilder;
import speiger.src.testers.floats.impl.maps.SimpleFloat2ByteMapTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Float2ByteMapTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("Float2ByteMaps");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(mapSuite("Float2ByteOpenHashMap", Float2ByteOpenHashMap::new));
		suite.addTest(mapSuite("Float2ByteLinkedOpenHashMap", Float2ByteLinkedOpenHashMap::new));
		suite.addTest(mapSuite("Float2ByteOpenCustomHashMap", (K, V) -> new Float2ByteOpenCustomHashMap(K, V, HashStrategy.INSTANCE)));
		suite.addTest(mapSuite("Float2ByteLinkedOpenCustomHashMap", (K, V) -> new Float2ByteLinkedOpenCustomHashMap(K, V, HashStrategy.INSTANCE)));
		suite.addTest(mapSuite("Float2ByteArrayMap", Float2ByteArrayMap::new));
		suite.addTest(mapSuite("Float2ByteConcurrentOpenHashMap", Float2ByteConcurrentOpenHashMap::new));
		suite.addTest(navigableMapSuite("Float2ByteRBTreeMap", Float2ByteRBTreeMap::new));
		suite.addTest(navigableMapSuite("Float2ByteAVLTreeMap", Float2ByteAVLTreeMap::new));
	}
	
	private static Test mapSuite(String name, BiFunction<float[], byte[], Float2ByteMap> factory) {
		Float2ByteMapTestSuiteBuilder builder = Float2ByteMapTestSuiteBuilder.using(new SimpleFloat2ByteMapTestGenerator.Maps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static Test navigableMapSuite(String name, BiFunction<float[], byte[], Float2ByteSortedMap> factory) {
		Float2ByteNavigableMapTestSuiteBuilder builder = Float2ByteNavigableMapTestSuiteBuilder.using(new SimpleFloat2ByteMapTestGenerator.SortedMaps(factory));
		builder.withFeatures(MapFeature.GENERAL_PURPOSE, CollectionSize.ANY, CollectionFeature.SUPPORTS_ITERATOR_REMOVE, SpecialFeature.COPYING);
		return builder.named(name).createTestSuite();
	}
	
	private static class HashStrategy implements FloatStrategy {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(float o) { return Float.hashCode(o); }
		@Override
		public boolean equals(float key, float value) { return Float.floatToIntBits(key) == Float.floatToIntBits(value); }
	}
}