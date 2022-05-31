package speiger.src.testers.ints.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.ints.lists.IntArrayList;
import speiger.src.collections.ints.lists.IntList;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class IntIterablePeekTester extends AbstractIntCollectionTester
{
	public void testIterablePeek() {
		IntList peeked = new IntArrayList();
		IntList result = new IntArrayList();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}