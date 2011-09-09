package se.raek.ahsa;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.Expression.EqualityOperation;
import se.raek.ahsa.ast.Expression.RelationalOperation;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.Expression.ValueLookup;
import se.raek.ahsa.ast.Expression.VariableLookup;
import se.raek.ahsa.ast.Statement.ValueDefinition;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.Expression.ArithmeticOperation;
import se.raek.ahsa.ast.Statement.Conditional;
import se.raek.ahsa.ast.Statement.VariableAssignment;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.runtime.Store;
import se.raek.ahsa.runtime.Value;
import se.raek.ahsa.runtime.Value.Boolean;
import se.raek.ahsa.runtime.Value.Null;
import se.raek.ahsa.runtime.Value.Number;

import static se.raek.ahsa.ast.ArithmeticOperator.*;
import static se.raek.ahsa.ast.EqualityOperator.*;
import static se.raek.ahsa.ast.RelationalOperator.*;

public class InterpreterTest {

	private final static Value v1 = Number.make(1.0);
	private final static Constant c1 = Constant.make(Number.make(1.0));
	private final static Constant c2 = Constant.make(Number.make(2.0));
	private final static Constant c3 = Constant.make(Number.make(3.0));
	private final static Constant c6 = Constant.make(Number.make(6.0));
	private final static Constant cNull = Constant.make(Null.make());
	private final static ValueLocation valX = new ValueLocation("x");
	private final static VariableLocation varY = new VariableLocation("y");

	@Test
	public void evalConstant() {
		assertEquals(v1, Interpreter.eval(Constant.make(v1), null));
	}

	@Test
	public void evalValueLookup() {
		Store sto = new Store(null);
		sto.defineValue(valX, v1);
		assertEquals(v1, Interpreter.eval(ValueLookup.make(valX), sto));
	}

	@Test
	public void evalVariableLookup() {
		Store sto = new Store(null);
		sto.assignVariable(varY, v1);
		assertEquals(v1, Interpreter.eval(VariableLookup.make(varY), sto));
	}

	@Test
	public void evalAddition() {
		assertEquals(Number.make(5), Interpreter.eval(
				ArithmeticOperation.make(ADDITION, c2, c3), null));
	}

	@Test
	public void evalSubtraction() {
		assertEquals(Number.make(-1), Interpreter.eval(
				ArithmeticOperation.make(SUBTRACTION, c2, c3), null));
	}

	@Test
	public void evalMultiplication() {
		assertEquals(
				Number.make(6),
				Interpreter.eval(
						ArithmeticOperation.make(MULTIPLICATION, c2, c3), null));
	}

	@Test
	public void evalDivision() {
		assertEquals(Number.make(2), Interpreter.eval(
				ArithmeticOperation.make(DIVISION, c6, c3), null));
	}

	@Test(expected = Interpreter.CastException.class)
	public void evalArithOpWithNonNumberArgument() {
		Interpreter.eval(ArithmeticOperation.make(ADDITION, c1, cNull), null);
	}

	@Test
	public void evalEqualSameNumbers() {
		assertEquals(Boolean.make(true),
				Interpreter.eval(EqualityOperation.make(EQUAL, c1, c1), null));
	}

	@Test
	public void evalEqualDifferentNumbers() {
		assertEquals(Boolean.make(false),
				Interpreter.eval(EqualityOperation.make(EQUAL, c1, c2), null));
	}

	@Test
	public void evalEqualDifferentTypes() {
		assertEquals(Boolean.make(false), Interpreter.eval(
				EqualityOperation.make(EQUAL, cNull, c2), null));
	}

	@Test
	public void evalUnequalSameNumbers() {
		assertEquals(Boolean.make(false),
				Interpreter.eval(EqualityOperation.make(UNEQUAL, c1, c1), null));
	}

	@Test
	public void evalUnequalDifferentNumbers() {
		assertEquals(Boolean.make(true),
				Interpreter.eval(EqualityOperation.make(UNEQUAL, c1, c2), null));
	}

	@Test
	public void evalUnequalDifferentTypes() {
		assertEquals(Boolean.make(true), Interpreter.eval(
				EqualityOperation.make(UNEQUAL, cNull, c2), null));
	}

	@Test
	public void evalGreaterTrue() {
		assertEquals(Boolean.make(true), Interpreter.eval(
				RelationalOperation.make(GREATER, c3, c2), null));
	}

	@Test
	public void evalGreaterFalse() {
		assertEquals(Boolean.make(false), Interpreter.eval(
				RelationalOperation.make(GREATER, c2, c3), null));
	}

	@Test
	public void evalLessTrue() {
		assertEquals(Boolean.make(true),
				Interpreter.eval(RelationalOperation.make(LESS, c2, c3), null));
	}

	@Test
	public void evalLessFalse() {
		assertEquals(Boolean.make(false),
				Interpreter.eval(RelationalOperation.make(LESS, c3, c2), null));
	}

	@Test
	public void evalGreaterEqualTrue() {
		assertEquals(
				Boolean.make(true),
				Interpreter.eval(
						RelationalOperation.make(GREATER_EQUAL, c3, c2), null));
	}

	@Test
	public void evalGreaterEqualFalse() {
		assertEquals(
				Boolean.make(false),
				Interpreter.eval(
						RelationalOperation.make(GREATER_EQUAL, c2, c3), null));
	}

	@Test
	public void evalLessEqualTrue() {
		assertEquals(Boolean.make(true), Interpreter.eval(
				RelationalOperation.make(LESS_EQUAL, c2, c3), null));
	}

	@Test
	public void evalLessEqualFalse() {
		assertEquals(Boolean.make(false), Interpreter.eval(
				RelationalOperation.make(LESS_EQUAL, c3, c2), null));
	}

	@Test(expected = Interpreter.CastException.class)
	public void evalRelOpWithNonNumberArgument() {
		Interpreter.eval(RelationalOperation.make(GREATER, c1, cNull), null);
	}

	@Test
	public void executeThrowawayExpression() {
		Store sto = new Store(null);
		Interpreter.execute(Statement.ThrowawayExpression.make(cNull), sto);
	}

	@Test
	public void executeValueDefinition() {
		Store sto = new Store(null);
		Interpreter.execute(Statement.ValueDefinition.make(valX, c1), sto);
		assertEquals(v1, sto.lookupValue(valX));
	}

	@Test
	public void executeVariableAssignment() {
		Store sto = new Store(null);
		Interpreter.execute(Statement.VariableAssignment.make(varY, c1), sto);
		assertEquals(v1, sto.lookupVariable(varY));
	}

	@Test
	public void executeConditionalTrue() {
		Constant c0 = Constant.make(Number.make(0.0));
		Expression lookupX = ValueLookup.make(valX);
		Store sto = new Store(null);
		List<Statement> stmts = new ArrayList<Statement>();
		stmts.add(ValueDefinition.make(valX, Constant.make(Number.make(2.0))));
		Expression cond = RelationalOperation.make(GREATER_EQUAL, lookupX, c0);
		Statement thenStmt = VariableAssignment.make(varY, lookupX);
		Statement elseStmt = VariableAssignment.make(varY,
				ArithmeticOperation.make(SUBTRACTION, c0, lookupX));
		stmts.add(Conditional.make(cond, Collections.singletonList(thenStmt),
				Collections.singletonList(elseStmt)));
		Interpreter.execute(stmts, sto);
		assertEquals(Number.make(2.0), sto.lookupVariable(varY));
	}

	@Test
	public void executeConditionalFalse() {
		Constant c0 = Constant.make(Number.make(0.0));
		Expression lookupX = ValueLookup.make(valX);
		Store sto = new Store(null);
		List<Statement> stmts = new ArrayList<Statement>();
		stmts.add(ValueDefinition.make(valX, Constant.make(Number.make(-2.0))));
		Expression cond = RelationalOperation.make(GREATER_EQUAL, lookupX, c0);
		Statement thenStmt = VariableAssignment.make(varY, lookupX);
		Statement elseStmt = VariableAssignment.make(varY,
				ArithmeticOperation.make(SUBTRACTION, c0, lookupX));
		stmts.add(Conditional.make(cond, Collections.singletonList(thenStmt),
				Collections.singletonList(elseStmt)));
		Interpreter.execute(stmts, sto);
		assertEquals(Number.make(2.0), sto.lookupVariable(varY));
	}

	@Test
	public void typeNameNull() {
		assertEquals("null", Interpreter.typeName(Null.make()));
	}

	@Test
	public void typeNameBoolean() {
		assertEquals("boolean", Interpreter.typeName(Boolean.make(true)));
	}

	@Test
	public void typeNameNumber() {
		assertEquals("number", Interpreter.typeName(Number.make(1.0)));
	}
	
	@Test
	public void isTruthyNull() {
		assertFalse(Interpreter.isTruthy(Null.make()));
	}
	
	@Test
	public void isTruthyBooleanTrue() {
		assertTrue(Interpreter.isTruthy(Boolean.make(true)));
	}
	
	@Test
	public void isTruthyBooleanFalse() {
		assertFalse(Interpreter.isTruthy(Boolean.make(false)));
	}
	
	@Test
	public void isTruthyNumberPositive() {
		assertTrue(Interpreter.isTruthy(Number.make(1.0)));
	}
	
	@Test
	public void isTruthyNumberZero() {
		assertTrue(Interpreter.isTruthy(Number.make(0.0)));
	}
	
	@Test
	public void isTruthyNumberNegative() {
		assertTrue(Interpreter.isTruthy(Number.make(-1.0)));
	}

}
