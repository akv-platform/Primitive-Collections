package speiger.src.testers.bytes.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteIterablePeekTester extends AbstractByteCollectionTester
{
	public void testIterablePeek() {
		ByteList peeked = new ByteArrayList();
		ByteList result = new ByteArrayList();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}