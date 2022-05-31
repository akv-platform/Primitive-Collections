package speiger.src.testers.chars.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharIterableSortedTester extends AbstractCharCollectionTester
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	public void testIterableSorted() {
		CharList expected = new CharArrayList(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}
}