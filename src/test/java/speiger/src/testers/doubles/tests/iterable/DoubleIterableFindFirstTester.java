package speiger.src.testers.doubles.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterableFindFirstTester extends AbstractDoubleCollectionTester
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
		assertEquals("First Element should be found", e0(), container.findFirst(T -> Double.doubleToLongBits(T) == Double.doubleToLongBits(e0())));
	}
	
	public void testIterableFindFirst_FindNothing() {
		assertEquals("No element should be found", 0, container.findFirst(T -> Double.doubleToLongBits(T) == Double.doubleToLongBits(e4())));
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFindFirst_FindLastElement() {
		assertEquals("Last Element should be found", e2(), container.findFirst(T -> Double.doubleToLongBits(T) == Double.doubleToLongBits(e2())));
	}
}