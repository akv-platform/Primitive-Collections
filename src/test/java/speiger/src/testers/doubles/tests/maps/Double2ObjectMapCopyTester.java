package speiger.src.testers.doubles.tests.maps;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ObjectMapTester;
import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class Double2ObjectMapCopyTester<V> extends AbstractDouble2ObjectMapTester<V>
{
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(SpecialFeature.COPYING)
	public void testEquals() {
		Double2ObjectMap<V> copy = container.copy();
		Assert.assertFalse("Copied Map shouldn't match", copy == container);
		Assert.assertTrue("Copied Map contents should match", copy.equals(container));
	}

	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(absent = SpecialFeature.COPYING)
	public void testEqualsFail() {
		try {
			assertNull(container.copy());
			fail("If Copying isn't supported it should throw a UnsupportedOperationException");
		}
		catch(UnsupportedOperationException e) {
			//Success
		}
	}
}