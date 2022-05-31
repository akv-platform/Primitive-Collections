package speiger.src.testers.longs.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.longs.lists.LongArrayList;
import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class LongIterablePeekTester extends AbstractLongCollectionTester
{
	public void testIterablePeek() {
		LongList peeked = new LongArrayList();
		LongList result = new LongArrayList();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}