package speiger.src.tests.chars.collections;

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
import speiger.src.collections.chars.lists.ImmutableCharList;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharLinkedList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.collections.chars.utils.CharLists;
import speiger.src.testers.chars.builder.CharListTestSuiteBuilder;
import speiger.src.testers.chars.impl.SimpleCharTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.chars.tests.list.CharListSubListTester;

@SuppressWarnings("javadoc")
public class CharListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("CharLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("CharArrayList", CharArrayList::new, getFeatures(), -1));
		suite.addTest(listSuite("CharLinkedList", CharLinkedList::new, getFeatures(), -1));
		suite.addTest(listSuite("ImmutableCharList", ImmutableCharList::new, getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized CharArrayList", T -> new CharArrayList(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable CharArrayList", T -> new CharArrayList(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty CharList", T -> CharLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton CharList", T -> CharLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<char[], CharList> factory, Collection<Feature<?>> features, int size) {
		return CharListTestSuiteBuilder.using(new SimpleCharTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<char[], CharList> factory, Collection<Feature<?>> features, int size) {
		return CharListTestSuiteBuilder.using(new SimpleCharTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(CharListSubListTester.class)).withFeatures(features).createTestSuite();
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