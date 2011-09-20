package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityOperatorTest {

	@Test
	public void matchEqual() {
		assertTrue(EqualityOperator.EQUAL.matchEqualityOperator(new EqualityOperator.Matcher<Boolean>() {
			public Boolean caseEqual() {
				return true;
			}
			public Boolean caseUnequal() {
				return false;
			}
		}));
	}

	@Test
	public void matchUnequal() {
		assertTrue(EqualityOperator.UNEQUAL.matchEqualityOperator(new EqualityOperator.Matcher<Boolean>() {
			public Boolean caseEqual() {
				return false;
			}
			public Boolean caseUnequal() {
				return true;
			}
		}));
	}

}
