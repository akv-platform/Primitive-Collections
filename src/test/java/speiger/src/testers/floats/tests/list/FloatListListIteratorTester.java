package speiger.src.testers.floats.tests.list;

import org.junit.Ignore;

import static com.google.common.collect.testing.IteratorFeature.MODIFIABLE;
import static com.google.common.collect.testing.IteratorFeature.UNMODIFIABLE;
import static com.google.common.collect.testing.features.CollectionFeature.SUPPORTS_REMOVE;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_ADD_WITH_INDEX;
import static com.google.common.collect.testing.features.ListFeature.SUPPORTS_SET;

import java.util.Set;

import com.google.common.collect.testing.IteratorFeature;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.ListFeature;

import speiger.src.collections.floats.lists.FloatList;
import speiger.src.collections.floats.lists.FloatListIterator;
import speiger.src.collections.floats.utils.FloatLists;
import speiger.src.testers.floats.tests.base.AbstractFloatListTester;
import speiger.src.testers.floats.utils.FloatHelpers;
import speiger.src.testers.floats.utils.FloatListIteratorTester;

@Ignore
@SuppressWarnings("javadoc")
public class FloatListListIteratorTester extends AbstractFloatListTester
{
	@CollectionFeature.Require(absent = SUPPORTS_REMOVE)
	@ListFeature.Require(absent = { SUPPORTS_SET, SUPPORTS_ADD_WITH_INDEX })
	public void testListIterator_unmodifiable() {
		runListIteratorTest(UNMODIFIABLE);
	}
	
	@CollectionFeature.Require(SUPPORTS_REMOVE)
	@ListFeature.Require({ SUPPORTS_SET, SUPPORTS_ADD_WITH_INDEX })
	public void testListIterator_fullyModifiable() {
		runListIteratorTest(MODIFIABLE);
	}

	private void runListIteratorTest(Set<IteratorFeature> features) {
		new FloatListIteratorTester(4, FloatLists.singleton(e4()), features, FloatHelpers.copyToList(getOrderedElements()), 0) {
			@Override
			protected FloatListIterator newTargetIterator() {
				resetCollection();
				return getList().listIterator();
			}

			@Override
			protected void verify(FloatList elements) {
				expectContents(elements);
			}
		}.test();
	}

	public void testListIterator_tooLow() {
		try {
			getList().listIterator(-1);
			fail();
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testListIterator_tooHigh() {
		try {
			getList().listIterator(getNumElements() + 1);
			fail();
		} catch (IndexOutOfBoundsException expected) {
		}
	}

	public void testListIterator_atSize() {
		getList().listIterator(getNumElements());
	}
}