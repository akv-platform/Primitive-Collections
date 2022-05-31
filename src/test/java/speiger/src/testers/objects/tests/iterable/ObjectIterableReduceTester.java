package speiger.src.testers.objects.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterableReduceTester<T> extends AbstractObjectCollectionTester<T>
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
			collection.reduce(null, null);
			fail("This should crash");
		} catch (NullPointerException e) {
		}
	}
	
	public void testIterableReduce() {
		assertEquals("The sum of the collection should match", getSum(), collection.reduce(this::sum));
	}
	
	public void testIterableExtraReduce() {
		assertEquals("The sum of the collection should match", getObjectSum(), collection.reduce(new StringBuilder(), this::sum).toString());		
	}
	
	public T getSum()
	{
		T result = null;
		for(T key : collection)
		{
			result = sum(result, key);
		}
		return result;
	}
	
	public T sum(T key, T value)
	{
		return value;
	}
	
	public StringBuilder sum(StringBuilder builder, T value) {
		return builder.append(Objects.toString(value));
	}
	
	public String getObjectSum() {
		StringBuilder builder = new StringBuilder();
		for(T key : collection)
		{
			builder = sum(builder, key);
		}
		return builder.toString();
	}
}