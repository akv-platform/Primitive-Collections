package speiger.src.testers.shorts.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.sets.ShortSet;
import speiger.src.collections.shorts.utils.ShortSets;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortIterableFilterTester extends AbstractShortCollectionTester
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> T != e0()).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(ShortSets.singleton(e0()), collection.filter(T -> T == e0()).pourAsSet().toShortArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> T == e1()).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(ShortSets.singleton(e1()), collection.filter(T -> T != e1()).pourAsSet().toShortArray()));
	}
	
	protected boolean expectMissing(ShortSet set, short...elements)
	{
		for(short element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}