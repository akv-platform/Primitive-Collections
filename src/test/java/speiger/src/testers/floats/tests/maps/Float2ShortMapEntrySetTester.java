package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_ITERATOR_REMOVE;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.Iterator;
import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.floats.maps.interfaces.Float2ShortMap;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Float2ShortMapEntrySetTester extends AbstractFloat2ShortMapTester
{
	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(SUPPORTS_ITERATOR_REMOVE)
	public void testEntrySetIteratorRemove() {
		Set<Float2ShortMap.Entry> entrySet = getMap().float2ShortEntrySet();
		Iterator<Float2ShortMap.Entry> entryItr = entrySet.iterator();
		assertEquals(e0(), entryItr.next());
		entryItr.remove();
		assertTrue(getMap().isEmpty());
		assertFalse(entrySet.contains(e0()));
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testSetValue() {
		for (Float2ShortMap.Entry entry : getMap().float2ShortEntrySet()) {
			if (Float.floatToIntBits(entry.getFloatKey()) == Float.floatToIntBits(k0())) {
				assertEquals("entry.setValue() should return the old value", v0(), entry.setValue(v3()));
				break;
			}
		}
		expectReplacement(entry(k0(), v3()));
	}

}