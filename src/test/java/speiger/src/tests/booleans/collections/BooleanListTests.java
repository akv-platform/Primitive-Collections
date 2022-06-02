package speiger.src.tests.booleans.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.testers.CollectionAddAllTester;
import com.google.common.collect.testing.testers.CollectionAddTester;
import com.google.common.collect.testing.testers.CollectionContainsAllTester;
import com.google.common.collect.testing.testers.CollectionContainsTester;
import com.google.common.collect.testing.testers.CollectionIteratorTester;
import com.google.common.collect.testing.testers.CollectionRemoveAllTester;
import com.google.common.collect.testing.testers.CollectionRemoveTester;
import com.google.common.collect.testing.testers.CollectionRetainAllTester;
import com.google.common.collect.testing.testers.ListAddAllAtIndexTester;
import com.google.common.collect.testing.testers.ListAddAtIndexTester;
import com.google.common.collect.testing.testers.ListEqualsTester;
import com.google.common.collect.testing.testers.ListIndexOfTester;
import com.google.common.collect.testing.testers.ListLastIndexOfTester;
import com.google.common.collect.testing.testers.ListRetainAllTester;
import com.google.common.collect.testing.testers.ListSubListTester;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.booleans.lists.ImmutableBooleanList;
import speiger.src.collections.booleans.lists.BooleanArrayList;
import speiger.src.collections.booleans.lists.BooleanLinkedList;
import speiger.src.collections.booleans.lists.BooleanList;
import speiger.src.testers.booleans.builder.BooleanListTestSuiteBuilder;
import speiger.src.testers.booleans.impl.SimpleBooleanTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllArrayTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionAddTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionContainsAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionContainsTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionIteratorTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionRemoveAllTester;
import speiger.src.testers.booleans.tests.collection.BooleanCollectionRetainAllTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAllArrayAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAllAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListAddAtIndexTester;
import speiger.src.testers.booleans.tests.list.BooleanListEqualsTester;
import speiger.src.testers.booleans.tests.list.BooleanListExtractElementsTester;
import speiger.src.testers.booleans.tests.list.BooleanListIndexOfTester;
import speiger.src.testers.booleans.tests.list.BooleanListLastIndexOfTester;
import speiger.src.testers.booleans.tests.list.BooleanListRemoveElementsTester;
import speiger.src.testers.booleans.tests.list.BooleanListRetainAllTester;
import speiger.src.testers.booleans.tests.list.BooleanListSubListTester;

@SuppressWarnings("javadoc")
public class BooleanListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("BooleanLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		listSuite(suite, "BooleanArrayList", BooleanArrayList::new, getFeatures(), false, -1);
		listSuite(suite, "BooleanLinkedList", BooleanLinkedList::new, getFeatures(), false, -1);
		listSuite(suite, "ImmutableBooleanList", ImmutableBooleanList::new, getImmutableFeatures(), true, -1);
		listSuite(suite, "Synchronized BooleanArrayList", T -> new BooleanArrayList(T).synchronize(), getFeatures(), false, -1);
		listSuite(suite, "Unmodifiable BooleanArrayList", T -> new BooleanArrayList(T).unmodifiable(), getImmutableFeatures(), true, -1);
	}
	
	private static void listSuite(TestSuite suite, String name, Function<boolean[], BooleanList> factory, Collection<Feature<?>> features, boolean immutable, int size) {
		TestSuite data = new TestSuite(name);
		Collection<CollectionSize> sizes = getSizes(size);
		if(sizes.contains(CollectionSize.ZERO) || sizes.contains(CollectionSize.ANY)) {
			data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory))
			.named(name+" [collection size: zero]").withFeatures(CollectionSize.ZERO).withFeatures(features).suppressing(getSurpression(CollectionSize.ZERO, immutable)).createTestSuite());
		}
		if(sizes.contains(CollectionSize.ONE) || sizes.contains(CollectionSize.ANY)) {
			data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory))
				.named(name+" [collection size: one]").withFeatures(CollectionSize.ONE).withFeatures(features).suppressing(getSurpression(CollectionSize.ONE, immutable)).createTestSuite());
		}
		if(sizes.contains(CollectionSize.SEVERAL) || sizes.contains(CollectionSize.ANY)) {
			data.addTest(BooleanListTestSuiteBuilder.using(new SimpleBooleanTestGenerator.Lists(factory)).named(name)
				.named(name+" [collection size: several]").withFeatures(CollectionSize.SEVERAL).withFeatures(features).suppressing(getSurpression(CollectionSize.SEVERAL, immutable)).createTestSuite());
		}
		suite.addTest(data);
	}
	
	private static List<Method> getSurpression(CollectionSize size, boolean immutable) {
		List<Method> list = new ArrayList<>();
		if(size == CollectionSize.ONE) {
			TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty");
			if(immutable) {
				TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
			}
		}
		else {
			TestUtils.getSurpession(list, CollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
			TestUtils.getSurpession(list, CollectionContainsTester.class, "testContains_no");
			TestUtils.getSurpession(list, CollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
			TestUtils.getSurpession(list, CollectionRemoveAllTester.class, "testRemoveAll_nonePresent");
			TestUtils.getSurpession(list, CollectionRemoveTester.class, "testRemove_present", "testRemove_notPresent");
			TestUtils.getSurpession(list, CollectionRetainAllTester.class, "testRetainAll_disjointPreviouslyNonEmpty", "testRetainAll_containsDuplicatesSizeSeveral", "testRetainAll_partialOverlap");
			TestUtils.getSurpession(list, BooleanCollectionContainsAllTester.class, "testContainsAll_disjoint", "testContainsAll_partialOverlap");
			TestUtils.getSurpession(list, BooleanCollectionContainsTester.class, "testContains_no");
			TestUtils.getSurpession(list, BooleanCollectionIteratorTester.class, "testIterator_removeAffectsBackingCollection");
			TestUtils.getSurpession(list, BooleanCollectionRemoveAllTester.class, "testRemoveAll_nonePresentFetchRemoved", "testRemoveAll_someFetchRemovedElements", "testRemoveAll_nonePresent");
			TestUtils.getSurpession(list, BooleanCollectionRetainAllTester.class, "testRetainAllExtra_containsDuplicatesSizeSeveral", "testRetainAllExtra_partialOverlap", "testRetainAll_containsDuplicatesSizeSeveral", "testRetainAll_partialOverlap");
			TestUtils.getSurpession(list, ListAddAllAtIndexTester.class, "testAddAllAtIndex_negative", "testAddAllAtIndex_tooLarge");
			TestUtils.getSurpession(list, ListAddAtIndexTester.class, "testAddAtIndex_tooLarge", "testAddAtIndex_negative");
			TestUtils.getSurpession(list, ListEqualsTester.class, "testEquals_otherListWithDifferentElements");
			TestUtils.getSurpession(list, ListIndexOfTester.class, "testFind_no");
			TestUtils.getSurpession(list, ListLastIndexOfTester.class, "testLastIndexOf_duplicate", "testFind_no", "testFind_yes");
			TestUtils.getSurpession(list, ListRetainAllTester.class, "testRetainAll_duplicatesRemoved", "testRetainAll_countIgnored");
			TestUtils.getSurpession(list, ListSubListTester.class, "testSubList_lastIndexOf", "testSubList_contains", "testSubList_indexOf");
			TestUtils.getSurpession(list, BooleanListAddAllAtIndexTester.class, "testAddAllAtIndex_negative", "testAddAllAtIndex_tooLarge");
			TestUtils.getSurpession(list, BooleanListAddAllArrayAtIndexTester.class, "testAddAllArrayAtIndex_tooLarge", "testAddAllArrayAtIndex_negative");
			TestUtils.getSurpession(list, BooleanListAddAtIndexTester.class, "testAddAtIndex_tooLarge", "testAddAtIndex_negative");
			TestUtils.getSurpession(list, BooleanListEqualsTester.class, "testEquals_otherListWithDifferentElements");
			TestUtils.getSurpession(list, BooleanListExtractElementsTester.class, "testRemoveElements");
			TestUtils.getSurpession(list, BooleanListIndexOfTester.class, "testFind_no");
			TestUtils.getSurpession(list, BooleanListLastIndexOfTester.class, "testLastIndexOf_duplicate", "testFind_no", "testFind_yes");
			TestUtils.getSurpession(list, BooleanListRemoveElementsTester.class, "testRemoveElements");
			TestUtils.getSurpession(list, BooleanListRetainAllTester.class, "testRetainAll_duplicatesRemoved", "testRetainAll_countIgnored");
			TestUtils.getSurpession(list, BooleanListSubListTester.class, "testSubList_lastIndexOf", "testSubList_contains", "testSubList_indexOf");
			if(immutable) {
				TestUtils.getSurpession(list, CollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				TestUtils.getSurpession(list, CollectionAddTester.class, "testAdd_unsupportedNotPresent");
				TestUtils.getSurpession(list, CollectionRemoveTester.class, "testRemove_unsupportedNotPresent");
				TestUtils.getSurpession(list, BooleanCollectionAddAllTester.class, "testAddAll_unsupportedNonePresent");
				TestUtils.getSurpession(list, BooleanCollectionAddAllArrayTester.class, "testAddAllArray_unsupportedNonePresent");
				TestUtils.getSurpession(list, BooleanCollectionAddTester.class, "testAdd_unsupportedNotPresent");
				TestUtils.getSurpession(list, ListAddAllAtIndexTester.class, "testAddAllAtIndex_unsupportedSomePresent");
				TestUtils.getSurpession(list, ListAddAtIndexTester.class, "testAddAtIndex_unsupportedNotPresent");
				TestUtils.getSurpession(list, BooleanListAddAllAtIndexTester.class, "testAddAllAtIndex_unsupportedSomePresent");
				TestUtils.getSurpession(list, BooleanListAddAllArrayAtIndexTester.class, "testAddAllArrayAtIndex_unsupportedSomePresent");
				TestUtils.getSurpession(list, BooleanListAddAtIndexTester.class, "testAddAtIndex_unsupportedNotPresent");
			}
		}
		return list;
	}
	
	private static Collection<CollectionSize> getSizes(int size) {
		switch(size) {
			case 0: return Arrays.asList(CollectionSize.ZERO);
			case 1: return Arrays.asList(CollectionSize.ONE);
			case 2: return Arrays.asList(CollectionSize.ZERO, CollectionSize.ONE);
			case 3: return Arrays.asList(CollectionSize.SEVERAL);
			case 4: return Arrays.asList(CollectionSize.ZERO, CollectionSize.SEVERAL);
			case 5: return Arrays.asList(CollectionSize.ONE, CollectionSize.SEVERAL);
			default: return Arrays.asList(CollectionSize.ANY);
		}
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}