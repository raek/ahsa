package se.raek.ahsa;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.BinaryOperator;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.Expression.BinaryOperation;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.runtime.Value.Null;
import se.raek.ahsa.runtime.Value.Number;
import se.raek.ahsa.runtime.Value.Boolean;

public class PrinterTest {

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
	public void printAddition() {
		Constant left = Constant.make(Number.make(2));
		Constant right = Constant.make(Number.make(3));
		Expression expr = BinaryOperation.make(BinaryOperator.ADDITION, left,
				right);
		String expected = "(" + Printer.toString(left) + "+"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printSubtraction() {
		Constant left = Constant.make(Number.make(2));
		Constant right = Constant.make(Number.make(3));
		Expression expr = BinaryOperation.make(BinaryOperator.SUBTRACTION,
				left, right);
		String expected = "(" + Printer.toString(left) + "-"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printMultiplication() {
		Constant left = Constant.make(Number.make(2));
		Constant right = Constant.make(Number.make(3));
		Expression expr = BinaryOperation.make(BinaryOperator.MULTIPLICATION,
				left, right);
		String expected = "(" + Printer.toString(left) + "*"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printDivision() {
		Constant left = Constant.make(Number.make(2));
		Constant right = Constant.make(Number.make(3));
		Expression expr = BinaryOperation.make(BinaryOperator.DIVISION, left,
				right);
		String expected = "(" + Printer.toString(left) + "/"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

}
