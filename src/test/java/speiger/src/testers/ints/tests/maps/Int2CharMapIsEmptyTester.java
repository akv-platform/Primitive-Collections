package speiger.src.testers.ints.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.ints.tests.base.maps.AbstractInt2CharMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Int2CharMapIsEmptyTester extends AbstractInt2CharMapTester 
{
	  @CollectionSize.Require(ZERO)
	  public void testIsEmpty_yes() {
	    assertTrue("isEmpty() should return true", getMap().isEmpty());
	  }

	  @CollectionSize.Require(absent = ZERO)
	  public void testIsEmpty_no() {
	    assertFalse("isEmpty() should return false", getMap().isEmpty());
	  }
}