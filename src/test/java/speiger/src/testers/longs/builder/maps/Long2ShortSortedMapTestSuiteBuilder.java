package speiger.src.testers.longs.builder.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.longs.generators.maps.TestLong2ShortSortedMapGenerator;
import speiger.src.testers.longs.impl.maps.DerivedLong2ShortMapGenerators;
import speiger.src.testers.longs.tests.maps.Long2ShortSortedMapNavigationTester;
import speiger.src.testers.longs.builder.LongSetTestSuiteBuilder;
import speiger.src.testers.longs.builder.LongSortedSetTestSuiteBuilder;
import speiger.src.testers.longs.generators.TestLongSetGenerator;
import speiger.src.testers.longs.generators.TestLongSortedSetGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Long2ShortSortedMapTestSuiteBuilder extends Long2ShortMapTestSuiteBuilder {
	public static Long2ShortSortedMapTestSuiteBuilder using(TestLong2ShortSortedMapGenerator generator) {
		return (Long2ShortSortedMapTestSuiteBuilder) new Long2ShortSortedMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Long2ShortSortedMapNavigationTester.class);
		return testers;
	}

	@Override
	public TestSuite createTestSuite() {
		if (!getFeatures().contains(KNOWN_ORDER)) {
			List<Feature<?>> features = Helpers.copyToList(getFeatures());
			features.add(KNOWN_ORDER);
			withFeatures(features);
		}
		return super.createTestSuite();
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}
		return derivedSuites;
	}
	
	@Override
	protected LongSetTestSuiteBuilder createDerivedKeySetSuite(TestLongSetGenerator keySetGenerator) {
		return keySetGenerator instanceof TestLongSortedSetGenerator ? LongSortedSetTestSuiteBuilder.using((TestLongSortedSetGenerator) keySetGenerator) : LongSetTestSuiteBuilder.using(keySetGenerator);
	}

	TestSuite createSubmapSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Long, Short>, Map.Entry<Long, Short>>> parentBuilder, Bound from, Bound to) {
		TestLong2ShortSortedMapGenerator delegate = (TestLong2ShortSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();
		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.SUBMAP);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subMap " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	Long2ShortSortedMapTestSuiteBuilder newBuilderUsing(TestLong2ShortSortedMapGenerator delegate, Bound to, Bound from) {
		return using(new DerivedLong2ShortMapGenerators.SortedMapGenerator(delegate, to, from));
	}

	enum NoRecurse implements Feature<Void> {
		SUBMAP, DESCENDING;

		@Override
		public Set<Feature<? super Void>> getImpliedFeatures() {
			return Collections.emptySet();
		}
	}
}