package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;
import java.util.Map;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.ints.maps.impl.hash.Int2BooleanLinkedOpenHashMap;
import speiger.src.collections.ints.maps.interfaces.Int2BooleanMap;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2BooleanMapTester;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
public class Int2BooleanMapPutAllArrayTester extends AbstractInt2BooleanMapTester
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
			ObjectIterator<Int2BooleanMap.Entry> iterator = getMap().int2BooleanEntrySet().iterator();
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
			ObjectIterator<Int2BooleanMap.Entry> iterator = getMap().int2BooleanEntrySet().iterator();
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
			getMap().putAll((int[])null, (boolean[])null);
			fail("putAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}
	
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAllObjectArray_nullCollectionReference() {
		try {
			getMap().putAll((Integer[])null, (Boolean[])null);
			fail("putAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}
	
	private void putAll(ObjectIterable<Int2BooleanMap.Entry> entries) {
		Int2BooleanMap map = new Int2BooleanLinkedOpenHashMap();
		for (Int2BooleanMap.Entry entry : entries) {
			map.put(entry.getIntKey(), entry.getBooleanValue());
		}
		getMap().putAll(map.keySet().toIntArray(new int[map.size()]), map.values().toBooleanArray(new boolean[map.size()]));
	}
	
	private void putAllObjects(ObjectIterable<Int2BooleanMap.Entry> entries) {
		Map<Integer, Boolean> map = new Int2BooleanLinkedOpenHashMap();
		for (Int2BooleanMap.Entry entry : entries) {
			map.put(entry.getIntKey(), entry.getBooleanValue());
		}
		getMap().putAll(map.keySet().toArray(new Integer[map.size()]), map.values().toArray(new Boolean[map.size()]));
	}
}