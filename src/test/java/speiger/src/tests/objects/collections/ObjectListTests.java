package speiger.src.tests.objects.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.CollectionFeature;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.lists.ImmutableObjectList;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectLinkedList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.builder.ObjectListTestSuiteBuilder;
import speiger.src.testers.objects.impl.SimpleObjectTestGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.objects.utils.ObjectSamples;

@SuppressWarnings("javadoc")
public class ObjectListTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("ObjectLists");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}

	public static void suite(TestSuite suite) {
		suite.addTest(listSuite("ObjectArrayList", T -> new ObjectArrayList<>(T), getFeatures()));
		suite.addTest(listSuite("ObjectLinkedList", T -> new ObjectLinkedList<>(T), getFeatures()));
		suite.addTest(listSuite("ImmutableObjectList", T -> new ImmutableObjectList<>(T), getImmutableFeatures()));
		suite.addTest(listSuite("Synchronized ObjectArrayList", T -> new ObjectArrayList<>(T).synchronize(), getFeatures()));
		suite.addTest(listSuite("Unmodifiable ObjectArrayList", T -> new ObjectArrayList<>(T).unmodifiable(), getImmutableFeatures()));
	}

	private static Test listSuite(String name, Function<String[], ObjectList<String>> factory, Collection<Feature<?>> features) {
		return ObjectListTestSuiteBuilder.using(new SimpleObjectTestGenerator.Lists<>(factory, createStrings())).named(name)
			.withFeatures(CollectionSize.ANY).withFeatures(features).createTestSuite();
	}
	
	private static ObjectSamples<String> createStrings() {
		return new ObjectSamples<>("b", "a", "c", "d", "e");
	}
	private static Collection<Feature<?>> getImmutableFeatures() {
		return Arrays.asList(SpecialFeature.COPYING, CollectionFeature.ALLOWS_NULL_VALUES);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING);
	}
}