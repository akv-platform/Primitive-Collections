package speiger.src.testers.floats.tests.maps;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ShortMapTester;
import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ShortMapCopyTester extends AbstractFloat2ShortMapTester
{
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(SpecialFeature.COPYING)
	public void testEquals() {
		Float2ShortMap copy = container.copy();
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