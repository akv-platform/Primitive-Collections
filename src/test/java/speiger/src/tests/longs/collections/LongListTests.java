package speiger.src.tests.longs.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.testers.ListSubListTester;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.lists.ImmutableLongList;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongLinkedList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.collections.longs.utils.LongLists;
import speiger.src.testers.longs.builder.LongListTestSuiteBuilder;
import speiger.src.testers.longs.impl.SimpleLongTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.longs.tests.list.LongListSubListTester;

@SuppressWarnings("javadoc")
public class LongListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("LongLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("LongArrayList", LongArrayList::new, getFeatures(), -1));
		suite.addTest(listSuite("LongLinkedList", LongLinkedList::new, getFeatures(), -1));
		suite.addTest(listSuite("ImmutableLongList", ImmutableLongList::new, getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized LongArrayList", T -> new LongArrayList(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable LongArrayList", T -> new LongArrayList(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty LongList", T -> LongLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton LongList", T -> LongLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<long[], LongList> factory, Collection<Feature<?>> features, int size) {
		return LongListTestSuiteBuilder.using(new SimpleLongTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<long[], LongList> factory, Collection<Feature<?>> features, int size) {
		return LongListTestSuiteBuilder.using(new SimpleLongTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(LongListSubListTester.class)).withFeatures(features).createTestSuite();
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