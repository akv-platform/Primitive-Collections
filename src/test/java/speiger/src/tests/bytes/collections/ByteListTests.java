package speiger.src.tests.bytes.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.bytes.lists.ImmutableByteList;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteLinkedList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.builder.ByteListTestSuiteBuilder;
import speiger.src.testers.bytes.impl.SimpleByteTestGenerator;
import speiger.src.testers.utils.SpecialFeature;

@SuppressWarnings("javadoc")
public class ByteListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("ByteLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("ByteArrayList", ByteArrayList::new, getFeatures()));
		suite.addTest(listSuite("ByteLinkedList", ByteLinkedList::new, getFeatures()));
		suite.addTest(listSuite("ImmutableByteList", ImmutableByteList::new, getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized ByteArrayList", T -> new ByteArrayList(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable ByteArrayList", T -> new ByteArrayList(T).unmodifiable(), getImmutableFeatures()));
	}
	
	private static Test listSuite(String name, Function<byte[], ByteList> factory, Collection<Feature<?>> features) {
		return ByteListTestSuiteBuilder.using(new SimpleByteTestGenerator.Lists(factory)).named(name)
				.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}