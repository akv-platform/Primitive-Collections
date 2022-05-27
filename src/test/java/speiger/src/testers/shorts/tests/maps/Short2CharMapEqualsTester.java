package speiger.src.testers.shorts.tests.maps;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.shorts.maps.impl.hash.Short2CharOpenHashMap;
import speiger.src.collections.shorts.maps.interfaces.Short2CharMap;
import speiger.src.collections.objects.collections.ObjectCollection;
import speiger.src.testers.shorts.tests.base.maps.AbstractShort2CharMapTester;
import speiger.src.testers.objects.utils.ObjectHelpers;

@Ignore
public class Short2CharMapEqualsTester extends AbstractShort2CharMapTester 
{
	public void testEquals_otherMapWithSameEntries() {
		assertTrue("A Map should equal any other Map containing the same entries.", getMap().equals(newHashMap(getSampleEntries())));
	}

	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_otherMapWithDifferentEntries() {
		Short2CharMap other = newHashMap(getSampleEntries(getNumEntries() - 1));
		other.put(k3(), v3());
		assertFalse("A Map should not equal another Map containing different entries.", getMap().equals(other));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testEquals_smallerMap() {
		ObjectCollection<Short2CharMap.Entry> fewerEntries = getSampleEntries(getNumEntries() - 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(fewerEntries)));
	}

	public void testEquals_largerMap() {
		ObjectCollection<Short2CharMap.Entry> moreEntries = getSampleEntries(getNumEntries() + 1);
		assertFalse("Maps of different sizes should not be equal.", getMap().equals(newHashMap(moreEntries)));
	}

	public void testEquals_list() {
		assertFalse("A List should never equal a Map.", getMap().equals(ObjectHelpers.copyToList(getMap().short2CharEntrySet())));
	}

	private static Short2CharMap newHashMap(ObjectCollection<? extends Short2CharMap.Entry> entries) {
		Short2CharMap map = new Short2CharOpenHashMap();
		for (Short2CharMap.Entry entry : entries) {
			map.put(entry.getShortKey(), entry.getCharValue());
		}
		return map;
	}
}