package speiger.src.tests.ints.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.ints.lists.ImmutableIntList;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntLinkedList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.builder.IntListTestSuiteBuilder;
import speiger.src.testers.ints.impl.SimpleIntTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class IntListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("IntLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("IntArrayList", IntArrayList::new, getFeatures()));
		suite.addTest(listSuite("IntLinkedList", IntLinkedList::new, getFeatures()));
		suite.addTest(listSuite("ImmutableIntList", ImmutableIntList::new, getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized IntArrayList", T -> new IntArrayList(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable IntArrayList", T -> new IntArrayList(T).unmodifiable(), getImmutableFeatures()));
	}
	
	private static Test listSuite(String name, Function<int[], IntList> factory, Collection<Feature<?>> features) {
		return IntListTestSuiteBuilder.using(new SimpleIntTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}