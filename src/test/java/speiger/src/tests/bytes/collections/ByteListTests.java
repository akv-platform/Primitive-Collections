package speiger.src.tests.bytes.collections;

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
import speiger.src.collections.bytes.lists.ImmutableByteList;
import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteLinkedList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.collections.bytes.utils.ByteLists;
import speiger.src.testers.bytes.builder.ByteListTestSuiteBuilder;
import speiger.src.testers.bytes.impl.SimpleByteTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.bytes.tests.list.ByteListSubListTester;

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
		suite.addTest(listSuite("ByteArrayList", ByteArrayList::new, getFeatures(), -1));
		suite.addTest(listSuite("ByteLinkedList", ByteLinkedList::new, getFeatures(), -1));
		suite.addTest(listSuite("ImmutableByteList", ImmutableByteList::new, getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized ByteArrayList", T -> new ByteArrayList(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable ByteArrayList", T -> new ByteArrayList(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty ByteList", T -> ByteLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton ByteList", T -> ByteLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<byte[], ByteList> factory, Collection<Feature<?>> features, int size) {
		return ByteListTestSuiteBuilder.using(new SimpleByteTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<byte[], ByteList> factory, Collection<Feature<?>> features, int size) {
		return ByteListTestSuiteBuilder.using(new SimpleByteTestGenerator.Lists(factory)).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(ByteListSubListTester.class)).withFeatures(features).createTestSuite();
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