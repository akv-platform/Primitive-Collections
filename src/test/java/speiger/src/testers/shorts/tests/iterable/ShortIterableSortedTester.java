package speiger.src.testers.shorts.tests.iterable;

import org.junit.Ignore;


import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;
import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class ShortIterableSortedTester extends AbstractShortCollectionTester
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	@SpecialFeature.Require(absent = SpecialFeature.MAP_ENTRY)
	public void testIterableSorted() {
		ShortList expected = new ShortArrayList(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}

}