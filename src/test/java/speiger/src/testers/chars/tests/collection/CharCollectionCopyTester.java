package speiger.src.testers.chars.tests.collection;

import org.junit.Assert;
import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.testers.utils.SpecialFeature;
import speiger.src.collections.chars.collections.CharCollection;
import speiger.src.collections.chars.utils.CharCollections;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharCollectionCopyTester extends AbstractCharCollectionTester
{
	@CollectionFeature.Require(absent = {CollectionFeature.SUBSET_VIEW, CollectionFeature.DESCENDING_VIEW})
	@SpecialFeature.Require(SpecialFeature.COPYING)
	public void testEquals() {
		CharCollection copy = collection.copy();
		if(!(collection instanceof CharCollections.EmptyCollection)) {
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