package speiger.src.testers.ints.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.ints.generators.TestIntCollectionGenerator;
import speiger.src.testers.ints.tests.collection.IntCollectionAddAllArrayTester;
import speiger.src.testers.ints.tests.collection.IntCollectionAddAllTester;
import speiger.src.testers.ints.tests.collection.IntCollectionAddTester;
import speiger.src.testers.ints.tests.collection.IntCollectionClearTester;
import speiger.src.testers.ints.tests.collection.IntCollectionContainsAllTester;
import speiger.src.testers.ints.tests.collection.IntCollectionContainsAnyTester;
import speiger.src.testers.ints.tests.collection.IntCollectionContainsTester;
import speiger.src.testers.ints.tests.collection.IntCollectionCopyTester;
import speiger.src.testers.ints.tests.collection.IntCollectionEqualsTester;
import speiger.src.testers.ints.tests.collection.IntCollectionForEachTester;
import speiger.src.testers.ints.tests.collection.IntCollectionIteratorTester;
import speiger.src.testers.ints.tests.collection.IntCollectionRemoveAllTester;
import speiger.src.testers.ints.tests.collection.IntCollectionRemoveIfTester;
import speiger.src.testers.ints.tests.collection.IntCollectionStreamTester;
import speiger.src.testers.ints.tests.collection.IntCollectionRetainAllTester;
import speiger.src.testers.ints.tests.collection.IntCollectionToArrayTester;
import speiger.src.testers.ints.tests.iterable.IntIterableCountTester;
import speiger.src.testers.ints.tests.iterable.IntIterableDistinctTester;
import speiger.src.testers.ints.tests.iterable.IntIterableFilterTester;
import speiger.src.testers.ints.tests.iterable.IntIterableFindFirstTester;
import speiger.src.testers.ints.tests.iterable.IntIterableLimitTester;
import speiger.src.testers.ints.tests.iterable.IntIterableMapTester;
import speiger.src.testers.ints.tests.iterable.IntIterableMatchesTester;
import speiger.src.testers.ints.tests.iterable.IntIterablePeekTester;
import speiger.src.testers.ints.tests.iterable.IntIterableReduceTester;
import speiger.src.testers.ints.tests.iterable.IntIterableSortedTester;

@SuppressWarnings("javadoc")
public class IntCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Integer> {
	public static IntCollectionTestSuiteBuilder using(TestIntCollectionGenerator generator) {
		return (IntCollectionTestSuiteBuilder) new IntCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(IntIterableMapTester.class);
		testers.add(IntIterableFilterTester.class);
		testers.add(IntIterableDistinctTester.class);
		testers.add(IntIterableLimitTester.class);
		testers.add(IntIterableSortedTester.class);
		testers.add(IntIterableMatchesTester.class);
		testers.add(IntIterablePeekTester.class);
		testers.add(IntIterableReduceTester.class);
		testers.add(IntIterableCountTester.class);
		testers.add(IntIterableFindFirstTester.class);
		testers.add(IntCollectionAddAllTester.class);
		testers.add(IntCollectionAddAllArrayTester.class);
		testers.add(IntCollectionAddTester.class);
		testers.add(IntCollectionClearTester.class);
		testers.add(IntCollectionContainsAllTester.class);
		testers.add(IntCollectionContainsAnyTester.class);
		testers.add(IntCollectionContainsTester.class);
		testers.add(IntCollectionCopyTester.class);
		testers.add(IntCollectionEqualsTester.class);
		testers.add(IntCollectionForEachTester.class);
		testers.add(IntCollectionIteratorTester.class);
		testers.add(IntCollectionRemoveAllTester.class);
		testers.add(IntCollectionRetainAllTester.class);
		testers.add(IntCollectionRemoveIfTester.class);
		testers.add(IntCollectionStreamTester.class);
		testers.add(IntCollectionToArrayTester.class);
		return testers;
	}
}