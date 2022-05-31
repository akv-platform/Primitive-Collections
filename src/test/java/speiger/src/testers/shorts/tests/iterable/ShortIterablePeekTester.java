package speiger.src.testers.shorts.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.shorts.lists.ShortArrayList;
import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortIterablePeekTester extends AbstractShortCollectionTester
{
	public void testIterablePeek() {
		ShortList peeked = new ShortArrayList();
		ShortList result = new ShortArrayList();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}