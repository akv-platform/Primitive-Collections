package speiger.src.testers.longs.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.sets.LongSet;
import speiger.src.collections.longs.utils.LongSets;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class LongIterableFilterTester extends AbstractLongCollectionTester
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> T != e0()).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(LongSets.singleton(e0()), collection.filter(T -> T == e0()).pourAsSet().toLongArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> T == e1()).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(LongSets.singleton(e1()), collection.filter(T -> T != e1()).pourAsSet().toLongArray()));
	}
	
	protected boolean expectMissing(LongSet set, long...elements)
	{
		for(long element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}