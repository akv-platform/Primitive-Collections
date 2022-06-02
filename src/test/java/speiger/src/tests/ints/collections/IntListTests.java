package speiger.src.tests.ints.collections;

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
import speiger.src.collections.ints.lists.ImmutableIntList;
import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntLinkedList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.collections.ints.utils.IntLists;
import speiger.src.testers.ints.builder.IntListTestSuiteBuilder;
import speiger.src.testers.ints.impl.SimpleIntTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.ints.tests.list.IntListSubListTester;

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
		suite.addTest(listSuite("IntArrayList", IntArrayList::new, getFeatures(), -1));
		suite.addTest(listSuite("IntLinkedList", IntLinkedList::new, getFeatures(), -1));
		suite.addTest(listSuite("ImmutableIntList", ImmutableIntList::new, getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized IntArrayList", T -> new IntArrayList(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable IntArrayList", T -> new IntArrayList(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty IntList", T -> IntLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton IntList", T -> IntLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<int[], IntList> factory, Collection<Feature<?>> features, int size) {
		return IntListTestSuiteBuilder.using(new SimpleIntTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<int[], IntList> factory, Collection<Feature<?>> features, int size) {
		return IntListTestSuiteBuilder.using(new SimpleIntTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(IntListSubListTester.class)).withFeatures(features).createTestSuite();
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