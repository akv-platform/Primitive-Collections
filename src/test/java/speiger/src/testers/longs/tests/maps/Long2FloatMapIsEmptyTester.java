package speiger.src.testers.longs.tests.maps;

import static com.google.common.collect.testing.features.CollectionSize.ZERO;

import org.junit.Ignore;

import com.google.common.collect.testing.features.CollectionSize;

import speiger.src.testers.longs.tests.base.maps.AbstractLong2FloatMapTester;

@Ignore
@SuppressWarnings("javadoc")
public class Long2FloatMapIsEmptyTester extends AbstractLong2FloatMapTester 
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