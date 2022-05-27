package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.REJECTS_DUPLICATES_AT_CREATION;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.shorts.maps.interfaces.Short2IntMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2IntMapCreatorTester extends AbstractShort2IntMapTester
{
	@MapFeature.Require(absent = REJECTS_DUPLICATES_AT_CREATION)
	@CollectionSize.Require(absent = { ZERO, ONE })
	public void testCreateWithDuplicates_nonNullDuplicatesNotRejected() {
		expectFirstRemoved(getEntriesMultipleNonNullKeys());
	}
	
	private Short2IntMap.Entry[] getEntriesMultipleNonNullKeys() {
		Short2IntMap.Entry[] entries = createSamplesArray();
		entries[0] = entry(k1(), v0());
		return entries;
	}

	private void expectFirstRemoved(Short2IntMap.Entry[] entries) {
		resetMap(entries);
		expectContents(ObjectArrayList.wrap(entries).subList(1, getNumElements()));
	}
}