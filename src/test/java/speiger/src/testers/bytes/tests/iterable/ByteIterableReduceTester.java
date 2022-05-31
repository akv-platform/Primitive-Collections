package speiger.src.testers.bytes.tests.iterable;

import org.junit.Ignore;

import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteIterableReduceTester extends AbstractByteCollectionTester
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
			collection.reduce((byte)0, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce((byte)0, this::sum));
	}
	
	public byte getSum()
	{
		byte result = (byte)0;
		for(byte key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public byte sum(byte key, byte value)
	{
		return (byte)(key + value);
	}
	
}