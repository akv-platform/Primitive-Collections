package speiger.src.testers.bytes.builder;

import java.util.List;

import com.google.common.collect.testing.AbstractTester;
import com.google.common.collect.testing.CollectionTestSuiteBuilder;
import com.google.common.collect.testing.Helpers;

import speiger.src.testers.bytes.generators.TestByteCollectionGenerator;
import speiger.src.testers.bytes.tests.collection.ByteCollectionAddAllArrayTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionAddAllTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionAddTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionClearTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionContainsAllTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionContainsAnyTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionContainsTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionCopyTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionEqualsTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionForEachTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionIteratorTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionRemoveAllTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionRemoveIfTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionStreamTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionRetainAllTester;
import speiger.src.testers.bytes.tests.collection.ByteCollectionToArrayTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableCountTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableDistinctTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableFilterTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableFindFirstTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableLimitTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableMapTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableMatchesTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterablePeekTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableReduceTester;
import speiger.src.testers.bytes.tests.iterable.ByteIterableSortedTester;

@SuppressWarnings("javadoc")
public class ByteCollectionTestSuiteBuilder extends CollectionTestSuiteBuilder<Byte> {
	public static ByteCollectionTestSuiteBuilder using(TestByteCollectionGenerator generator) {
		return (ByteCollectionTestSuiteBuilder) new ByteCollectionTestSuiteBuilder().usingGenerator(generator);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected List<Class<? extends AbstractTester>> getTesters() {
		List<Class<? extends AbstractTester>> testers = Helpers.copyToList(super.getTesters());
		testers.add(ByteIterableMapTester.class);
		testers.add(ByteIterableFilterTester.class);
		testers.add(ByteIterableDistinctTester.class);
		testers.add(ByteIterableLimitTester.class);
		testers.add(ByteIterableSortedTester.class);
		testers.add(ByteIterableMatchesTester.class);
		testers.add(ByteIterablePeekTester.class);
		testers.add(ByteIterableReduceTester.class);
		testers.add(ByteIterableCountTester.class);
		testers.add(ByteIterableFindFirstTester.class);
		testers.add(ByteCollectionAddAllTester.class);
		testers.add(ByteCollectionAddAllArrayTester.class);
		testers.add(ByteCollectionAddTester.class);
		testers.add(ByteCollectionClearTester.class);
		testers.add(ByteCollectionContainsAllTester.class);
		testers.add(ByteCollectionContainsAnyTester.class);
		testers.add(ByteCollectionContainsTester.class);
		testers.add(ByteCollectionCopyTester.class);
		testers.add(ByteCollectionEqualsTester.class);
		testers.add(ByteCollectionForEachTester.class);
		testers.add(ByteCollectionIteratorTester.class);
		testers.add(ByteCollectionRemoveAllTester.class);
		testers.add(ByteCollectionRetainAllTester.class);
		testers.add(ByteCollectionRemoveIfTester.class);
		testers.add(ByteCollectionStreamTester.class);
		testers.add(ByteCollectionToArrayTester.class);
		return testers;
	}
}