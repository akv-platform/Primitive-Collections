package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import java.util.List;

import org.junit.Ignore;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.bytes.maps.interfaces.Byte2FloatMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.bytes.tests.base.maps.AbstractByte2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Byte2FloatMapForEachTester extends AbstractByte2FloatMapTester
{
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ObjectList<Byte2FloatMap.Entry> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		assertEquals(getOrderedElements(), entries);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		List<Byte2FloatMap.Entry> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		Helpers.assertEqualIgnoringOrder(getSampleEntries(), entries);
	}
}