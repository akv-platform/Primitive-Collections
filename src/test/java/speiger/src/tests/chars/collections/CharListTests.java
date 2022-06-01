package speiger.src.tests.chars.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.chars.lists.ImmutableCharList;
import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharLinkedList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.builder.CharListTestSuiteBuilder;
import speiger.src.testers.chars.impl.SimpleCharTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

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
		suite.addTest(listSuite("CharArrayList", CharArrayList::new, getFeatures()));
		suite.addTest(listSuite("CharLinkedList", CharLinkedList::new, getFeatures()));
		suite.addTest(listSuite("ImmutableCharList", ImmutableCharList::new, getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized CharArrayList", T -> new CharArrayList(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable CharArrayList", T -> new CharArrayList(T).unmodifiable(), getImmutableFeatures()));
	}
	
	private static Test listSuite(String name, Function<char[], CharList> factory, Collection<Feature<?>> features) {
		return CharListTestSuiteBuilder.using(new SimpleCharTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}