package speiger.src.tests.doubles.collections;

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
import speiger.src.collections.doubles.lists.ImmutableDoubleList;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleLinkedList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.collections.doubles.utils.DoubleLists;
import speiger.src.testers.doubles.builder.DoubleListTestSuiteBuilder;
import speiger.src.testers.doubles.impl.SimpleDoubleTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.doubles.tests.list.DoubleListSubListTester;

@SuppressWarnings("javadoc")
public class DoubleListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("DoubleLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("DoubleArrayList", DoubleArrayList::new, getFeatures(), -1));
		suite.addTest(listSuite("DoubleLinkedList", DoubleLinkedList::new, getFeatures(), -1));
		suite.addTest(listSuite("ImmutableDoubleList", ImmutableDoubleList::new, getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized DoubleArrayList", T -> new DoubleArrayList(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable DoubleArrayList", T -> new DoubleArrayList(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty DoubleList", T -> DoubleLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton DoubleList", T -> DoubleLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<double[], DoubleList> factory, Collection<Feature<?>> features, int size) {
		return DoubleListTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<double[], DoubleList> factory, Collection<Feature<?>> features, int size) {
		return DoubleListTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(DoubleListSubListTester.class)).withFeatures(features).createTestSuite();
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