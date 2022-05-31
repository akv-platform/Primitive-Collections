package speiger.src.testers.longs.tests.iterable;


import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;
import speiger.src.testers.longs.utils.LongHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class LongIterableMapTester extends AbstractLongCollectionTester
{
	public void testIterableMap_ToString() {
		assertEquals(LongHelpers.copyToList(collection).toString(), collection.map(Long::toString).pourAsList().toString());
	}
	
	public void testIterableMap_Collection() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(long entry : getOrderedElements())
		{
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange((int)T))).pourAsList());
	}
	
	public void testIterableMap_Array() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(long entry : getOrderedElements())
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