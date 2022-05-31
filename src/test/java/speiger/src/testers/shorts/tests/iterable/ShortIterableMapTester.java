package speiger.src.testers.shorts.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;
import speiger.src.testers.shorts.utils.ShortHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class ShortIterableMapTester extends AbstractShortCollectionTester
{
	public void testIterableMap_ToString() {
		assertEquals(ShortHelpers.copyToList(collection).toString(), collection.map(Short::toString).pourAsList().toString());
	}
	
	public void testIterableMap_Collection() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(short entry : getOrderedElements())
		{
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange((int)T))).pourAsList());
	}
	
	public void testIterableMap_Array() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(short entry : getOrderedElements())
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