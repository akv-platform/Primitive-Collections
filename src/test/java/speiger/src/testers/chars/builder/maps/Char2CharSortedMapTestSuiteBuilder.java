package speiger.src.testers.chars.builder.maps;

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
import speiger.src.testers.chars.generators.maps.TestChar2CharSortedMapGenerator;
import speiger.src.testers.chars.impl.maps.DerivedChar2CharMapGenerators;
import speiger.src.testers.chars.tests.maps.Char2CharSortedMapNavigationTester;
import speiger.src.testers.chars.builder.CharSetTestSuiteBuilder;
import speiger.src.testers.chars.builder.CharSortedSetTestSuiteBuilder;
import speiger.src.testers.chars.generators.TestCharSetGenerator;
import speiger.src.testers.chars.generators.TestCharSortedSetGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class Char2CharSortedMapTestSuiteBuilder extends Char2CharMapTestSuiteBuilder {
	public static Char2CharSortedMapTestSuiteBuilder using(TestChar2CharSortedMapGenerator generator) {
		return (Char2CharSortedMapTestSuiteBuilder) new Char2CharSortedMapTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = super.getTesters();
		testers.add(Char2CharSortedMapNavigationTester.class);
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
	protected List<TestSuite> createDerivedSuites(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Character>, Map.Entry<Character, Character>>> parentBuilder) {
		List<TestSuite> derivedSuites = super.createDerivedSuites(parentBuilder);
		if (!parentBuilder.getFeatures().contains(NoRecurse.SUBMAP)) {
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.NO_BOUND, Bound.EXCLUSIVE));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.NO_BOUND));
			derivedSuites.add(createSubmapSuite(parentBuilder, Bound.INCLUSIVE, Bound.EXCLUSIVE));
		}
		return derivedSuites;
	}
	
	@Override
	protected CharSetTestSuiteBuilder createDerivedKeySetSuite(TestCharSetGenerator keySetGenerator) {
		return keySetGenerator instanceof TestCharSortedSetGenerator ? CharSortedSetTestSuiteBuilder.using((TestCharSortedSetGenerator) keySetGenerator) : CharSetTestSuiteBuilder.using(keySetGenerator);
	}

	TestSuite createSubmapSuite(FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<Map<Character, Character>, Map.Entry<Character, Character>>> parentBuilder, Bound from, Bound to) {
		TestChar2CharSortedMapGenerator delegate = (TestChar2CharSortedMapGenerator) parentBuilder.getSubjectGenerator().getInnerGenerator();
		List<Feature<?>> features = new ArrayList<>();
		features.add(NoRecurse.SUBMAP);
		features.addAll(parentBuilder.getFeatures());
		features.remove(SpecialFeature.COPYING);

		return newBuilderUsing(delegate, to, from).named(parentBuilder.getName() + " subMap " + from + "-" + to)
				.withFeatures(features).suppressing(parentBuilder.getSuppressedTests())
				.withSetUp(parentBuilder.getSetUp()).withTearDown(parentBuilder.getTearDown()).createTestSuite();
	}

	Char2CharSortedMapTestSuiteBuilder newBuilderUsing(TestChar2CharSortedMapGenerator delegate, Bound to, Bound from) {
		return using(new DerivedChar2CharMapGenerators.SortedMapGenerator(delegate, to, from));
	}

	enum NoRecurse implements Feature<Void> {
		SUBMAP, DESCENDING;

		@Override
		public Set<Feature<? super Void>> getImpliedFeatures() {
			return Collections.emptySet();
		}
	}
}