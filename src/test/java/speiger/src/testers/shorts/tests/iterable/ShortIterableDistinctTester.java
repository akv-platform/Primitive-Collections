package speiger.src.testers.shorts.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.lists.ShortList;
import speiger.src.testers.shorts.tests.base.AbstractShortCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ShortIterableDistinctTester extends AbstractShortCollectionTester
{
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testDistinct()
	{
		ArrayWithDuplicate duplicate = createArrayWithDuplicateElement();
		resetContainer(primitiveGenerator.create(duplicate.elements));
		ShortList list = collection.distinct().pourAsList();
		assertEquals("Distinct should remove duplicate elements", list.indexOf(duplicate.duplicate), list.lastIndexOf(duplicate.duplicate));
	}
}