package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;


import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.floats.collections.FloatIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.maps.interfaces.Object2FloatMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2FloatMapAddToTester<T> extends AbstractObject2FloatMapTester<T>
{	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testAddTo_supportedPresent() {
		assertEquals("addTo(present, value) should return the old value", v0(), getMap().addTo(k0(), v3()));
		expectReplacement(entry(k0(), v3()));
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testAddTo_supportedNotPresent() {
		assertEquals("addTo(notPresent, value) should return -1F", -1F, addTo(e3()));
		expectAdded(e3());
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testAddToSum_supportedNotPresent() {
		assertEquals("addTo(notPresent, value) should return -1F", -1F, addTo(e3()));
		assertEquals("addTo(notPresent, value) should return "+v3(), v3(), addTo(e3()));
		expectAdded(entry(k3(), (float)(v3()+v3())));
	}


	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testAddToAbsentConcurrentWithEntrySetIteration() {
		try {
			Iterator<Object2FloatMap.Entry<T>> iterator = getMap().object2FloatEntrySet().iterator();
			addTo(e3());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testAddToAbsentConcurrentWithKeySetIteration() {
		try {
			ObjectIterator<T> iterator = getMap().keySet().iterator();
			addTo(e3());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testAddToAbsentConcurrentWithValueIteration() {
		try {
			FloatIterator iterator = getMap().values().iterator();
			addTo(e3());
			iterator.nextFloat();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testAddTo_unsupportedNotPresent() {
		try {
			addTo(e3());
			fail("addTo(notPresent, value) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testAddTo_unsupportedPresentExistingValue() {
		try {
			assertEquals("addTo(present, existingValue) should return present or throw", v0(), addTo(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testAddTo_unsupportedPresentDifferentValue() {
		try {
			getMap().addTo(k0(), v3());
			fail("addTo(present, differentValue) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	private float addTo(Object2FloatMap.Entry<T> entry) {
		return getMap().addTo(entry.getKey(), entry.getFloatValue());
	}
}