package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryOperatorTest {

	@Test
	public void matchAddition() {
		assertTrue(BinaryOperator.ADDITION.matchBinaryOperator(new BinaryOperator.Matcher<Boolean>() {
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
		assertTrue(BinaryOperator.SUBTRACTION.matchBinaryOperator(new BinaryOperator.Matcher<Boolean>() {
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
		assertTrue(BinaryOperator.MULTIPLICATION.matchBinaryOperator(new BinaryOperator.Matcher<Boolean>() {
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
		assertTrue(BinaryOperator.DIVISION.matchBinaryOperator(new BinaryOperator.Matcher<Boolean>() {
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
