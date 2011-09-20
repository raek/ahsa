package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArithmeticOperatorTest {

	@Test
	public void matchAddition() {
		assertTrue(ArithmeticOperator.ADDITION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			public Boolean caseAddition() {
				return true;
			}
			public Boolean caseSubtraction() {
				return false;
			}
			public Boolean caseMultiplication() {
				return false;
			}
			public Boolean caseDivision() {
				return false;
			}
		}));
	}

	@Test
	public void matchSubstraction() {
		assertTrue(ArithmeticOperator.SUBTRACTION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			public Boolean caseAddition() {
				return false;
			}
			public Boolean caseSubtraction() {
				return true;
			}
			public Boolean caseMultiplication() {
				return false;
			}
			public Boolean caseDivision() {
				return false;
			}
		}));
	}

	@Test
	public void matchMultiplication() {
		assertTrue(ArithmeticOperator.MULTIPLICATION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			public Boolean caseAddition() {
				return false;
			}
			public Boolean caseSubtraction() {
				return false;
			}
			public Boolean caseMultiplication() {
				return true;
			}
			public Boolean caseDivision() {
				return false;
			}
		}));
	}

	@Test
	public void matchDivision() {
		assertTrue(ArithmeticOperator.DIVISION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			public Boolean caseAddition() {
				return false;
			}
			public Boolean caseSubtraction() {
				return false;
			}
			public Boolean caseMultiplication() {
				return false;
			}
			public Boolean caseDivision() {
				return true;
			}
		}));
	}

}
