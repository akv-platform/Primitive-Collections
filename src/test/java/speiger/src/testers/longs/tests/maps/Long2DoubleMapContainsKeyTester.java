package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2DoubleMapContainsKeyTester extends AbstractLong2DoubleMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsKey(present) should return true", getMap().containsKey(k0()));
	}
	
	public void testContains_no() {
		assertFalse("containsKey(notPresent) should return false", getMap().containsKey(k3()));
	}
}