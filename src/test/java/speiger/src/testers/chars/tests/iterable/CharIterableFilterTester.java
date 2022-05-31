package speiger.src.testers.chars.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.sets.CharSet;
import speiger.src.collections.chars.utils.CharSets;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharIterableFilterTester extends AbstractCharCollectionTester
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> T != e0()).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(CharSets.singleton(e0()), collection.filter(T -> T == e0()).pourAsSet().toCharArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> T == e1()).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(CharSets.singleton(e1()), collection.filter(T -> T != e1()).pourAsSet().toCharArray()));
	}
	
	protected boolean expectMissing(CharSet set, char...elements)
	{
		for(char element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}