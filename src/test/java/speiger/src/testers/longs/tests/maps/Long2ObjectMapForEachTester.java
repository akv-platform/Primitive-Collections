package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import java.util.List;

import org.junit.Ignore;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.longs.maps.interfaces.Long2ObjectMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.longs.tests.base.maps.AbstractLong2ObjectMapTester;

@Ignore
public class Long2ObjectMapForEachTester<V> extends AbstractLong2ObjectMapTester<V>
{
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ObjectList<Long2ObjectMap.Entry<V>> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		assertEquals(getOrderedElements(), entries);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		List<Long2ObjectMap.Entry<V>> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		Helpers.assertEqualIgnoringOrder(getSampleEntries(), entries);
	}
}