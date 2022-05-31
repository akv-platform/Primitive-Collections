package speiger.src.testers.chars.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.chars.lists.CharArrayList;
import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharIterablePeekTester extends AbstractCharCollectionTester
{
	public void testIterablePeek() {
		CharList peeked = new CharArrayList();
		CharList result = new CharArrayList();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}