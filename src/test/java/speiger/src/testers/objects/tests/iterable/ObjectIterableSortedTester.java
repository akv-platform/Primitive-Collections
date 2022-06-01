package speiger.src.testers.objects.tests.iterable;

import org.junit.Ignore;

import java.util.Comparator;
import java.util.Map;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;
import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterableSortedTester<T> extends AbstractObjectCollectionTester<T>
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	@SpecialFeature.Require(absent = SpecialFeature.MAP_ENTRY)
	public void testIterableSorted() {
		ObjectList<T> expected = new ObjectArrayList<>(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	@SpecialFeature.Require(SpecialFeature.MAP_ENTRY)
	public void testIterableSortedEntry() {
		ObjectList<T> expected = new ObjectArrayList<>(collection);
		Comparator<T> comparator = Comparator.comparing(T -> (Comparable<Object>)((Map.Entry<Object, Object>)T).getKey());
		expected.sort(comparator);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(comparator).pourAsList());
	}
}