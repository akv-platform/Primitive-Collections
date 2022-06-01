package speiger.src.tests.shorts.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.shorts.lists.ImmutableShortList;
import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortLinkedList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.builder.ShortListTestSuiteBuilder;
import speiger.src.testers.shorts.impl.SimpleShortTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

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
		suite.addTest(listSuite("ShortArrayList", ShortArrayList::new, getFeatures()));
		suite.addTest(listSuite("ShortLinkedList", ShortLinkedList::new, getFeatures()));
		suite.addTest(listSuite("ImmutableShortList", ImmutableShortList::new, getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized ShortArrayList", T -> new ShortArrayList(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable ShortArrayList", T -> new ShortArrayList(T).unmodifiable(), getImmutableFeatures()));
	}
	
	private static Test listSuite(String name, Function<short[], ShortList> factory, Collection<Feature<?>> features) {
		return ShortListTestSuiteBuilder.using(new SimpleShortTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}