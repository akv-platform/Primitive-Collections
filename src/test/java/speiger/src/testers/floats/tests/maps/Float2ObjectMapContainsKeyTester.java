package speiger.src.testers.floats.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.floats.tests.base.maps.AbstractFloat2ObjectMapTester;

@Ignore
public class Float2ObjectMapContainsKeyTester<V> extends AbstractFloat2ObjectMapTester<V>
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsKey(present) should return true", getMap().containsKey(k0()));
	}
	
	public void testContains_no() {
		assertFalse("containsKey(notPresent) should return false", getMap().containsKey(k3()));
	}
}