package speiger.src.testers.bytes.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.bytes.tests.base.maps.AbstractByte2BooleanMapTester;

@Ignore
public class Byte2BooleanMapGetOrDefaultTester extends AbstractByte2BooleanMapTester
{
	@CollectionSize.Require(absent = ZERO)
	public void testGetOrDefault_present() {
		assertEquals("getOrDefault(present, def) should return the associated value", v0(), getMap().getOrDefault(k0(), v3()));
	}
	
	public void testGetOrDefault_absent() {
		assertEquals("getOrDefault(absent, def) should return the default value", v3(), getMap().getOrDefault(k3(), v3()));
	}
}