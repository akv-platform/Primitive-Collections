package speiger.src.testers.longs.tests.iterable;

import org.junit.Ignore;


import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;
import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class LongIterableSortedTester extends AbstractLongCollectionTester
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	@SpecialFeature.Require(absent = SpecialFeature.MAP_ENTRY)
	public void testIterableSorted() {
		LongList expected = new LongArrayList(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}

}