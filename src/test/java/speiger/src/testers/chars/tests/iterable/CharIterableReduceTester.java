package speiger.src.testers.chars.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharIterableReduceTester extends AbstractCharCollectionTester
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
			collection.reduce((char)0, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce((char)0, this::sum));
	}
	
	public char getSum()
	{
		char result = (char)0;
		for(char key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public char sum(char key, char value)
	{
		return (char)(key + value);
	}
	
}