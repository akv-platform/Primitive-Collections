package speiger.src.testers.floats.tests.iterable;

import org.junit.Ignore;

import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatIterableReduceTester extends AbstractFloatCollectionTester
{
	public void testIterableReduce_Null() {
		try {
			collection.reduce(null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce_extraNull() {
		try {
			collection.reduce(0F, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(0F, this::sum));
	}
	
	public float getSum()
	{
		float result = 0F;
		for(float key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public float sum(float key, float value)
	{
		return key + value;
	}
	
}