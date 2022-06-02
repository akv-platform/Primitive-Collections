package speiger.src.tests.shorts.collections;

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
import speiger.src.collections.shorts.lists.ImmutableShortList;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortLinkedList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.collections.shorts.utils.ShortLists;
import speiger.src.testers.shorts.builder.ShortListTestSuiteBuilder;
import speiger.src.testers.shorts.impl.SimpleShortTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.shorts.tests.list.ShortListSubListTester;

@SuppressWarnings("javadoc")
public class ShortListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("ShortLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("ShortArrayList", ShortArrayList::new, getFeatures(), -1));
		suite.addTest(listSuite("ShortLinkedList", ShortLinkedList::new, getFeatures(), -1));
		suite.addTest(listSuite("ImmutableShortList", ImmutableShortList::new, getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized ShortArrayList", T -> new ShortArrayList(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable ShortArrayList", T -> new ShortArrayList(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty ShortList", T -> ShortLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton ShortList", T -> ShortLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<short[], ShortList> factory, Collection<Feature<?>> features, int size) {
		return ShortListTestSuiteBuilder.using(new SimpleShortTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<short[], ShortList> factory, Collection<Feature<?>> features, int size) {
		return ShortListTestSuiteBuilder.using(new SimpleShortTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(ShortListSubListTester.class)).withFeatures(features).createTestSuite();
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