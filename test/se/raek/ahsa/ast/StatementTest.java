package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.Statement.Conditional;
import se.raek.ahsa.ast.Statement.Matcher;
import se.raek.ahsa.ast.Statement.ThrowawayExpression;
import se.raek.ahsa.ast.Statement.ValueDefinition;
import se.raek.ahsa.ast.Statement.VariableAssignment;
import se.raek.ahsa.runtime.Value;
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
			@Override
			public Boolean caseConditional(Expression cond,
					List<Statement> thenStmts, List<Statement> elseStmts) {
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
			@Override
			public Boolean caseConditional(Expression cond,
					List<Statement> thenStmts, List<Statement> elseStmts) {
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
			@Override
			public Boolean caseConditional(Expression cond,
					List<Statement> thenStmts, List<Statement> elseStmts) {
				return false;
			}
		}));
	}

	@Test
	public void matchConditional() {
		final Expression cond0 = Constant.make(Null.make());
		final List<Statement> thenStmts0 = new ArrayList<Statement>();
		thenStmts0.add(ThrowawayExpression.make(Constant.make(Number.make(1.0))));
		final List<Statement> elseStmts0 = new ArrayList<Statement>();
		elseStmts0.add(ThrowawayExpression.make(Constant.make(Number.make(2.0))));
		assertTrue(Conditional.make(cond0, thenStmts0, elseStmts0).matchStatement(new Matcher<Boolean>() {
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
				return false;
			}
			@Override
			public Boolean caseConditional(Expression cond,
					List<Statement> thenStmts, List<Statement> elseStmts) {
				return cond.equals(cond0) && thenStmts.equals(thenStmts0) && elseStmts.equals(elseStmts0);
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
	public void equalsConditional() {
		Expression cond = Constant.make(Null.make());
		List<Statement> thenStmts = new ArrayList<Statement>();
		thenStmts.add(ThrowawayExpression.make(Constant.make(Number.make(1.0))));
		List<Statement> elseStmts = new ArrayList<Statement>();
		elseStmts.add(ThrowawayExpression.make(Constant.make(Number.make(2.0))));
		assertTrue(Conditional.make(cond, thenStmts, elseStmts).equals(Conditional.make(cond, thenStmts, elseStmts)));
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
	public void notEqualConditionalCond() {
		Expression cond1 = Constant.make(Value.Boolean.make(true));
		Expression cond2 = Constant.make(Value.Boolean.make(false));
		List<Statement> thenStmts = new ArrayList<Statement>();
		thenStmts.add(ThrowawayExpression.make(Constant.make(Number.make(1.0))));
		List<Statement> elseStmts = new ArrayList<Statement>();
		elseStmts.add(ThrowawayExpression.make(Constant.make(Number.make(2.0))));
		assertFalse(Conditional.make(cond1, thenStmts, elseStmts).equals(Conditional.make(cond2, thenStmts, elseStmts)));
	}
	
	@Test
	public void notEqualConditionalThenStmts() {
		Expression cond = Constant.make(Null.make());
		final List<Statement> thenStmts1 = new ArrayList<Statement>();
		thenStmts1.add(ThrowawayExpression.make(Constant.make(Number.make(1.0))));
		final List<Statement> thenStmts2 = new ArrayList<Statement>();
		thenStmts2.add(ThrowawayExpression.make(Constant.make(Number.make(3.0))));
		final List<Statement> elseStmts = new ArrayList<Statement>();
		elseStmts.add(ThrowawayExpression.make(Constant.make(Number.make(2.0))));
		assertFalse(Conditional.make(cond, thenStmts1, elseStmts).equals(Conditional.make(cond, thenStmts2, elseStmts)));
	}
	
	@Test
	public void notEqualConditionalElseStmts() {
		Expression cond = Constant.make(Null.make());
		final List<Statement> thenStmts = new ArrayList<Statement>();
		thenStmts.add(ThrowawayExpression.make(Constant.make(Number.make(1.0))));
		final List<Statement> elseStmts1 = new ArrayList<Statement>();
		elseStmts1.add(ThrowawayExpression.make(Constant.make(Number.make(2.0))));
		final List<Statement> elseStmts2 = new ArrayList<Statement>();
		elseStmts2.add(ThrowawayExpression.make(Constant.make(Number.make(3.0))));
		assertFalse(Conditional.make(cond, thenStmts, elseStmts1).equals(Conditional.make(cond, thenStmts, elseStmts2)));
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
	
	@Test
	public void equalHashCodeConditional() {
		Expression cond = Constant.make(Null.make());
		List<Statement> thenStmts = new ArrayList<Statement>();
		thenStmts.add(ThrowawayExpression.make(Constant.make(Number.make(1.0))));
		List<Statement> elseStmts = new ArrayList<Statement>();
		elseStmts.add(ThrowawayExpression.make(Constant.make(Number.make(2.0))));
		assertTrue(Conditional.make(cond, thenStmts, elseStmts).hashCode() == Conditional.make(cond, thenStmts, elseStmts).hashCode());
	}

}
