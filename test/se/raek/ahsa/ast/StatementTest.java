package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.Statement.Matcher;
import se.raek.ahsa.ast.Statement.ThrowawayExpression;
import se.raek.ahsa.ast.Statement.ValueDefinition;
import se.raek.ahsa.ast.Statement.VariableAssignment;
import se.raek.ahsa.runtime.Value.Null;
import se.raek.ahsa.runtime.Value.Number;

public class StatementTest {

	@Test
	public void matchThrowawayExpression() {
		final Expression expr0 = Constant.make(Null.make());
		assertTrue(ThrowawayExpression.make(expr0).matchStatement(new Matcher<Boolean>() {
			@Override
			public Boolean caseThrowawayExpression(Expression expr) {
				return expr.equals(expr0);
			}
			@Override
			public Boolean caseValueDefinition(ValueLocation val,
					Expression expr) {
				return false;
			}
			@Override
			public Boolean caseVariableAssignment(VariableLocation var,
					Expression expr) {
				return false;
			}
		}));
	}

	@Test
	public void matchValueDefinition() {
		final ValueLocation val0 = new ValueLocation("x");
		final Expression expr0 = Constant.make(Null.make());
		assertTrue(ValueDefinition.make(val0, expr0).matchStatement(new Matcher<Boolean>() {
			@Override
			public Boolean caseThrowawayExpression(Expression expr) {
				return false;
			}
			@Override
			public Boolean caseValueDefinition(ValueLocation val,
					Expression expr) {
				return val.equals(val0) && expr.equals(expr0);
			}
			@Override
			public Boolean caseVariableAssignment(VariableLocation var,
					Expression expr) {
				return false;
			}
		}));
	}

	@Test
	public void matchVariableAssignment() {
		final VariableLocation var0 = new VariableLocation("x");
		final Expression expr0 = Constant.make(Null.make());
		assertTrue(VariableAssignment.make(var0, expr0).matchStatement(new Matcher<Boolean>() {
			@Override
			public Boolean caseThrowawayExpression(Expression expr) {
				return false;
			}
			@Override
			public Boolean caseValueDefinition(ValueLocation val,
					Expression expr) {
				return false;
			}
			@Override
			public Boolean caseVariableAssignment(VariableLocation var,
					Expression expr) {
				return var.equals(var0) && expr.equals(expr0);
			}
		}));
	}
	
	@Test
	public void equalsThrowawayExpression() {
		Expression expr = Constant.make(Null.make());
		assertTrue(ThrowawayExpression.make(expr).equals(ThrowawayExpression.make(expr)));
	}
	
	@Test
	public void equalsValueDefinition() {
		Expression expr = Constant.make(Null.make());
		ValueLocation val = new ValueLocation("x");
		assertTrue(ValueDefinition.make(val, expr).equals(ValueDefinition.make(val, expr)));
	}
	
	@Test
	public void equalsVariableAssignment() {
		Expression expr = Constant.make(Null.make());
		VariableLocation var = new VariableLocation("x");
		assertTrue(VariableAssignment.make(var, expr).equals(VariableAssignment.make(var, expr)));
	}
	
	@Test
	public void notEqualThrowawayExpression() {
		Expression expr1 = Constant.make(Number.make(1.0));
		Expression expr2 = Constant.make(Number.make(2.0));
		assertFalse(ThrowawayExpression.make(expr1).equals(ThrowawayExpression.make(expr2)));
	}
	
	@Test
	public void notEqualValueDefinitionValueLocation() {
		Expression expr = Constant.make(Null.make());
		ValueLocation valX = new ValueLocation("x");
		ValueLocation valY = new ValueLocation("y");
		assertFalse(ValueDefinition.make(valX, expr).equals(ValueDefinition.make(valY, expr)));
	}
	
	@Test
	public void notEqualValueDefinitionExpression() {
		Expression expr1 = Constant.make(Number.make(1.0));
		Expression expr2 = Constant.make(Number.make(2.0));
		ValueLocation val = new ValueLocation("x");
		assertFalse(ValueDefinition.make(val, expr1).equals(ValueDefinition.make(val, expr2)));
	}
	
	@Test
	public void notEqualVariableAssignmentVariableLocation() {
		Expression expr = Constant.make(Null.make());
		VariableLocation varX = new VariableLocation("x");
		VariableLocation varY = new VariableLocation("y");
		assertFalse(VariableAssignment.make(varX, expr).equals(VariableAssignment.make(varY, expr)));
	}
	
	@Test
	public void notEqualVariableAssignmentExpression() {
		Expression expr1 = Constant.make(Number.make(1.0));
		Expression expr2 = Constant.make(Number.make(2.0));
		VariableLocation var = new VariableLocation("x");
		assertFalse(VariableAssignment.make(var, expr1).equals(VariableAssignment.make(var, expr2)));
	}
	
	@Test
	public void equalHashCodeThrowawayExpression() {
		Expression expr = Constant.make(Null.make());
		assertTrue(ThrowawayExpression.make(expr).hashCode() == ThrowawayExpression.make(expr).hashCode());
	}
	
	@Test
	public void equalHashCodeValueDefinition() {
		Expression expr = Constant.make(Null.make());
		ValueLocation val = new ValueLocation("x");
		assertTrue(ValueDefinition.make(val, expr).hashCode() == ValueDefinition.make(val, expr).hashCode());
	}
	
	@Test
	public void equalHashCodeVariableAssignment() {
		Expression expr = Constant.make(Null.make());
		VariableLocation var = new VariableLocation("x");
		assertTrue(VariableAssignment.make(var, expr).hashCode() == VariableAssignment.make(var, expr).hashCode());
	}

}
