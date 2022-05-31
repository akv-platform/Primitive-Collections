package speiger.src.testers.bytes.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteIterableDistinctTester extends AbstractByteCollectionTester
{
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testDistinct()
	{
		ArrayWithDuplicate duplicate = createArrayWithDuplicateElement();
		resetContainer(primitiveGenerator.create(duplicate.elements));
		ByteList list = collection.distinct().pourAsList();
		assertEquals("Distinct should remove duplicate elements", list.indexOf(duplicate.duplicate), list.lastIndexOf(duplicate.duplicate));
	}
}