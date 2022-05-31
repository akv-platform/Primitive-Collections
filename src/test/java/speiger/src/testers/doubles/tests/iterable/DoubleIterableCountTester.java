package speiger.src.testers.doubles.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterableCountTester extends AbstractDoubleCollectionTester
{
	public void testIterableCount_null() {
		try {
			collection.count(null);
			fail("This should throw a NullPointerException");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableCount_NoneFound() {
		assertEquals("Expected none to be found", 0, collection.count(T -> false));
	}
	
	public void testIterableCount_AllFound() {
		assertEquals("Expected All to be found", getNumElements(), collection.count(T -> true));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableCount_FindFirst() 
	{
		assertEquals("First element should be found", 1, collection.count(T -> Double.doubleToLongBits(T) == Double.doubleToLongBits(e0())));		
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableCount_FindLast() {
		assertEquals("Last element should be found", 1, collection.count(T -> Double.doubleToLongBits(T) == Double.doubleToLongBits(e2())));
	}
}