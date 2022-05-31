package speiger.src.testers.objects.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterableSortedTester<T> extends AbstractObjectCollectionTester<T>
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	public void testIterableSorted() {
		ObjectList<T> expected = new ObjectArrayList<>(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}
}