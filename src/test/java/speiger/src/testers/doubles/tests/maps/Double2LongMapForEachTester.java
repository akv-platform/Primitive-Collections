package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.KNOWN_ORDER;

import java.util.List;

import org.junit.Ignore;

import com.google.common.collect.testing.Helpers;
import com.google.common.collect.testing.features.CollectionFeature;

import speiger.src.collections.doubles.maps.interfaces.Double2LongMap;
import speiger.src.collections.objects.lists.ObjectArrayList;
import speiger.src.collections.objects.lists.ObjectList;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2LongMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2LongMapForEachTester extends AbstractDouble2LongMapTester
{
	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachKnownOrder() {
		ObjectList<Double2LongMap.Entry> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		assertEquals(getOrderedElements(), entries);
	}

	@CollectionFeature.Require(KNOWN_ORDER)
	public void testForEachUnknownOrder() {
		List<Double2LongMap.Entry> entries = new ObjectArrayList<>();
		getMap().forEach((k, v) -> entries.add(entry(k, v)));
		Helpers.assertEqualIgnoringOrder(getSampleEntries(), entries);
	}
}