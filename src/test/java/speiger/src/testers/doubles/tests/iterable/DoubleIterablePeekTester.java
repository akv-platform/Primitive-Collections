package speiger.src.testers.doubles.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.doubles.lists.DoubleArrayList;
import speiger.src.collections.doubles.lists.DoubleList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterablePeekTester extends AbstractDoubleCollectionTester
{
	public void testIterablePeek() {
		DoubleList peeked = new DoubleArrayList();
		DoubleList result = new DoubleArrayList();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}