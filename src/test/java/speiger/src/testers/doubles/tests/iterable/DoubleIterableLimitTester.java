package speiger.src.testers.doubles.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterableLimitTester extends AbstractDoubleCollectionTester
{
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableLimit() {
		DoubleList list = new DoubleArrayList(collection);
		list.removeDouble(list.size()-1);
		DoubleList result = collection.limit(getNumElements()-1).pourAsList();
		assertEquals(list.size(), result.size());
		assertEquals("Limit does not retain the iteration order", list, result);
	}
}