package speiger.src.testers.bytes.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.sets.ByteSet;
import speiger.src.collections.bytes.utils.ByteSets;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteIterableFilterTester extends AbstractByteCollectionTester
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> T != e0()).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(ByteSets.singleton(e0()), collection.filter(T -> T == e0()).pourAsSet().toByteArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> T == e1()).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(ByteSets.singleton(e1()), collection.filter(T -> T != e1()).pourAsSet().toByteArray()));
	}
	
	protected boolean expectMissing(ByteSet set, byte...elements)
	{
		for(byte element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}