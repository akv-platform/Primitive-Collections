package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionFeature.NON_STANDARD_TOSTRING;
import static com.google.common.collect.testing.features.CollectionSize.ONE;
import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import java.util.Set;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.ints.maps.impl.hash.Int2DoubleLinkedOpenHashMap;
import speiger.src.collections.ints.maps.interfaces.Int2DoubleMap;
import speiger.src.testers.ints.tests.base.maps.AbstractInt2DoubleMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2DoubleMapToStringTester extends AbstractInt2DoubleMapTester
{
	public void testToString_minimal() {
		assertNotNull("toString() should not return null", getMap().toString());
	}

	@CollectionSize.Require(ZERO)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size0() {
		assertEquals("emptyMap.toString should return {}", "{}", getMap().toString());
	}

	@CollectionSize.Require(ONE)
	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_size1() {
		assertEquals("size1Map.toString should return {entry}", "{" + e0() + "}", getMap().toString());
	}

	@CollectionFeature.Require(absent = NON_STANDARD_TOSTRING)
	public void testToString_formatting() {
		assertEquals("map.toString() incorrect", expectedToString(getMap().int2DoubleEntrySet()), getMap().toString());
	}

	private String expectedToString(Set<Int2DoubleMap.Entry> entries) {
		Int2DoubleMap reference = new Int2DoubleLinkedOpenHashMap();
		for (Int2DoubleMap.Entry entry : entries) {
			reference.put(entry.getIntKey(), entry.getDoubleValue());
		}
		return reference.toString();
	}
}