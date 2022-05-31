package speiger.src.testers.bytes.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteIterableLimitTester extends AbstractByteCollectionTester
{
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableLimit() {
		ByteList list = new ByteArrayList(collection);
		list.removeByte(list.size()-1);
		ByteList result = collection.limit(getNumElements()-1).pourAsList();
		assertEquals(list.size(), result.size());
		assertEquals("Limit does not retain the iteration order", list, result);
	}
}