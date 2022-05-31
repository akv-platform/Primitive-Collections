package speiger.src.testers.floats.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatIterableLimitTester extends AbstractFloatCollectionTester
{
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableLimit() {
		FloatList list = new FloatArrayList(collection);
		list.removeFloat(list.size()-1);
		FloatList result = collection.limit(getNumElements()-1).pourAsList();
		assertEquals(list.size(), result.size());
		assertEquals("Limit does not retain the iteration order", list, result);
	}
}