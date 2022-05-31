package speiger.src.testers.objects.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterableFindFirstTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testIterableFindFirst_null() {
		try {
			container.findFirst(null);
			fail("This should throw a NullPointerException");
		}
		catch (NullPointerException e) {
		}
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFindFirst_FindFirstElements() {
		assertEquals("First Element should be found", e0(), container.findFirst(T -> Objects.equals(T, e0())));
	}
	
	public void testIterableFindFirst_FindNothing() {
		assertEquals("No element should be found", 0, container.findFirst(T -> Objects.equals(T, e4())));
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFindFirst_FindLastElement() {
		assertEquals("Last Element should be found", e2(), container.findFirst(T -> Objects.equals(T, e2())));
	}
}