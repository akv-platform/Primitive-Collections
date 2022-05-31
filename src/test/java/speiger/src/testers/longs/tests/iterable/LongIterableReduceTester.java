package speiger.src.testers.longs.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class LongIterableReduceTester extends AbstractLongCollectionTester
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
			collection.reduce(0L, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(0L, this::sum));
	}
	
	public long getSum()
	{
		long result = 0L;
		for(long key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public long sum(long key, long value)
	{
		return key + value;
	}
	
}