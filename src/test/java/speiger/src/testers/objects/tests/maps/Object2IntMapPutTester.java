package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.ints.collections.IntIterator;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.collections.objects.maps.interfaces.Object2IntMap;
import speiger.src.testers.objects.tests.base.maps.AbstractObject2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2IntMapPutTester<T> extends AbstractObject2IntMapTester<T>
{	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPut_supportedPresent() {
		assertEquals("put(present, value) should return the old value", v0(), getMap().put(k0(), v3()));
		expectReplacement(entry(k0(), v3()));
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPut_supportedNotPresent() {
		assertEquals("put(notPresent, value) should return -1", -1, put(e3()));
		expectAdded(e3());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithEntrySetIteration() {
		try {
			Iterator<Object2IntMap.Entry<T>> iterator = getMap().object2IntEntrySet().iterator();
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
			ObjectIterator<T> iterator = getMap().keySet().iterator();
			put(e3());
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAbsentConcurrentWithValueIteration() {
		try {
			IntIterator iterator = getMap().values().iterator();
			put(e3());
			iterator.nextInt();
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

	private int put(Object2IntMap.Entry<T> entry) {
		return getMap().put(entry.getKey(), entry.getIntValue());
	}
}