package speiger.src.tests.floats.collections;

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
import speiger.src.collections.floats.lists.ImmutableFloatList;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatLinkedList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.utils.FloatLists;
import speiger.src.testers.floats.builder.FloatListTestSuiteBuilder;
import speiger.src.testers.floats.impl.SimpleFloatTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.floats.tests.list.FloatListSubListTester;

@SuppressWarnings("javadoc")
public class FloatListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("FloatLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("FloatArrayList", FloatArrayList::new, getFeatures(), -1));
		suite.addTest(listSuite("FloatLinkedList", FloatLinkedList::new, getFeatures(), -1));
		suite.addTest(listSuite("ImmutableFloatList", ImmutableFloatList::new, getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized FloatArrayList", T -> new FloatArrayList(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable FloatArrayList", T -> new FloatArrayList(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty FloatList", T -> FloatLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton FloatList", T -> FloatLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<float[], FloatList> factory, Collection<Feature<?>> features, int size) {
		return FloatListTestSuiteBuilder.using(new SimpleFloatTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<float[], FloatList> factory, Collection<Feature<?>> features, int size) {
		return FloatListTestSuiteBuilder.using(new SimpleFloatTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(FloatListSubListTester.class)).withFeatures(features).createTestSuite();
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