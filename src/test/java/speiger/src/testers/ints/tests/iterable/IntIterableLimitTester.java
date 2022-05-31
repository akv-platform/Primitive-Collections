package speiger.src.testers.ints.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class IntIterableLimitTester extends AbstractIntCollectionTester
{
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableLimit() {
		IntList list = new IntArrayList(collection);
		list.removeInt(list.size()-1);
		IntList result = collection.limit(getNumElements()-1).pourAsList();
		assertEquals(list.size(), result.size());
		assertEquals("Limit does not retain the iteration order", list, result);
	}
}