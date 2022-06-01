package speiger.src.testers.ints.tests.iterable;


import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;
import speiger.src.testers.ints.utils.IntHelpers;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
@SuppressWarnings("javadoc")
public class IntIterableMapTester extends AbstractIntCollectionTester
{
	public void testIterableMap_ToString() {
		assertEquals(IntHelpers.copyToList(collection).toString(), collection.map(Integer::toString).pourAsList().toString());
	}

	@CollectionFeature.Require(CollectionFeature.KNOWN_ORDER)
	public void testIterableMap_Collection() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(int entry : getOrderedElements()) {
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange((int)T))).pourAsList());
	}
	
	@CollectionFeature.Require(CollectionFeature.KNOWN_ORDER)
	public void testIterableMap_Array() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(int entry : getOrderedElements()) {
			result.addAll(toRange((int)entry));
		}
		assertEquals(result, collection.arrayflatMap(T -> toRange((int)T)).pourAsList());
	}
	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	public void testIterableMap_CollectionUnordered() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(int entry : getOrderedElements()) {
			result.addAll(toRange((int)entry));
		}
		ObjectHelpers.assertEqualIgnoringOrder(result, collection.flatMap(T -> ObjectArrayList.wrap(toRange((int)T))).pourAsList());
	}
	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	public void testIterableMap_ArrayUnordered() {
		ObjectList<Integer> result = new ObjectArrayList<>();
		for(int entry : getOrderedElements()) {
			result.addAll(toRange((int)entry));
		}
		ObjectHelpers.assertEqualIgnoringOrder(result, collection.arrayflatMap(T -> toRange((int)T)).pourAsList());
	}
	
	private Integer[] toRange(int range) {
		Integer[] result = new Integer[range];
		for(int i = 0;i<range;i++) {
			result[i] = Integer.valueOf(i);
		}
		return result;
	}
}