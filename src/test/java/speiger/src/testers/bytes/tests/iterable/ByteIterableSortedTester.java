package speiger.src.testers.bytes.tests.iterable;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.bytes.lists.ByteArrayList;
import speiger.src.collections.bytes.lists.ByteList;
import speiger.src.testers.bytes.tests.base.AbstractByteCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ByteIterableSortedTester extends AbstractByteCollectionTester
{	
	@CollectionFeature.Require(absent = CollectionFeature.KNOWN_ORDER)
	public void testIterableSorted() {
		ByteList expected = new ByteArrayList(collection);
		expected.sort(null);
		assertEquals("Elements were expected to be sorted", expected, collection.sorted(null).pourAsList());
	}
}