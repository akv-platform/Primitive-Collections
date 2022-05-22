package speiger.src.testers.longs.tests.list;

import static com.google.common.collect.testing.features.CollectionFeature.REJECTS_DUPLICATES_AT_CREATION;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.AbstractLongListIndexOfTester;

public class LongListLastIndexOfTester extends AbstractLongListIndexOfTester {
	@Override
	protected int find(long o) {
		return getList().lastIndexOf(o);
	}

	@Override
	protected String getMethodName() {
		return "lastIndexOf";
	}

	@CollectionFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testLastIndexOf_duplicate() {
		long[] array = createSamplesArray();
		array[getNumElements() / 2] = e0();
		collection = primitiveGenerator.create(array);
		assertEquals("lastIndexOf(duplicate) should return index of last occurrence", getNumElements() / 2, getList().lastIndexOf(e0()));
	}
}