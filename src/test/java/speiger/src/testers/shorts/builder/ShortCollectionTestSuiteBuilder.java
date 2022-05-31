package speiger.src.testers.shorts.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.shorts.generators.TestShortCollectionGenerator;
import speiger.src.testers.shorts.tests.collection.ShortCollectionAddAllArrayTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionAddAllTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionAddTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionClearTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionContainsAllTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionContainsAnyTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionContainsTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionCopyTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionEqualsTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionForEachTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionIteratorTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionRemoveAllTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionRemoveIfTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionStreamTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionRetainAllTester;
import speiger.src.testers.shorts.tests.collection.ShortCollectionToArrayTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableCountTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableDistinctTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableFilterTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableFindFirstTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableLimitTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableMapTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableMatchesTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterablePeekTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableReduceTester;
import speiger.src.testers.shorts.tests.iterable.ShortIterableSortedTester;

@SuppressWarnings("javadoc")
public class ShortCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Short> {
	public static ShortCollectionTestSuiteBuilder using(TestShortCollectionGenerator generator) {
		return (ShortCollectionTestSuiteBuilder) new ShortCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(ShortIterableMapTester.class);
		testers.add(ShortIterableFilterTester.class);
		testers.add(ShortIterableDistinctTester.class);
		testers.add(ShortIterableLimitTester.class);
		testers.add(ShortIterableSortedTester.class);
		testers.add(ShortIterableMatchesTester.class);
		testers.add(ShortIterablePeekTester.class);
		testers.add(ShortIterableReduceTester.class);
		testers.add(ShortIterableCountTester.class);
		testers.add(ShortIterableFindFirstTester.class);
		testers.add(ShortCollectionAddAllTester.class);
		testers.add(ShortCollectionAddAllArrayTester.class);
		testers.add(ShortCollectionAddTester.class);
		testers.add(ShortCollectionClearTester.class);
		testers.add(ShortCollectionContainsAllTester.class);
		testers.add(ShortCollectionContainsAnyTester.class);
		testers.add(ShortCollectionContainsTester.class);
		testers.add(ShortCollectionCopyTester.class);
		testers.add(ShortCollectionEqualsTester.class);
		testers.add(ShortCollectionForEachTester.class);
		testers.add(ShortCollectionIteratorTester.class);
		testers.add(ShortCollectionRemoveAllTester.class);
		testers.add(ShortCollectionRetainAllTester.class);
		testers.add(ShortCollectionRemoveIfTester.class);
		testers.add(ShortCollectionStreamTester.class);
		testers.add(ShortCollectionToArrayTester.class);
		return testers;
	}
}