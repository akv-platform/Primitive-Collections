package speiger.src.testers.floats.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatIterableFindFirstTester extends AbstractFloatCollectionTester
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
		assertEquals("First Element should be found", e0(), container.findFirst(T -> Float.floatToIntBits(T) == Float.floatToIntBits(e0())));
	}
	
	public void testIterableFindFirst_FindNothing() {
		assertEquals("No element should be found", 0, container.findFirst(T -> Float.floatToIntBits(T) == Float.floatToIntBits(e4())));
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFindFirst_FindLastElement() {
		assertEquals("Last Element should be found", e2(), container.findFirst(T -> Float.floatToIntBits(T) == Float.floatToIntBits(e2())));
	}
}