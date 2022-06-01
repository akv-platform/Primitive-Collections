package speiger.src.tests.floats.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.floats.lists.ImmutableFloatList;
import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatLinkedList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.builder.FloatListTestSuiteBuilder;
import speiger.src.testers.floats.impl.SimpleFloatTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

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
		suite.addTest(listSuite("FloatArrayList", FloatArrayList::new, getFeatures()));
		suite.addTest(listSuite("FloatLinkedList", FloatLinkedList::new, getFeatures()));
		suite.addTest(listSuite("ImmutableFloatList", ImmutableFloatList::new, getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized FloatArrayList", T -> new FloatArrayList(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable FloatArrayList", T -> new FloatArrayList(T).unmodifiable(), getImmutableFeatures()));
	}
	
	private static Test listSuite(String name, Function<float[], FloatList> factory, Collection<Feature<?>> features) {
		return FloatListTestSuiteBuilder.using(new SimpleFloatTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}