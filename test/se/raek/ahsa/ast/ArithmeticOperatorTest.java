package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArithmeticOperatorTest {

	@Test
	public void matchAddition() {
		assertTrue(ArithmeticOperator.ADDITION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseAddition() {
				return true;
			}
			@Override
			public Boolean caseSubtraction() {
				return false;
			}
			@Override
			public Boolean caseMultiplication() {
				return false;
			}
			@Override
			public Boolean caseDivision() {
				return false;
			}
		}));
	}

	@Test
	public void matchSubstraction() {
		assertTrue(ArithmeticOperator.SUBTRACTION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseAddition() {
				return false;
			}
			@Override
			public Boolean caseSubtraction() {
				return true;
			}
			@Override
			public Boolean caseMultiplication() {
				return false;
			}
			@Override
			public Boolean caseDivision() {
				return false;
			}
		}));
	}

	@Test
	public void matchMultiplication() {
		assertTrue(ArithmeticOperator.MULTIPLICATION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseAddition() {
				return false;
			}
			@Override
			public Boolean caseSubtraction() {
				return false;
			}
			@Override
			public Boolean caseMultiplication() {
				return true;
			}
			@Override
			public Boolean caseDivision() {
				return false;
			}
		}));
	}

	@Test
	public void matchDivision() {
		assertTrue(ArithmeticOperator.DIVISION.matchArithmeticOperator(new ArithmeticOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseAddition() {
				return false;
			}
			@Override
			public Boolean caseSubtraction() {
				return false;
			}
			@Override
			public Boolean caseMultiplication() {
				return false;
			}
			@Override
			public Boolean caseDivision() {
				return true;
			}
		}));
	}

}
