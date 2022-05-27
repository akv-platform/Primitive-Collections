package speiger.src.testers.booleans.tests.collection;

import org.junit.Ignore;

import speiger.src.testers.booleans.tests.base.AbstractBooleanCollectionTester;

@Ignore
@SuppressWarnings("javadoc")
public class BooleanCollectionEqualsTester extends AbstractBooleanCollectionTester
{
	public void testEquals_self() {
		assertTrue("An Object should be equal to itself.", collection.equals(collection));
	}
	
	public void testEquals_null() {
		// noinspection ObjectEqualsNull
		assertFalse("An object should not be equal to null.", collection.equals(null));
	}
	
	public void testEquals_notACollection() {
		// noinspection EqualsBetweenInconvertibleTypes
		assertFalse("A Collection should never equal an object that is not a Collection.", collection.equals("huh?"));
	}
}