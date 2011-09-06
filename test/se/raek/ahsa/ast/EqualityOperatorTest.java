package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class EqualityOperatorTest {

	@Test
	public void matchEqual() {
		assertTrue(EqualityOperator.EQUAL.matchEqualityOperator(new EqualityOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseEqual() {
				return true;
			}
			@Override
			public Boolean caseUnequal() {
				return false;
			}
		}));
	}

	@Test
	public void matchUnequal() {
		assertTrue(EqualityOperator.UNEQUAL.matchEqualityOperator(new EqualityOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseEqual() {
				return false;
			}
			@Override
			public Boolean caseUnequal() {
				return true;
			}
		}));
	}

}
