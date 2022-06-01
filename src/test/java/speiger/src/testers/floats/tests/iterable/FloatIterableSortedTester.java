package speiger.src.testers.floats.tests.iterable;

import org.junit.Ignore;


import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;
import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class FloatIterableSortedTester extends AbstractFloatCollectionTester
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	@SpecialFeature.Require(absent = SpecialFeature.MAP_ENTRY)
	public void testIterableSorted() {
		FloatList expected = new FloatArrayList(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}

}