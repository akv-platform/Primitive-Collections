package speiger.src.tests.doubles.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.doubles.lists.ImmutableDoubleList;
import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleLinkedList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.builder.DoubleListTestSuiteBuilder;
import speiger.src.testers.doubles.impl.SimpleDoubleTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

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
		suite.addTest(listSuite("DoubleArrayList", DoubleArrayList::new, getFeatures()));
		suite.addTest(listSuite("DoubleLinkedList", DoubleLinkedList::new, getFeatures()));
		suite.addTest(listSuite("ImmutableDoubleList", ImmutableDoubleList::new, getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized DoubleArrayList", T -> new DoubleArrayList(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable DoubleArrayList", T -> new DoubleArrayList(T).unmodifiable(), getImmutableFeatures()));
	}
	
	private static Test listSuite(String name, Function<double[], DoubleList> factory, Collection<Feature<?>> features) {
		return DoubleListTestSuiteBuilder.using(new SimpleDoubleTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}