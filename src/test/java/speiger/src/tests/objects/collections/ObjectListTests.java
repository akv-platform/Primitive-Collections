package speiger.src.tests.objects.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.testers.ListSubListTester;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.lists.ImmutableObjectList;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectLinkedList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.collections.objects.utils.ObjectLists;
import speiger.src.testers.objects.builder.ObjectListTestSuiteBuilder;
import speiger.src.testers.objects.impl.SimpleObjectTestGenerator;
import speiger.src.testers.objects.generators.TestObjectListGenerator;
import speiger.src.testers.utils.SpecialFeature;
import speiger.src.testers.utils.TestUtils;
import speiger.src.testers.objects.tests.list.ObjectListSubListTester;

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
		suite.addTest(listSuite("ObjectArrayList", T -> new ObjectArrayList<>(T), getFeatures(), -1));
		suite.addTest(listSuite("ObjectLinkedList", T -> new ObjectLinkedList<>(T), getFeatures(), -1));
		suite.addTest(listSuite("ImmutableObjectList", T -> new ImmutableObjectList<>(T), getImmutableFeatures(), -1));
		suite.addTest(listSuite("Synchronized ObjectArrayList", T -> new ObjectArrayList<>(T).synchronize(), getFeatures(), -1));
		suite.addTest(listSuite("Unmodifiable ObjectArrayList", T -> new ObjectArrayList<>(T).unmodifiable(), getImmutableFeatures(), -1));
		suite.addTest(emptylistSuite("Empty ObjectList", T -> ObjectLists.empty(), getImmutableFeatures(), 0));
		suite.addTest(listSuite("Singleton ObjectList", T -> ObjectLists.singleton(T[0]), getImmutableFeatures(), 1));
	}
	
	private static Test listSuite(String name, Function<String[], ObjectList<String>> factory, Collection<Feature<?>> features, int size) {
		return ObjectListTestSuiteBuilder.using((TestObjectListGenerator<String>)new SimpleObjectTestGenerator.Lists<String>(factory).setElements(createStrings())).named(name).withFeatures(getSizes(size)).withFeatures(features).createTestSuite();
	}
	
	private static Test emptylistSuite(String name, Function<String[], ObjectList<String>> factory, Collection<Feature<?>> features, int size) {
		return ObjectListTestSuiteBuilder.using((TestObjectListGenerator<String>)new SimpleObjectTestGenerator.Lists<String>(factory).setElements(createStrings())).named(name).withFeatures(getSizes(size)).suppressing(TestUtils.getSurpession(ListSubListTester.class)).suppressing(TestUtils.getSurpession(ObjectListSubListTester.class)).withFeatures(features).createTestSuite();
	}
	
	private static String[] createStrings() {
		return new String[]{"b", "a", "c", "d", "e"};
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
		return Arrays.asList(SpecialFeature.COPYING, CollectionFeature.ALLOWS_NULL_VALUES);
	}

	private static Collection<Feature<?>> getFeatures() {
		return Arrays.asList(ListFeature.GENERAL_PURPOSE, CollectionFeature.ALLOWS_NULL_VALUES, SpecialFeature.COPYING);
	}
}