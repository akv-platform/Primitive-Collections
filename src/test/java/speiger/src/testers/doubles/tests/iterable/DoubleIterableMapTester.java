package speiger.src.testers.doubles.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;
import speiger.src.testers.doubles.utils.DoubleHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterableMapTester extends AbstractDoubleCollectionTester
{
	public void testIterableMap_ToString() {
		assertEquals(DoubleHelpers.copyToList(collection).toString(), collection.map(Double::toString).pourAsList().toString());
	}
	
	public void testIterableMap_Collection() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(double entry : getOrderedElements())
		{
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange((int)T))).pourAsList());
	}
	
	public void testIterableMap_Array() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(double entry : getOrderedElements())
		{
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.arrayflatMap(T -> toRange((int)T)).pourAsList());
	}
	
	private Integer[] toRange(int range)
	{
		Integer[] result = new Integer[range];
		for(int i = 0;i<range;i++)
		{
			result[i] = Integer.valueOf(i);
		}
		return result;
	}
}