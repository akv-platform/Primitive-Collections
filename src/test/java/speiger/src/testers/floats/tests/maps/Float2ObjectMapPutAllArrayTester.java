package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.floats.maps.impl.hash.Float2ObjectLinkedOpenHashMap;
import speiger.src.collections.floats.maps.interfaces.Float2ObjectMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ObjectMapTester;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
public class Float2ObjectMapPutAllArrayTester<V> extends AbstractFloat2ObjectMapTester<V>
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllArray_supportedNothing() {
		getMap().putAll(emptyKeyArray(), emptyValueArray());
		expectUnchanged();
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllObjectArray_supportedNothing() {
		getMap().putAll(emptyKeyObjectArray(), emptyValueObjectArray());
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllArray_unsupportedNothing() {
		try {
			getMap().putAll(emptyKeyArray(), emptyValueArray());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllObjectArray_unsupportedNothing() {
		try {
			getMap().putAll(emptyKeyObjectArray(), emptyValueObjectArray());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllArray_supportedNonePresent() {
		putAll(createDisjointCollection());
		expectAdded(e3(), e4());
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllObjectArray_supportedNonePresent() {
		putAllObjects(createDisjointCollection());
		expectAdded(e3(), e4());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllArray_unsupportedNonePresent() {
		try {
			putAll(createDisjointCollection());
			fail("putAll(nonePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAllObjectArray_unsupportedNonePresent() {
		try {
			putAllObjects(createDisjointCollection());
			fail("putAll(nonePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArray_supportedSomePresent() {
		putAll(MinimalObjectCollection.of(e3(), e0()));
		expectAdded(e3());
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArray_supportedSomePresent() {
		putAllObjects(MinimalObjectCollection.of(e3(), e0()));
		expectAdded(e3());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArraySomePresentConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Float2ObjectMap.Entry<V>> iterator = getMap().float2ObjectEntrySet().iterator();
			putAll(MinimalObjectCollection.of(e3(), e0()));
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}
	
	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArraySomePresentConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Float2ObjectMap.Entry<V>> iterator = getMap().float2ObjectEntrySet().iterator();
			putAllObjects(MinimalObjectCollection.of(e3(), e0()));
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArray_unsupportedSomePresent() {
		try {
			putAll(MinimalObjectCollection.of(e3(), e0()));
			fail("putAll(somePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArray_unsupportedSomePresent() {
		try {
			putAllObjects(MinimalObjectCollection.of(e3(), e0()));
			fail("putAll(somePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllArray_unsupportedAllPresent() {
		try {
			putAll(MinimalObjectCollection.of(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
	
	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllObjectArray_unsupportedAllPresent() {
		try {
			putAllObjects(MinimalObjectCollection.of(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllArray_nullCollectionReference() {
		try {
			getMap().putAll((float[])null, (V[])null);
			fail("putAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllObjectArray_nullCollectionReference() {
		try {
			getMap().putAll((Float[])null, (V[])null);
			fail("putAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}
	
	private void putAll(ObjectIterable<Float2ObjectMap.Entry<V>> entries) {
		Float2ObjectMap<V> map = new Float2ObjectLinkedOpenHashMap<>();
		for (Float2ObjectMap.Entry<V> entry : entries) {
			map.put(entry.getFloatKey(), entry.getValue());
		}
		getMap().putAll(map.keySet().toFloatArray(new float[map.size()]), map.values().toArray((V[])new Object[map.size()]));
	}
	
	private void putAllObjects(ObjectIterable<Float2ObjectMap.Entry<V>> entries) {
		Map<Float, V> map = new Float2ObjectLinkedOpenHashMap<>();
		for (Float2ObjectMap.Entry<V> entry : entries) {
			map.put(entry.getFloatKey(), entry.getValue());
		}
		getMap().putAll(map.keySet().toArray(new Float[map.size()]), map.values().toArray((V[])new Object[map.size()]));
	}
}