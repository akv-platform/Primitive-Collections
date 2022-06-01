package speiger.src.tests.longs.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.longs.lists.ImmutableLongList;
import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongLinkedList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.builder.LongListTestSuiteBuilder;
import speiger.src.testers.longs.impl.SimpleLongTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

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
		suite.addTest(listSuite("LongArrayList", LongArrayList::new, getFeatures()));
		suite.addTest(listSuite("LongLinkedList", LongLinkedList::new, getFeatures()));
		suite.addTest(listSuite("ImmutableLongList", ImmutableLongList::new, getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized LongArrayList", T -> new LongArrayList(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable LongArrayList", T -> new LongArrayList(T).unmodifiable(), getImmutableFeatures()));
	}
	
	private static Test listSuite(String name, Function<long[], LongList> factory, Collection<Feature<?>> features) {
		return LongListTestSuiteBuilder.using(new SimpleLongTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}