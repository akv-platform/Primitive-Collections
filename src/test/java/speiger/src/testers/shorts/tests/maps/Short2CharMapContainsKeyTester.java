package speiger.src.testers.shorts.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.shorts.tests.base.maps.AbstractShort2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Short2CharMapContainsKeyTester extends AbstractShort2CharMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsKey(present) should return true", getMap().containsKey(k0()));
	}
	
	public void testContains_no() {
		assertFalse("containsKey(notPresent) should return false", getMap().containsKey(k3()));
	}
}