package speiger.src.testers.objects.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.collections.objects.utils.ObjectSets;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterableFilterTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> !Objects.equals(T, e0())).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(ObjectSets.singleton(e0()), collection.filter(T -> Objects.equals(T, e0())).pourAsSet().toArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> Objects.equals(T, e1())).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(ObjectSets.singleton(e1()), collection.filter(T -> !Objects.equals(T, e1())).pourAsSet().toArray()));
	}
	
	protected boolean expectMissing(ObjectSet<T> set, Object...elements)
	{
		for(Object element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}