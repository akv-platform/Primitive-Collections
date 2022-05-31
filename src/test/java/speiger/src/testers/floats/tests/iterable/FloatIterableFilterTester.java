package speiger.src.testers.floats.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.sets.FloatSet;
import speiger.src.collections.floats.utils.FloatSets;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatIterableFilterTester extends AbstractFloatCollectionTester
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> Float.floatToIntBits(T) != Float.floatToIntBits(e0())).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(FloatSets.singleton(e0()), collection.filter(T -> Float.floatToIntBits(T) == Float.floatToIntBits(e0())).pourAsSet().toFloatArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> Float.floatToIntBits(T) == Float.floatToIntBits(e1())).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(FloatSets.singleton(e1()), collection.filter(T -> Float.floatToIntBits(T) != Float.floatToIntBits(e1())).pourAsSet().toFloatArray()));
	}
	
	protected boolean expectMissing(FloatSet set, float...elements)
	{
		for(float element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}