package speiger.src.testers.ints.builder.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.DerivedCollectionGenerators.Bound;
import com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder;
import com.google.common.collect.testing.OneSizeTestContainerGenerator;
import com.google.common.collect.testing.features.Feature;

import junit.framework.TestSuite;
import speiger.src.testers.ints.builder.IntNavigableSetTestSuiteBuilder;
import speiger.src.testers.ints.generators.TestIntSetGenerator;
import speiger.src.testers.ints.generators.TestIntNavigableSetGenerator;
import speiger.src.testers.ints.generators.maps.TestInt2IntSortedMapGenerator;
import speiger.src.testers.ints.impl.maps.DerivedInt2IntMapGenerators;
import speiger.src.testers.ints.tests.maps.Int2IntNavigableMapNavigationTester;
import speiger.src.testers.utils.SpecialFeature;

public class Int2IntNavigableMapTestSuiteBuilder extends Int2IntSortedMapTestSuiteBuilder
{
	public static Int2IntNavigableMapTestSuiteBuilder using(TestInt2IntSortedMapGenerator generator) {
		return (Int2IntNavigableMapTestSuiteBuilder)new Int2IntNavigableMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Int2IntNavigableMapNavigationTester.class);
		return testers;
	}

	@Override
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Integer>, Map.Entry<Integer, Integer>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.DESCENDING)) {
			derivedSuites.add(createDescendingSuite(parentBuilder));
		}
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.INCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.EXCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.EXCLUSIVE, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.EXCLUSIVE, Bound.INCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.INCLUSIVE));
		}
		return derivedSuites;
	}

	@Override
	Int2IntNavigableMapTestSuiteBuilder newBuilderUsing(TestInt2IntSortedMapGenerator delegate, Bound to, Bound from) {
		return Int2IntNavigableMapTestSuiteBuilder.using(new DerivedInt2IntMapGenerators.NavigableMapGenerator(delegate, to, from));
	}

	private TestSuite createDescendingSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Integer, Integer>, Map.Entry<Integer, Integer>>> parentBuilder) {
		TestInt2IntSortedMapGenerator delegate = (TestInt2IntSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();

		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.DESCENDING);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);
		return Int2IntNavigableMapTestSuiteBuilder.using(new DerivedInt2IntMapGenerators.DescendingTestMapGenerator(delegate))
				.named(parentBuilder.getName() + " descending").withFeatures(features)
				.suppressing(parentBuilder.getSuppressedTests()).createTestSuite();
	}

	@Override
	protected IntNavigableSetTestSuiteBuilder createDerivedKeySetSuite(TestIntSetGenerator keySetGenerator) {
		return IntNavigableSetTestSuiteBuilder.using((TestIntNavigableSetGenerator) keySetGenerator);
	}
}