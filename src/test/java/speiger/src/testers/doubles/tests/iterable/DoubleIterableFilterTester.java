package speiger.src.testers.doubles.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.doubles.sets.DoubleSet;
import speiger.src.collections.doubles.utils.DoubleSets;
import speiger.src.testers.doubles.tests.base.AbstractDoubleCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class DoubleIterableFilterTester extends AbstractDoubleCollectionTester
{
	public void testIterableFilter_missingElement() {
		assertTrue(expectMissing(collection.filter(T -> Double.doubleToLongBits(T) != Double.doubleToLongBits(e0())).pourAsSet(), e0()));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableFilter_filterElement() {
		assertFalse(expectMissing(DoubleSets.singleton(e0()), collection.filter(T -> Double.doubleToLongBits(T) == Double.doubleToLongBits(e0())).pourAsSet().toDoubleArray()));		
	}
	
	@CollectionSize.Require(CollectionSize.ONE)
	public void testIterableFilter_filterMissing() {
		assertTrue(collection.filter(T -> Double.doubleToLongBits(T) == Double.doubleToLongBits(e1())).pourAsList().isEmpty());
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableFilter_filterSeveral() {
		assertTrue(expectMissing(DoubleSets.singleton(e1()), collection.filter(T -> Double.doubleToLongBits(T) != Double.doubleToLongBits(e1())).pourAsSet().toDoubleArray()));
	}
	
	protected boolean expectMissing(DoubleSet set, double...elements)
	{
		for(double element : elements)
		{
			if(set.contains(element)) return false;
		}
		return true;
	}
}