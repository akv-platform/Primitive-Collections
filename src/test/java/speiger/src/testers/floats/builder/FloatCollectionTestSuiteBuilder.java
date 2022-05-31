package speiger.src.testers.floats.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.floats.generators.TestFloatCollectionGenerator;
import speiger.src.testers.floats.tests.collection.FloatCollectionAddAllArrayTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionAddAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionAddTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionClearTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionContainsAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionContainsAnyTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionContainsTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionCopyTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionEqualsTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionForEachTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionIteratorTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionRemoveAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionRemoveIfTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionStreamTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionRetainAllTester;
import speiger.src.testers.floats.tests.collection.FloatCollectionToArrayTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableCountTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableDistinctTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableFilterTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableFindFirstTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableLimitTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableMapTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableMatchesTester;
import speiger.src.testers.floats.tests.iterable.FloatIterablePeekTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableReduceTester;
import speiger.src.testers.floats.tests.iterable.FloatIterableSortedTester;

@SuppressWarnings("javadoc")
public class FloatCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Float> {
	public static FloatCollectionTestSuiteBuilder using(TestFloatCollectionGenerator generator) {
		return (FloatCollectionTestSuiteBuilder) new FloatCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(FloatIterableMapTester.class);
		testers.add(FloatIterableFilterTester.class);
		testers.add(FloatIterableDistinctTester.class);
		testers.add(FloatIterableLimitTester.class);
		testers.add(FloatIterableSortedTester.class);
		testers.add(FloatIterableMatchesTester.class);
		testers.add(FloatIterablePeekTester.class);
		testers.add(FloatIterableReduceTester.class);
		testers.add(FloatIterableCountTester.class);
		testers.add(FloatIterableFindFirstTester.class);
		testers.add(FloatCollectionAddAllTester.class);
		testers.add(FloatCollectionAddAllArrayTester.class);
		testers.add(FloatCollectionAddTester.class);
		testers.add(FloatCollectionClearTester.class);
		testers.add(FloatCollectionContainsAllTester.class);
		testers.add(FloatCollectionContainsAnyTester.class);
		testers.add(FloatCollectionContainsTester.class);
		testers.add(FloatCollectionCopyTester.class);
		testers.add(FloatCollectionEqualsTester.class);
		testers.add(FloatCollectionForEachTester.class);
		testers.add(FloatCollectionIteratorTester.class);
		testers.add(FloatCollectionRemoveAllTester.class);
		testers.add(FloatCollectionRetainAllTester.class);
		testers.add(FloatCollectionRemoveIfTester.class);
		testers.add(FloatCollectionStreamTester.class);
		testers.add(FloatCollectionToArrayTester.class);
		return testers;
	}
}