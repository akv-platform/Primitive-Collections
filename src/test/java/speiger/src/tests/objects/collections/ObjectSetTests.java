package speiger.src.tests.objects.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.Comparator;
import java.util.Objects;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.SetFeature;
import com.google.common.collect.testing.features.Feature;
import com.google.common.collect.testing.features.CollectionFeature;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import speiger.src.collections.objects.sets.ImmutableObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectAVLTreeSet;
import speiger.src.collections.objects.sets.ObjectArraySet;
import speiger.src.collections.objects.sets.ObjectLinkedOpenCustomHashSet;
import speiger.src.collections.objects.sets.ObjectLinkedOpenHashSet;
import speiger.src.collections.objects.sets.ObjectNavigableSet;
import speiger.src.collections.objects.sets.ObjectOpenCustomHashSet;
import speiger.src.collections.objects.sets.ObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectOrderedSet;
import speiger.src.collections.objects.sets.ObjectRBTreeSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.collections.objects.utils.ObjectStrategy;
import speiger.src.testers.objects.builder.ObjectNavigableSetTestSuiteBuilder;
import speiger.src.testers.objects.builder.ObjectOrderedSetTestSuiteBuilder;
import speiger.src.testers.objects.builder.ObjectSetTestSuiteBuilder;
import speiger.src.testers.objects.generators.TestObjectNavigableSetGenerator;
import speiger.src.testers.objects.generators.TestObjectOrderedSetGenerator;
import speiger.src.testers.objects.generators.TestObjectSetGenerator;
import speiger.src.testers.objects.impl.SimpleObjectTestGenerator;
import speiger.src.testers.utils.SpecialFeature;


@SuppressWarnings("javadoc")
public class ObjectSetTests extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite("ObjectSets");
		suite(suite);
		System.out.println("Generated ["+suite.countTestCases()+"] Tests");
		return suite;
	}
	
	public static void suite(TestSuite suite) {
		suite.addTest(setSuite("ObjectOpenHashSet", ObjectOpenHashSet::new, getFeatures(), -1));
		suite.addTest(orderedSetSuite("ObjectLinkedOpenHashSet", ObjectLinkedOpenHashSet::new, getFeatures(), -1));
		suite.addTest(setSuite("ObjectOpenCustomHashSet", T -> new ObjectOpenCustomHashSet<>(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ObjectLinkedOpenCustomHashSet", T -> new ObjectLinkedOpenCustomHashSet<>(T, HashStrategy.INSTANCE), getFeatures(), -1));
		suite.addTest(orderedSetSuite("ImmutableObjectOpenHashSet", ImmutableObjectOpenHashSet::new, getImmutableFeatures(), -1));
		suite.addTest(setSuite("ObjectArraySet", ObjectArraySet::new, getFeatures(), -1));
		suite.addTest(navigableSetSuite("ObjectRBTreeSet", ObjectRBTreeSet::new, getFeatures(), false, -1));
		suite.addTest(navigableSetSuite("ObjectAVLTreeSet", ObjectAVLTreeSet::new, getFeatures(), false, -1));
		suite.addTest(navigableSetSuite("ObjectRBTreeSet_Null", T -> new ObjectRBTreeSet<>(T, Comparator.nullsFirst(Comparator.naturalOrder())), getFeatures(), true, -1));
		suite.addTest(navigableSetSuite("ObjectAVLTreeSet_Null", T -> new ObjectAVLTreeSet<>(T, Comparator.nullsFirst(Comparator.naturalOrder())), getFeatures(), true, -1));
		suite.addTest(navigableSetSuite("Synchronized ObjectRBTreeSet", T -> new ObjectRBTreeSet<>(T).synchronize(), getFeatures(), false, -1));
		suite.addTest(navigableSetSuite("Unmodifiable ObjectRBTreeSet", T -> new ObjectRBTreeSet<>(T).unmodifiable(), getImmutableFeatures(), false, -1));
		suite.addTest(setSuite("Empty ObjectSet", T -> ObjectSets.empty(), getImmutableFeatures(), 0));
		suite.addTest(setSuite("Singleton ObjectSet", T -> ObjectSets.singleton(T[0]), getImmutableFeatures(), 1));
		suite.addTest(orderedSetSuite("Synchronized ObjectLinkedOpenHashSet", T -> new ObjectLinkedOpenHashSet<>(T).synchronize(), getFeatures(), -1));
		suite.addTest(orderedSetSuite("Unmodifiable ObjectLinkedOpenHashSet", T -> new ObjectLinkedOpenHashSet<>(T).unmodifiable(), getImmutableFeatures(), -1));
	}
		
	public static Test setSuite(String name, Function<String[], ObjectSet<String>> factory, Collection<Feature<?>> features, int size) {
		return ObjectSetTestSuiteBuilder.using((TestObjectSetGenerator<String>)new SimpleObjectTestGenerator.Sets<>(factory).setElements(createStrings())).named(name)
			.withFeatures(getSizes(size)).withFeatures(CollectionFeature.ALLOWS_NULL_VALUES).withFeatures(features).createTestSuite();
	}

	public static Test orderedSetSuite(String name, Function<String[], ObjectOrderedSet<String>> factory, Collection<Feature<?>> features, int size) {
		return ObjectOrderedSetTestSuiteBuilder.using((TestObjectOrderedSetGenerator<String>)new SimpleObjectTestGenerator.OrderedSets<>(factory).setElements(createStrings())).named(name)
			.withFeatures(getSizes(size)).withFeatures(CollectionFeature.ALLOWS_NULL_VALUES).withFeatures(features).createTestSuite();
	}

	public static Test navigableSetSuite(String name, Function<String[], ObjectNavigableSet<String>> factory, Collection<Feature<?>> features, boolean nullValues, int size) {
		ObjectNavigableSetTestSuiteBuilder<String> builder = (ObjectNavigableSetTestSuiteBuilder<String>)ObjectNavigableSetTestSuiteBuilder.using((TestObjectNavigableSetGenerator<String>)new SimpleObjectTestGenerator.NavigableSets<>(factory).setElements(createStrings())).named(name)
			.withFeatures(getSizes(size)).withFeatures(features);
		if(nullValues) builder.withFeatures(CollectionFeature.ALLOWS_NULL_VALUES);
		return builder.createTestSuite();
	}
	
	private static class HashStrategy implements ObjectStrategy<String> {
		static final HashStrategy INSTANCE = new HashStrategy();
		@Override
		public int hashCode(String o) { return Objects.hashCode(o); }
		@Override
		public boolean equals(String key, String value) { return Objects.equals(key, value); }
	}
	
	private static String[] createStrings() {
		return new String[]{"b", "a", "c", "d", "e", "!! a", "!! b", "~~ a", "~~ b"};
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
		return Arrays.asList(SetFeature.GENERAL_PURPOSE, SpecialFeature.COPYING);
	}
}