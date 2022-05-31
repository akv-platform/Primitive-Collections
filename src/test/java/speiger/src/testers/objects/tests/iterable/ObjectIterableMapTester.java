package speiger.src.testers.objects.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import speiger.src.collections.chars.utils.CharArrays;
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
		ObjectList<Character> result = new ObjectArrayList<>();
		for(T entry : getOrderedElements())
		{
			result.addAll(toRange(entry));
		}
		ObjectHelpers.assertEqualIgnoringOrder(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange(T))).pourAsList());
	}

	public void testIterableMap_Array() {
		ObjectList<Character> result = new ObjectArrayList<>();
		for(T entry : getOrderedElements())
		{
			result.addAll(toRange(entry));
		}
		ObjectHelpers.assertEqualIgnoringOrder(result, collection.arrayflatMap(this::toRange).pourAsList());
	}

	private Character[] toRange(T obj)
	{
		return CharArrays.wrap(Objects.toString(obj).toCharArray());
	}
}