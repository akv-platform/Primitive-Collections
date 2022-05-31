package speiger.src.testers.objects.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterablePeekTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testIterablePeek() {
		ObjectList<T> peeked = new ObjectArrayList<>();
		ObjectList<T> result = new ObjectArrayList<>();
		collection.peek(peeked::add).forEach(result::add);
		assertEquals("Collections should match since peek is just a preview of foreach", result, peeked);
	}
}