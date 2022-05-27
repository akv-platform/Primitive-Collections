package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2ObjectMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ObjectMapSizeTester<T, V> extends AbstractObject2ObjectMapTester<T, V> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}