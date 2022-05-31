package speiger.src.testers.longs.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.longs.lists.LongList;
import speiger.src.testers.longs.tests.base.AbstractLongCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class LongIterableDistinctTester extends AbstractLongCollectionTester
{
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testDistinct()
	{
		ArrayWithDuplicate duplicate = createArrayWithDuplicateElement();
		resetContainer(primitiveGenerator.create(duplicate.elements));
		LongList list = collection.distinct().pourAsList();
		assertEquals("Distinct should remove duplicate elements", list.indexOf(duplicate.duplicate), list.lastIndexOf(duplicate.duplicate));
	}
}