package se.raek.ahsa;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.Expression.EqualityOperation;
import se.raek.ahsa.ast.Expression.RelationalOperation;
import se.raek.ahsa.ast.Expression.ValueLookup;
import se.raek.ahsa.ast.Expression.VariableLookup;
import se.raek.ahsa.ast.Statement.ThrowawayExpression;
import se.raek.ahsa.ast.Statement.ValueDefinition;
import se.raek.ahsa.ast.Statement.VariableAssignment;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.Expression.ArithmeticOperation;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.runtime.Value.Null;
import se.raek.ahsa.runtime.Value.Number;
import se.raek.ahsa.runtime.Value.Boolean;

import static se.raek.ahsa.ast.ArithmeticOperator.*;
import static se.raek.ahsa.ast.EqualityOperator.*;
import static se.raek.ahsa.ast.RelationalOperator.*;

public class PrinterTest {

	private final Constant left = Constant.make(Number.make(2.0));
	private final Constant right = Constant.make(Number.make(3.0));

	@Test
	public void printNull() {
		assertEquals("null", Printer.toString(Null.make()));
	}

	@Test
	public void printTrue() {
		assertEquals("true", Printer.toString(Boolean.make(true)));
	}

	@Test
	public void printFalse() {
		assertEquals("false", Printer.toString(Boolean.make(false)));
	}

	@Test
	public void printNumber() {
		assertEquals(123.0,
				Double.valueOf(Printer.toString(Number.make(123.0))), 0.01);
	}

	@Test
	public void printConstant() {
		assertEquals("null", Printer.toString(Constant.make(Null.make())));
	}

	@Test
	public void printValueLookup() {
		ValueLocation val = new ValueLocation("x");
		assertEquals("x", Printer.toString(ValueLookup.make(val)));
	}

	@Test
	public void printVariableLookup() {
		VariableLocation var = new VariableLocation("y");
		assertEquals("y", Printer.toString(VariableLookup.make(var)));
	}

	@Test
	public void printAddition() {
		Expression expr = ArithmeticOperation.make(ADDITION, left, right);
		String expected = "(" + Printer.toString(left) + "+"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printSubtraction() {
		Expression expr = ArithmeticOperation.make(SUBTRACTION, left, right);
		String expected = "(" + Printer.toString(left) + "-"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printMultiplication() {
		Expression expr = ArithmeticOperation.make(MULTIPLICATION, left, right);
		String expected = "(" + Printer.toString(left) + "*"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printDivision() {
		Expression expr = ArithmeticOperation.make(DIVISION, left, right);
		String expected = "(" + Printer.toString(left) + "/"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printEqual() {
		Expression expr = EqualityOperation.make(EQUAL, left, right);
		String expected = "(" + Printer.toString(left) + "=="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printUnequal() {
		Expression expr = EqualityOperation.make(UNEQUAL, left, right);
		String expected = "(" + Printer.toString(left) + "!="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printGreater() {
		Expression expr = RelationalOperation.make(GREATER, left, right);
		String expected = "(" + Printer.toString(left) + ">"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printLess() {
		Expression expr = RelationalOperation.make(LESS, left, right);
		String expected = "(" + Printer.toString(left) + "<"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printGreaterEqual() {
		Expression expr = RelationalOperation.make(GREATER_EQUAL, left, right);
		String expected = "(" + Printer.toString(left) + ">="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printLessEqual() {
		Expression expr = RelationalOperation.make(LESS_EQUAL, left, right);
		String expected = "(" + Printer.toString(left) + "<="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printThrowawayExpression() {
		assertEquals("null;", Printer.toString(ThrowawayExpression
				.make(Constant.make(Null.make()))));
	}

	@Test
	public void printValueDefinition() {
		assertEquals("val x=null;", Printer.toString(ValueDefinition.make(
				new ValueLocation("x"), Constant.make(Null.make()))));
	}

	@Test
	public void printVariableAssignment() {
		assertEquals("x=null;", Printer.toString(VariableAssignment.make(
				new VariableLocation("x"), Constant.make(Null.make()))));
	}

}
