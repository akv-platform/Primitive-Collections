package speiger.src.testers.objects.tests.iterable;

import java.util.Objects;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.collections.objects.sets.ObjectOpenHashSet;
import speiger.src.collections.objects.sets.ObjectSet;
import speiger.src.testers.objects.tests.base.AbstractObjectCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class ObjectIterableMatchesTester<T> extends AbstractObjectCollectionTester<T>
{
	public void testIterableMatch_AnyNull() {
		try {
			collection.matchesAny(null);
			fail("MatchesAny should throw a NullPointException");
		}
		catch (NullPointerException e) {
		}
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableMatch_AnyFoundFirstElement() {
		assertTrue("Element ["+e0()+"] should be found", collection.matchesAny(T -> Objects.equals(T, e0())));
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableMatch_AnyFoundLastElement() {
		assertTrue("Element ["+e2()+"] should be found", collection.matchesAny(T -> Objects.equals(T, e2())));
	}
	
	public void testIterableMatch_AnyFoundNoElement() {
		assertFalse("Element ["+e4()+"] should not be found", collection.matchesAny(T -> Objects.equals(T, e4())));
	}
	
	public void testIterableMatch_NoneNull() {
		try {
			collection.matchesNone(null);
			fail("MatchesNone should throw a NullPointException");
		}
		catch (NullPointerException e) {
		}
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableMatch_NoneFoundFirstElement() {
		assertFalse("Element ["+e0()+"] should not be found", collection.matchesNone(T -> Objects.equals(T, e0())));
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableMatch_NoneFoundLastElement() {
		assertFalse("Element ["+e2()+"] should not be found", collection.matchesNone(T -> Objects.equals(T, e2())));
	}
	
	public void testIterableMatch_NoneFoundNoElement() {
		assertTrue("Element ["+e4()+"] should not be found", collection.matchesNone(T -> Objects.equals(T, e4())));
	}
	
	public void testIterableMatch_AllNull() {
		try {
			collection.matchesAll(null);
			fail("MatchesAny should throw a NullPointException");
		}
		catch (NullPointerException e) {
		}
	}
	
	public void testIterableMatch_AllFoundAllElements() {
		ObjectSet<T> set = new ObjectOpenHashSet<>(collection);
		assertTrue("All elements should be found", collection.matchesAll(set::contains));
	}
	
	@CollectionSize.Require(absent = CollectionSize.ZERO)
	public void testIterableMatch_AllFoundNone() {
		assertFalse("It should not find anything", collection.matchesAll(T -> false));
	}
	
	@CollectionSize.Require(CollectionSize.ZERO)
	public void testIterableMatch_AllFoundNoneEmpty() {
		assertTrue("Empty Collections should return true even if all have to be found", collection.matchesAll(T -> false));
	}
	
	@CollectionSize.Require(CollectionSize.SEVERAL)
	public void testIterableMatches_AllPartical() {
		assertFalse("Even if some elements were found, it should return false", collection.matchesAll(T -> Objects.equals(T, e0())));
	}
}