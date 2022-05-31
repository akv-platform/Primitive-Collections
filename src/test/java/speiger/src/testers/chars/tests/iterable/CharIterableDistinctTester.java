package speiger.src.testers.chars.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.chars.lists.CharList;
import speiger.src.testers.chars.tests.base.AbstractCharCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class CharIterableDistinctTester extends AbstractCharCollectionTester
{
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testDistinct()
	{
		ArrayWithDuplicate duplicate = createArrayWithDuplicateElement();
		resetContainer(primitiveGenerator.create(duplicate.elements));
		CharList list = collection.distinct().pourAsList();
		assertEquals("Distinct should remove duplicate elements", list.indexOf(duplicate.duplicate), list.lastIndexOf(duplicate.duplicate));
	}
}