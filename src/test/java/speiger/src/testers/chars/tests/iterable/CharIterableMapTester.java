package speiger.src.testers.chars.tests.iterable;

import org.junit.Ignore;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;
import speiger.src.testers.chars.utils.CharHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class CharIterableMapTester extends AbstractCharCollectionTester
{
	public void testIterableMap_ToString() {
		assertEquals(CharHelpers.copyToList(collection).toString(), collection.map(Character::toString).pourAsList().toString());
	}
	
	public void testIterableMap_Collection() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(char entry : getOrderedElements())
		{
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange((int)T))).pourAsList());
	}
	
	public void testIterableMap_Array() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(char entry : getOrderedElements())
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