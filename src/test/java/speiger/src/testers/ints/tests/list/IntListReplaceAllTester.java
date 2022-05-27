package speiger.src.testers.ints.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Arrays;

import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.testers.ints.tests.base.AbstractIntListTester;

@Ignore
@SuppressWarnings("javadoc")
public class IntListReplaceAllTester extends AbstractIntListTester
{
	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll() {
		getList().replaceInts(e -> samples.e3());
		int[] array = new int[getNumElements()];
		Arrays.fill(array, samples.e3());
		expectContents(array);
	}

	@ListFeature.Require(SUPPORTS_SET)
	public void testReplaceAll_changesSome() {
		getList().replaceInts(e -> e == samples.e0() ? samples.e3() : e);
		int[] expected = createSamplesArray();
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] == samples.e0()) {
				expected[i] = samples.e3();
			}
		}
		expectContents(expected);
	}

	@CollectionSize.Require(absent = ZERO)
	@ListFeature.Require(absent = SUPPORTS_SET)
	public void testReplaceAll_unsupported() {
		try {
			getList().replaceInts(e -> e);
			fail("replaceAll() should throw UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
		}
		expectUnchanged();
	}
}