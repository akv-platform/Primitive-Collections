package speiger.src.testers.floats.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.floats.lists.FloatArrayList;
import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatIterablePeekTester extends AbstractFloatCollectionTester
{
	public void testIterablePeek() {
		FloatList peeked = new FloatArrayList();
		FloatList result = new FloatArrayList();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}