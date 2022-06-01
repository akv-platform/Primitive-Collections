package speiger.src.testers.chars.tests.maps;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.chars.maps.interfaces.Char2FloatMap;
import speiger.src.testers.chars.tests.base.maps.AbstractChar2FloatMapTester;
import speiger.src.testers.utils.SpecialFeature;

@Ignore
@SuppressWarnings("javadoc")
public class Char2FloatMapCopyTester extends AbstractChar2FloatMapTester
{
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(SpecialFeature.COPYING)
	public void testEquals() {
		Char2FloatMap copy = container.copy();
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