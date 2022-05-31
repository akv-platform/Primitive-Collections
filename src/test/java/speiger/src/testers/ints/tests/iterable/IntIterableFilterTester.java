package speiger.src.testers.ints.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.sets.IntSet;
import speiger.src.collections.ints.utils.IntSets;
import speiger.src.testers.ints.tests.base.AbstractIntCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class IntIterableFilterTester extends AbstractIntCollectionTester
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> T != e0()).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(IntSets.singleton(e0()), collection.filter(T -> T == e0()).pourAsSet().toIntArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> T == e1()).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(IntSets.singleton(e1()), collection.filter(T -> T != e1()).pourAsSet().toIntArray()));
	}
	
	protected boolean expectMissing(IntSet set, int...elements)
	{
		for(int element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}