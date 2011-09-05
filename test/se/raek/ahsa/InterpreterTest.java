package se.raek.ahsa;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.BinaryOperator;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.Expression.ValueLookup;
import se.raek.ahsa.ast.Expression.VariableLookup;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.Expression.BinaryOperation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.runtime.Store;
import se.raek.ahsa.runtime.Value;
import se.raek.ahsa.runtime.Value.Null;
import se.raek.ahsa.runtime.Value.Number;

public class InterpreterTest {

	@Test
	public void evalConstant() {
		Value v = Number.make(0);
		assertEquals(v, Interpreter.eval(Constant.make(v), null));
	}

	@Test
	public void evalValueLookup() {
		Value v = Number.make(0);
		ValueLocation val = new ValueLocation("x");
		Store sto = new Store(null);
		sto.defineValue(val, v);
		assertEquals(v, Interpreter.eval(ValueLookup.make(val), sto));
	}

	@Test
	public void evalVariableLookup() {
		Value v = Number.make(0);
		VariableLocation var = new VariableLocation("x");
		Store sto = new Store(null);
		sto.assignVariable(var, v);
		assertEquals(v, Interpreter.eval(VariableLookup.make(var), sto));
	}

	@Test
	public void evalAddition() {
		assertEquals(Number.make(5), Interpreter.eval(BinaryOperation.make(
				BinaryOperator.ADDITION, Constant.make(Number.make(2)),
				Constant.make(Number.make(3))), null));
	}

	@Test
	public void evalSubtraction() {
		assertEquals(Number.make(-1), Interpreter.eval(BinaryOperation.make(
				BinaryOperator.SUBTRACTION, Constant.make(Number.make(2)),
				Constant.make(Number.make(3))), null));
	}

	@Test
	public void evalMultiplication() {
		assertEquals(Number.make(6), Interpreter.eval(BinaryOperation.make(
				BinaryOperator.MULTIPLICATION, Constant.make(Number.make(2)),
				Constant.make(Number.make(3))), null));
	}

	@Test
	public void evalDivision() {
		assertEquals(Number.make(2), Interpreter.eval(BinaryOperation.make(
				BinaryOperator.DIVISION, Constant.make(Number.make(6)),
				Constant.make(Number.make(3))), null));
	}

	@Test(expected=Interpreter.CastException.class)
	public void evalBinOpWithNonNumberArgument() {
		Interpreter.eval(BinaryOperation.make(BinaryOperator.ADDITION,
				Constant.make(Number.make(1)), Constant.make(Null.make())), null);
	}
	
	@Test
	public void executeThrowawayExpression() {
		Store sto = new Store(null);
		Interpreter.execute(Statement.ThrowawayExpression.make(Constant.make(Null.make())), sto);
	}
	
	@Test
	public void executeValueDefinition() {
		ValueLocation val = new ValueLocation("x");
		Value v = Number.make(1.0);
		Store sto = new Store(null);
		Interpreter.execute(Statement.ValueDefinition.make(val, Constant.make(v)), sto);
		assertEquals(v, sto.lookupValue(val));
	}
	
	@Test
	public void executeVariableAssignment() {
		VariableLocation var = new VariableLocation("x");
		Value v = Number.make(1.0);
		Store sto = new Store(null);
		Interpreter.execute(Statement.VariableAssignment.make(var, Constant.make(v)), sto);
		assertEquals(v, sto.lookupVariable(var));
	}

}
