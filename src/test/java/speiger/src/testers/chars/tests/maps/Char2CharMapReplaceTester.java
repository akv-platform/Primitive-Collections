package speiger.src.testers.chars.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.MapFeature.SUPPORTS_PUT;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.MapFeature;

import speiger.src.testers.chars.tests.base.maps.AbstractChar2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Char2CharMapReplaceTester extends AbstractChar2CharMapTester
{
	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplace_supportedPresent() {
		try {
			assertEquals(v0(), getMap().replace(k0(), v3()));
			expectReplacement(entry(k0(), v3()));
		} catch (ClassCastException tolerated) { // for ClassToInstanceMap
			expectUnchanged();
		}
	}

	@MapFeature.Require(SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplace_supportedPresentNoChange() {
		assertEquals(v0(), getMap().replace(k0(), v0()));
		expectUnchanged();
	}

	@MapFeature.Require(SUPPORTS_PUT)
	public void testReplace_supportedAbsent() {
		assertEquals((char)-1, getMap().replace(k3(), v3()));
		expectUnchanged();
	}

	@MapFeature.Require(absent = SUPPORTS_PUT)
	@CollectionSize.Require(absent = ZERO)
	public void testReplace_unsupportedPresent() {
		try {
			getMap().replace(k0(), v3());
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		} catch (ClassCastException tolerated) {
			// for ClassToInstanceMap
		}
		expectUnchanged();
	}
}