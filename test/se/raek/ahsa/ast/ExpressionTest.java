package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.Expression.BinaryOperation;
import se.raek.ahsa.ast.BinaryOperator;
import se.raek.ahsa.ast.Expression.ValueLookup;
import se.raek.ahsa.ast.Expression.VariableLookup;
import se.raek.ahsa.runtime.Value;

import org.junit.Test;

public class ExpressionTest {
	
	private static final ValueLocation valX = new ValueLocation("x");
	private static final ValueLocation valY = new ValueLocation("y");
	private static final VariableLocation varX = new VariableLocation("x");
	private static final VariableLocation varY = new VariableLocation("y");

	@Test
	public void matchConstant() {
		assertTrue(Constant.make(Value.Null.make()).matchExpression(new Expression.Matcher<Boolean>() {
			@Override
			public Boolean caseConstant(Value v) {
				return true;
			}
			@Override
			public Boolean caseValueLookup(ValueLocation val) {
				return false;
			}
			@Override
			public Boolean caseVariableLookup(VariableLocation var) {
				return false;
			}
			@Override
			public Boolean caseBinaryOperation(BinaryOperator op,
					Expression left, Expression right) {
				return false;
			}
		}));
	}

	@Test
	public void matchValueLookup() {
		assertTrue(ValueLookup.make(valX).matchExpression(new Expression.Matcher<Boolean>() {
			@Override
			public Boolean caseConstant(Value v) {
				return false;
			}
			@Override
			public Boolean caseValueLookup(ValueLocation val) {
				return val.equals(valX);
			}
			@Override
			public Boolean caseVariableLookup(VariableLocation var) {
				return false;
			}
			@Override
			public Boolean caseBinaryOperation(BinaryOperator op,
					Expression left, Expression right) {
				return false;
			}
		}));
	}

	@Test
	public void matchVariableLookup() {
		assertTrue(VariableLookup.make(varX).matchExpression(new Expression.Matcher<Boolean>() {
			@Override
			public Boolean caseConstant(Value v) {
				return false;
			}
			@Override
			public Boolean caseValueLookup(ValueLocation val) {
				return false;
			}
			@Override
			public Boolean caseVariableLookup(VariableLocation var) {
				return var.equals(varX);
			}
			@Override
			public Boolean caseBinaryOperation(BinaryOperator op,
					Expression left, Expression right) {
				return false;
			}
		}));
	}

	@Test
	public void matchBinaryOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr = BinaryOperation.make(BinaryOperator.ADDITION, term, term);
		assertTrue(expr.matchExpression(new Expression.Matcher<Boolean>() {
			@Override
			public Boolean caseConstant(Value v) {
				return false;
			}
			@Override
			public Boolean caseValueLookup(ValueLocation val) {
				return false;
			}
			@Override
			public Boolean caseVariableLookup(VariableLocation var) {
				return false;
			}
			@Override
			public Boolean caseBinaryOperation(BinaryOperator op,
					Expression left, Expression right) {
				return true;
			}
		}));
	}
	
	@Test
	public void equalsConstant() {
		assertTrue(Constant.make(Value.Null.make()).equals(Constant.make(Value.Null.make())));
	}
	
	@Test
	public void equalsValueLookup() {
		assertTrue(ValueLookup.make(valX).equals(ValueLookup.make(valX)));
	}
	
	@Test
	public void equalsVariableLookup() {
		assertTrue(VariableLookup.make(varX).equals(VariableLookup.make(varX)));
	}
	
	@Test
	public void equalsBinaryOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		BinaryOperator op = BinaryOperator.ADDITION;
		Expression expr1 = BinaryOperation.make(op, term, term);
		Expression expr2 = BinaryOperation.make(op, term, term);
		assertTrue(expr1.equals(expr2));
	}
	 
	@Test
	public void equalHashCodeConstant() {
		assertTrue(Constant.make(Value.Null.make()).hashCode() == Constant.make(Value.Null.make()).hashCode());
	}
	 
	@Test
	public void equalHashCodeValueLookup() {
		assertTrue(ValueLookup.make(valX).hashCode() == ValueLookup.make(valX).hashCode());
	}
	 
	@Test
	public void equalHashCodeVariableLookup() {
		assertTrue(VariableLookup.make(varX).hashCode() == VariableLookup.make(varX).hashCode());
	}
	
	@Test
	public void equalHashCodeBinaryOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		BinaryOperator op = BinaryOperator.ADDITION;
		Expression expr1 = BinaryOperation.make(op, term, term);
		Expression expr2 = BinaryOperation.make(op, term, term);
		assertTrue(expr1.hashCode() == expr2.hashCode());
	}
	
	@Test
	public void notEqualConstant() {
		assertFalse(Constant.make(Value.Number.make(1.0)).equals(Constant.make(Value.Number.make(2.0))));
	}
	
	@Test
	public void notEqualValueLookup() {
		assertFalse(ValueLookup.make(valX).equals(ValueLookup.make(valY)));
	}
	
	@Test
	public void notEqualVariableLookup() {
		assertFalse(VariableLookup.make(varX).equals(VariableLookup.make(varY)));
	}
	
	@Test
	public void notEqualBinaryOperationOp() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr1 = BinaryOperation.make(BinaryOperator.ADDITION, term, term);
		Expression expr2 = BinaryOperation.make(BinaryOperator.SUBTRACTION, term, term);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualBinaryOperationLeft() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		BinaryOperator op = BinaryOperator.ADDITION;
		Expression expr1 = BinaryOperation.make(op, term1, term3);
		Expression expr2 = BinaryOperation.make(op, term2, term3);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualBinaryOperationRight() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		BinaryOperator op = BinaryOperator.ADDITION;
		Expression expr1 = BinaryOperation.make(op, term3, term1);
		Expression expr2 = BinaryOperation.make(op, term3, term2);
		assertFalse(expr1.equals(expr2));
	}

}
