package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.doubles.collections.DoubleIterator;
import speiger.src.collections.doubles.maps.interfaces.Double2ObjectMap;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ObjectMapTester;

@Ignore
public class Double2ObjectMapPutTester<V> extends AbstractDouble2ObjectMapTester<V>
{	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPut_supportedPresent() {
		assertEquals("put(present, value) should return the old value", v0(), getMap().put(k0(), v3()));
		expectReplacement(entry(k0(), v3()));
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPut_supportedNotPresent() {
		assertEquals("put(notPresent, value) should return null", null, put(e3()));
		expectAdded(e3());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithEntrySetIteration() {
		try {
			Iterator<Double2ObjectMap.Entry<V>> iterator = getMap().double2ObjectEntrySet().iterator();
			put(e3());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithKeySetIteration() {
		try {
			DoubleIterator iterator = getMap().keySet().iterator();
			put(e3());
			iterator.nextDouble();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithValueIteration() {
		try {
			ObjectIterator<V> iterator = getMap().values().iterator();
			put(e3());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPut_unsupportedNotPresent() {
		try {
			put(e3());
			fail("put(notPresent, value) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPut_unsupportedPresentExistingValue() {
		try {
			assertEquals("put(present, existingValue) should return present or throw", v0(), put(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPut_unsupportedPresentDifferentValue() {
		try {
			getMap().put(k0(), v3());
			fail("put(present, differentValue) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	private V put(Double2ObjectMap.Entry<V> entry) {
		return getMap().put(entry.getDoubleKey(), entry.getValue());
	}
}