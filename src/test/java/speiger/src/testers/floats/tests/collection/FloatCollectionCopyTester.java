package speiger.src.testers.floats.tests.collection;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.testers.utils.SpecialFeature;
import speiger.src.collections.floats.collections.FloatCollection;
import speiger.src.collections.floats.utils.FloatCollections;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatCollectionCopyTester extends AbstractFloatCollectionTester
{
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(SpecialFeature.COPYING)
	public void testEquals() {
		FloatCollection copy = collection.copy();
		if(!(collection instanceof FloatCollections.EmptyCollection)) {
			Assert.assertFalse("Copied Collection shouldn't match", copy == collection);
		}
		Assert.assertTrue("Copied Collection contents should match", copy.equals(collection));
	}

	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(absent = SpecialFeature.COPYING)
	public void testEqualsFail() {
		try {
			assertNull(collection.copy());
			fail("If Copying isn't supported it should throw a UnsupportedOperationException");
		}
		catch(UnsupportedOperationException e) {
			//Success
		}
	}
}