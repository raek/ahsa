package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.Expression.ArithmeticOperation;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.ast.Expression.EqualityOperation;
import se.raek.ahsa.ast.Expression.RelationalOperation;
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
			public Boolean caseArithmeticOperation(ArithmeticOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseEqualityOperation(EqualityOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseRelationalOperation(RelationalOperator op,
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
			public Boolean caseArithmeticOperation(ArithmeticOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseEqualityOperation(EqualityOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseRelationalOperation(RelationalOperator op,
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
			public Boolean caseArithmeticOperation(ArithmeticOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseEqualityOperation(EqualityOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseRelationalOperation(RelationalOperator op,
					Expression left, Expression right) {
				return false;
			}
		}));
	}

	@Test
	public void matchArithmeticOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr = ArithmeticOperation.make(ArithmeticOperator.ADDITION, term, term);
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
			public Boolean caseArithmeticOperation(ArithmeticOperator op,
					Expression left, Expression right) {
				return true;
			}
			@Override
			public Boolean caseEqualityOperation(EqualityOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseRelationalOperation(RelationalOperator op,
					Expression left, Expression right) {
				return false;
			}
		}));
	}

	@Test
	public void matchEqualityOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr = EqualityOperation.make(EqualityOperator.EQUAL, term, term);
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
			public Boolean caseArithmeticOperation(ArithmeticOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseEqualityOperation(EqualityOperator op,
					Expression left, Expression right) {
				return true;
			}
			@Override
			public Boolean caseRelationalOperation(RelationalOperator op,
					Expression left, Expression right) {
				return false;
			}
		}));
	}

	@Test
	public void matchRelationalOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr = RelationalOperation.make(RelationalOperator.GREATER, term, term);
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
			public Boolean caseArithmeticOperation(ArithmeticOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseEqualityOperation(EqualityOperator op,
					Expression left, Expression right) {
				return false;
			}
			@Override
			public Boolean caseRelationalOperation(RelationalOperator op,
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
	public void equalsArithmeticOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		ArithmeticOperator op = ArithmeticOperator.ADDITION;
		Expression expr1 = ArithmeticOperation.make(op, term, term);
		Expression expr2 = ArithmeticOperation.make(op, term, term);
		assertTrue(expr1.equals(expr2));
	}
	
	@Test
	public void equalsEqualityOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		EqualityOperator op = EqualityOperator.EQUAL;
		Expression expr1 = EqualityOperation.make(op, term, term);
		Expression expr2 = EqualityOperation.make(op, term, term);
		assertTrue(expr1.equals(expr2));
	}
	
	@Test
	public void equalsRelationalOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		RelationalOperator op = RelationalOperator.GREATER;
		Expression expr1 = RelationalOperation.make(op, term, term);
		Expression expr2 = RelationalOperation.make(op, term, term);
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
	public void equalHashCodeArithmeticOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		ArithmeticOperator op = ArithmeticOperator.ADDITION;
		Expression expr1 = ArithmeticOperation.make(op, term, term);
		Expression expr2 = ArithmeticOperation.make(op, term, term);
		assertTrue(expr1.hashCode() == expr2.hashCode());
	}
	
	@Test
	public void equalHashCodeEqualityOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		EqualityOperator op = EqualityOperator.EQUAL;
		Expression expr1 = EqualityOperation.make(op, term, term);
		Expression expr2 = EqualityOperation.make(op, term, term);
		assertTrue(expr1.hashCode() == expr2.hashCode());
	}
	
	@Test
	public void equalHashCodeRelationalOperation() {
		Expression term = Constant.make(Value.Number.make(1.0));
		RelationalOperator op = RelationalOperator.GREATER;
		Expression expr1 = RelationalOperation.make(op, term, term);
		Expression expr2 = RelationalOperation.make(op, term, term);
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
	public void notEqualArithmeticOperationOp() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr1 = ArithmeticOperation.make(ArithmeticOperator.ADDITION, term, term);
		Expression expr2 = ArithmeticOperation.make(ArithmeticOperator.SUBTRACTION, term, term);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualArithmeticOperationLeft() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		ArithmeticOperator op = ArithmeticOperator.ADDITION;
		Expression expr1 = ArithmeticOperation.make(op, term1, term3);
		Expression expr2 = ArithmeticOperation.make(op, term2, term3);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualArithmeticOperationRight() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		ArithmeticOperator op = ArithmeticOperator.ADDITION;
		Expression expr1 = ArithmeticOperation.make(op, term3, term1);
		Expression expr2 = ArithmeticOperation.make(op, term3, term2);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualEqualityOperationOp() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr1 = EqualityOperation.make(EqualityOperator.EQUAL, term, term);
		Expression expr2 = EqualityOperation.make(EqualityOperator.UNEQUAL, term, term);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualEqualityOperationLeft() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		EqualityOperator op = EqualityOperator.EQUAL;
		Expression expr1 = EqualityOperation.make(op, term1, term3);
		Expression expr2 = EqualityOperation.make(op, term2, term3);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualEqualityOperationRight() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		EqualityOperator op = EqualityOperator.EQUAL;
		Expression expr1 = EqualityOperation.make(op, term3, term1);
		Expression expr2 = EqualityOperation.make(op, term3, term2);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualRelationalOperationOp() {
		Expression term = Constant.make(Value.Number.make(1.0));
		Expression expr1 = RelationalOperation.make(RelationalOperator.GREATER, term, term);
		Expression expr2 = RelationalOperation.make(RelationalOperator.LESS, term, term);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualRelationalOperationLeft() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		RelationalOperator op = RelationalOperator.GREATER;
		Expression expr1 = RelationalOperation.make(op, term1, term3);
		Expression expr2 = RelationalOperation.make(op, term2, term3);
		assertFalse(expr1.equals(expr2));
	}
	
	@Test
	public void notEqualRelationalOperationRight() {
		Expression term1 = Constant.make(Value.Number.make(1.0));
		Expression term2 = Constant.make(Value.Number.make(2.0));
		Expression term3 = Constant.make(Value.Number.make(3.0));
		RelationalOperator op = RelationalOperator.GREATER;
		Expression expr1 = RelationalOperation.make(op, term3, term1);
		Expression expr2 = RelationalOperation.make(op, term3, term2);
		assertFalse(expr1.equals(expr2));
	}

}
