package speiger.src.testers.shorts.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortIterableLimitTester extends AbstractShortCollectionTester
{
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableLimit() {
		ShortList list = new ShortArrayList(collection);
		list.removeShort(list.size()-1);
		ShortList result = collection.limit(getNumElements()-1).pourAsList();
		assertEquals(list.size(), result.size());
		assertEquals("Limit does not retain the iteration order", list, result);
	}
}