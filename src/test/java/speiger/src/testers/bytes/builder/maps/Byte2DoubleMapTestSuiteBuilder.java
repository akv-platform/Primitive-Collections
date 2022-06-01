package speiger.src.testers.bytes.builder.maps;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.MapTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.MapFeature;
import com.google.common.collect.testing.testers.CollectionIteratorTester;

import junit.framework.TestSuite;
import speiger.src.collections.bytes.maps.interfaces.Byte2DoubleMap;
import speiger.src.testers.doubles.builder.DoubleCollectionTestSuiteBuilder;
import speiger.src.testers.doubles.generators.TestDoubleCollectionGenerator;
import speiger.src.testers.bytes.builder.ByteSetTestSuiteBuilder;
import speiger.src.testers.bytes.generators.TestByteSetGenerator;
import speiger.src.testers.bytes.generators.maps.TestByte2DoubleMapGenerator;
import speiger.src.testers.bytes.impl.maps.DerivedByte2DoubleMapGenerators;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapAddToTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapClearTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapComputeIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapComputeIfPresentTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapComputeTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapContainsKeyTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapContainsValueTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapEntrySetTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapEqualsTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapForEachTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapGetOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapGetTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapHashCodeTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapIsEmptyTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapMergeTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapPutAllArrayTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapPutAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapPutIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapPutTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapRemoveEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapRemoveOrDefaultTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapRemoveTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapReplaceAllTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapReplaceEntryTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapReplaceTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapSizeTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapSupplyIfAbsentTester;
import speiger.src.testers.bytes.tests.maps.Byte2DoubleMapToStringTester;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.tests.collection.ObjectCollectionIteratorTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRemoveAllTester;
import speiger.src.testers.objects.tests.collection.ObjectCollectionRetainAllTester;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;

@SuppressWarnings("javadoc")
public class Byte2DoubleMapTestSuiteBuilder extends MapTestSuiteBuilder<Byte, Double> {
	public static Byte2DoubleMapTestSuiteBuilder using(TestByte2DoubleMapGenerator generator) {
		return (Byte2DoubleMapTestSuiteBuilder) new Byte2DoubleMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = new ArrayList<>();
		testers.add(Byte2DoubleMapClearTester.class);
		testers.add(Byte2DoubleMapComputeTester.class);
		testers.add(Byte2DoubleMapComputeIfAbsentTester.class);
		testers.add(Byte2DoubleMapComputeIfPresentTester.class);
		testers.add(Byte2DoubleMapSupplyIfAbsentTester.class);
		testers.add(Byte2DoubleMapContainsKeyTester.class);
		testers.add(Byte2DoubleMapContainsValueTester.class);
		testers.add(Byte2DoubleMapEntrySetTester.class);
		testers.add(Byte2DoubleMapEqualsTester.class);
		testers.add(Byte2DoubleMapForEachTester.class);
		testers.add(Byte2DoubleMapGetTester.class);
		testers.add(Byte2DoubleMapGetOrDefaultTester.class);
		testers.add(Byte2DoubleMapHashCodeTester.class);
		testers.add(Byte2DoubleMapIsEmptyTester.class);
		testers.add(Byte2DoubleMapMergeTester.class);
		testers.add(Byte2DoubleMapPutTester.class);
		testers.add(Byte2DoubleMapAddToTester.class);
		testers.add(Byte2DoubleMapPutAllTester.class);
		testers.add(Byte2DoubleMapPutAllArrayTester.class);
		testers.add(Byte2DoubleMapPutIfAbsentTester.class);
		testers.add(Byte2DoubleMapRemoveTester.class);
		testers.add(Byte2DoubleMapRemoveEntryTester.class);
		testers.add(Byte2DoubleMapRemoveOrDefaultTester.class);
		testers.add(Byte2DoubleMapReplaceTester.class);
		testers.add(Byte2DoubleMapReplaceAllTester.class);
		testers.add(Byte2DoubleMapReplaceEntryTester.class);
		testers.add(Byte2DoubleMapSizeTester.class);
		testers.add(Byte2DoubleMapToStringTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Byte, Double>, Map.Entry<Byte, Double>>> parentBuilder) {
		List<TestSuite> derivedSuites = new ArrayList<>();
		derivedSuites.add(createDerivedEntrySetSuite(
				new DerivedByte2DoubleMapGenerators.MapEntrySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeEntrySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " entrySet")
						.suppressing(getEntrySetSuppressing(parentBuilder.getSuppressedTests()))
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());

		derivedSuites.add(createDerivedKeySetSuite(
				DerivedByte2DoubleMapGenerators.keySetGenerator(parentBuilder.getSubjectGenerator()))
						.withFeatures(computeKeySetFeatures(parentBuilder.getFeatures()))
						.named(parentBuilder.getName() + " keys").suppressing(parentBuilder.getSuppressedTests())
						.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown())
						.createTestSuite());
		derivedSuites.add(createDerivedValueCollectionSuite(
				new DerivedByte2DoubleMapGenerators.MapValueCollectionGenerator(parentBuilder.getSubjectGenerator()))
						.named(parentBuilder.getName() + " values")
						.withFeatures(computeValuesCollectionFeatures(parentBuilder.getFeatures()))
						.suppressing(parentBuilder.getSuppressedTests()).withSetUp(parentBuilder.getSetUp())
						.withTearDown(parentBuilder.getTearDown()).createTestSuite());
		return derivedSuites;
	}

	protected ObjectSetTestSuiteBuilder<Byte2DoubleMap.Entry> createDerivedEntrySetSuite(TestObjectSetGenerator<Byte2DoubleMap.Entry> entrySetGenerator) {
		return ObjectSetTestSuiteBuilder.using(entrySetGenerator);
	}
	
	protected ByteSetTestSuiteBuilder createDerivedKeySetSuite(TestByteSetGenerator generator) {
		return ByteSetTestSuiteBuilder.using(generator);
	}
	
	protected DoubleCollectionTestSuiteBuilder createDerivedValueCollectionSuite(TestDoubleCollectionGenerator generator) {
		return DoubleCollectionTestSuiteBuilder.using(generator);
	}
	
	private static Set<Feature<?>> computeEntrySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> entrySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_ENTRY_QUERIES)) {
			entrySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		entrySetFeatures.remove(SpecialFeature.COPYING);
		entrySetFeatures.add(SpecialFeature.MAP_ENTRY);
		return entrySetFeatures;
	}

	private static Set<Feature<?>> computeKeySetFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> keySetFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		keySetFeatures.add(CollectionFeature.SUBSET_VIEW);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEYS)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		} else if (mapFeatures.contains(MapFeature.ALLOWS_NULL_KEY_QUERIES)) {
			keySetFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		keySetFeatures.remove(SpecialFeature.COPYING);
		return keySetFeatures;
	}

	private static Set<Feature<?>> computeValuesCollectionFeatures(Set<Feature<?>> mapFeatures) {
		Set<Feature<?>> valuesCollectionFeatures = MapTestSuiteBuilder.computeCommonDerivedCollectionFeatures(mapFeatures);
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUE_QUERIES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_QUERIES);
		}
		if (mapFeatures.contains(MapFeature.ALLOWS_NULL_VALUES)) {
			valuesCollectionFeatures.add(CollectionFeature.ALLOWS_NULL_VALUES);
		}
		valuesCollectionFeatures.remove(SpecialFeature.COPYING);
		return valuesCollectionFeatures;
	}
	
	private static Set<Method> getEntrySetSuppressing(Set<Method> suppressing) {
		TestUtils.getSurpession(suppressing, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
		TestUtils.getSurpession(suppressing, ObjectCollectionRemoveAllTester.class, "testRemoveAll_someFetchRemovedElements");
		TestUtils.getSurpession(suppressing, ObjectCollectionRetainAllTester.class, "testRetainAllExtra_disjointPreviouslyNonEmpty", "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_subset", "testRetainAllExtra_partialOverlap");
		return suppressing;
	}
}