package speiger.src.testers.objects.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.objects.tests.base.maps.AbstractObject2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Object2FloatMapContainsValueTester<T> extends AbstractObject2FloatMapTester<T>
{
	@CollectionSize.Require(absent = ZERO)
	public void testContains_yes() {
		assertTrue("containsValue(present) should return true", getMap().containsValue(v0()));
	}

	public void testContains_no() {
		assertFalse("containsValue(notPresent) should return false", getMap().containsValue(v3()));
	}
}