package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2IntMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2IntMapReplaceEntryTester extends AbstractInt2IntMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceEntry_supportedPresent() {
		try {
			assertTrue(getMap().replace(k0(), v0(), v3()));
			expectReplacement(entry(k0(), v3()));
		} catch (ClassCastException tolerated) { // for ClassToInstanceMap
			expectUnchanged();
		}
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceEntry_supportedPresentUnchanged() {
		assertTrue(getMap().replace(k0(), v0(), v0()));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceEntry_supportedWrongValue() {
		assertFalse(getMap().replace(k0(), v3(), v4()));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplaceEntry_supportedAbsentKey() {
		assertFalse(getMap().replace(k3(), v3(), v4()));
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceEntry_unsupportedPresent() {
		try {
			getMap().replace(k0(), v0(), v3());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplaceEntry_unsupportedWrongValue() {
		try {
			getMap().replace(k0(), v3(), v4());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	public void testReplaceEntry_unsupportedAbsentKey() {
		try {
			getMap().replace(k3(), v3(), v4());
		} catch (UnsupportedOperationException tolerated) {
		}
		expectUnchanged();
	}
}