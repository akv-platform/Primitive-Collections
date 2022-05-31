package speiger.src.testers.doubles.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterableReduceTester extends AbstractDoubleCollectionTester
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
			collection.reduce(0D, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(0D, this::sum));
	}
	
	public double getSum()
	{
		double result = 0D;
		for(double key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public double sum(double key, double value)
	{
		return key + value;
	}
	
}