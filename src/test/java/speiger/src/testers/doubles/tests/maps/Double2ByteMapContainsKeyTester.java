package speiger.src.testers.doubles.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.doubles.tests.base.maps.AbstractDouble2ByteMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Double2ByteMapContainsKeyTester extends AbstractDouble2ByteMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsKey(present) should return true", getMap().containsKey(k0()));
	}
	
	public void testContains_no() {
		assertFalse("containsKey(notPresent) should return false", getMap().containsKey(k3()));
	}
}