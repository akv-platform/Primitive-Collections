package speiger.src.testers.shorts.tests.iterable;

import org.junit.Ignore;

import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortIterableReduceTester extends AbstractShortCollectionTester
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
			collection.reduce((short)0, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce((short)0, this::sum));
	}
	
	public short getSum()
	{
		short result = (short)0;
		for(short key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public short sum(short key, short value)
	{
		return (short)(key + value);
	}
	
}