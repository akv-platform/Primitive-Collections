package speiger.src.testers.ints.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class IntIterableReduceTester extends AbstractIntCollectionTester
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
			collection.reduce(0, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(0, this::sum));
	}
	
	public int getSum()
	{
		int result = 0;
		for(int key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public int sum(int key, int value)
	{
		return key + value;
	}
	
}