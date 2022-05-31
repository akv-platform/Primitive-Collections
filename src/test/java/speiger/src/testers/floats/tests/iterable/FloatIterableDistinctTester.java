package speiger.src.testers.floats.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.floats.lists.FloatList;
import speiger.src.testers.floats.tests.base.AbstractFloatCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatIterableDistinctTester extends AbstractFloatCollectionTester
{
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testDistinct()
	{
		ArrayWithDuplicate duplicate = createArrayWithDuplicateElement();
		resetContainer(primitiveGenerator.create(duplicate.elements));
		FloatList list = collection.distinct().pourAsList();
		assertEquals("Distinct should remove duplicate elements", list.indexOf(duplicate.duplicate), list.lastIndexOf(duplicate.duplicate));
	}
}