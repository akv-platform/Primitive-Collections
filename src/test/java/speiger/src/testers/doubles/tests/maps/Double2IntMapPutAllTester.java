package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import java.util.ConcurrentModificationException;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.collections.doubles.maps.impl.hash.Double2IntLinkedOpenHashMap;
import speiger.src.collections.doubles.maps.interfaces.Double2IntMap;
import speiger.src.collections.doubles.utils.maps.Double2IntMaps;
import speiger.src.collections.objects.collections.ObjectIterable;
import speiger.src.collections.objects.collections.ObjectIterator;
import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2IntMapTester;
import speiger.src.testers.objects.utils.MinimalObjectCollection;

@Ignore
@SuppressWarnings("javadoc")
public class Double2IntMapPutAllTester extends AbstractDouble2IntMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAll_supportedNothing() {
		getMap().putAll(emptyMap());
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAll_unsupportedNothing() {
		try {
			getMap().putAll(emptyMap());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAll_supportedNonePresent() {
		putAll(createDisjointCollection());
		expectAdded(e3(), e4());
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testPutAll_unsupportedNonePresent() {
		try {
			putAll(createDisjointCollection());
			fail("putAll(nonePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
		expectMissing(e3(), e4());
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAll_supportedSomePresent() {
		putAll(MinimalObjectCollection.of(e3(), e0()));
		expectAdded(e3());
	}

	@MapFeature.Require({ FAILS_FAST_ON_CONCURRENT_MODIFICATION, SUPPORTS_PUT })
	@CollectionSize.Require(absent = ZERO)
	public void testPutAllSomePresentConcurrentWithEntrySetIteration() {
		try {
			ObjectIterator<Double2IntMap.Entry> iterator = getMap().double2IntEntrySet().iterator();
			putAll(MinimalObjectCollection.of(e3(), e0()));
			iterator.next();
			fail("Expected ConcurrentModificationException");
		} catch (ConcurrentModificationException expected) {
			// success
		}
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAll_unsupportedSomePresent() {
		try {
			putAll(MinimalObjectCollection.of(e3(), e0()));
			fail("putAll(somePresent) should throw");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testPutAll_unsupportedAllPresent() {
		try {
			putAll(MinimalObjectCollection.of(e0()));
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testPutAll_nullCollectionReference() {
		try {
			getMap().putAll(null);
			fail("putAll(null) should throw NullPointerException");
		} catch (NullPointerException expected) {
		}
	}

	private Double2IntMap emptyMap() {
		return Double2IntMaps.empty();
	}

	private void putAll(ObjectIterable<Double2IntMap.Entry> entries) {
		Double2IntMap map = new Double2IntLinkedOpenHashMap();
		for (Double2IntMap.Entry entry : entries) {
			map.put(entry.getDoubleKey(), entry.getIntValue());
		}
		getMap().putAll(map);
	}
}