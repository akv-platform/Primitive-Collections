package speiger.src.testers.objects.tests.maps;

import org.junit.Ignore;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2ShortMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2ShortMapSizeTester<T> extends AbstractObject2ShortMapTester<T> {
	
	public void testSize() {
		assertEquals("size():", getNumElements(), getMap().size());
	}
}