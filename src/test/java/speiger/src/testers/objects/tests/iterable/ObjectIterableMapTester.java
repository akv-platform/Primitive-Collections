package speiger.src.testers.objects.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterableMapTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testIterableMap_ToString() {
		assertEquals(ObjectHelpers.copyToList(collection).toString(), collection.map(T::toString).pourAsList().toString());
	}
	
	public void testIterableMap_Collection() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(T entry : getOrderedElements())
		{
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange((int)T))).pourAsList());
	}
	
	public void testIterableMap_Array() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(T entry : getOrderedElements())
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