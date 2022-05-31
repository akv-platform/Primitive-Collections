package speiger.src.testers.doubles.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterableSortedTester extends AbstractDoubleCollectionTester
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	public void testIterableSorted() {
		DoubleList expected = new DoubleArrayList(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}
}