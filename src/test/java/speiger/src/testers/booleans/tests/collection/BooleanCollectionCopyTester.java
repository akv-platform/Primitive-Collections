package speiger.src.testers.booleans.tests.collection;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.testers.utils.SpecialFeature;
import speiger.src.collections.booleans.collections.BooleanCollection;
import speiger.src.collections.booleans.utils.BooleanCollections;
import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class BooleanCollectionCopyTester extends AbstractBooleanCollectionTester
{
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(SpecialFeature.COPYING)
	public void testEquals() {
		BooleanCollection copy = collection.copy();
		if(!(collection instanceof BooleanCollections.EmptyCollection)) {
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