package se.raek.ahsa.interpreter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.interpreter.Printer;

import static se.raek.ahsa.ast.Expression.*;
import static se.raek.ahsa.ast.ArithmeticOperator.*;
import static se.raek.ahsa.ast.EqualityOperator.*;
import static se.raek.ahsa.ast.RelationalOperator.*;
import static se.raek.ahsa.ast.Statement.*;
import static se.raek.ahsa.ast.Literal.*;

public class PrinterTest {

	private final Expression left = makeConstant(makeNumber(2.0));
	private final Expression right = makeConstant(makeNumber(3.0));

	@Test
	public void printNull() {
		assertEquals("null", Printer.toString(makeNull()));
	}

	@Test
	public void printTrue() {
		assertEquals("true", Printer.toString(makeBoolean(true)));
	}

	@Test
	public void printFalse() {
		assertEquals("false", Printer.toString(makeBoolean(false)));
	}

	@Test
	public void printNumber() {
		assertEquals(123.0,
				Double.valueOf(Printer.toString(makeNumber(123.0))), 0.01);
	}

	@Test
	public void printConstant() {
		assertEquals("null", Printer.toString(makeConstant(makeNull())));
	}

	@Test
	public void printValueLookup() {
		ValueLocation val = new ValueLocation("x");
		assertEquals("x", Printer.toString(makeValueLookup(val)));
	}

	@Test
	public void printVariableLookup() {
		VariableLocation var = new VariableLocation("y");
		assertEquals("y", Printer.toString(makeVariableLookup(var)));
	}

	@Test
	public void printAddition() {
		Expression expr = makeArithmeticOperation(ADDITION, left, right);
		String expected = "(" + Printer.toString(left) + "+"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printSubtraction() {
		Expression expr = makeArithmeticOperation(SUBTRACTION, left, right);
		String expected = "(" + Printer.toString(left) + "-"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printMultiplication() {
		Expression expr = makeArithmeticOperation(MULTIPLICATION, left, right);
		String expected = "(" + Printer.toString(left) + "*"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printDivision() {
		Expression expr = makeArithmeticOperation(DIVISION, left, right);
		String expected = "(" + Printer.toString(left) + "/"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printEqual() {
		Expression expr = makeEqualityOperation(EQUAL, left, right);
		String expected = "(" + Printer.toString(left) + "=="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printUnequal() {
		Expression expr = makeEqualityOperation(UNEQUAL, left, right);
		String expected = "(" + Printer.toString(left) + "!="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printGreater() {
		Expression expr = makeRelationalOperation(GREATER, left, right);
		String expected = "(" + Printer.toString(left) + ">"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printLess() {
		Expression expr = makeRelationalOperation(LESS, left, right);
		String expected = "(" + Printer.toString(left) + "<"
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printGreaterEqual() {
		Expression expr = makeRelationalOperation(GREATER_EQUAL, left, right);
		String expected = "(" + Printer.toString(left) + ">="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printLessEqual() {
		Expression expr = makeRelationalOperation(LESS_EQUAL, left, right);
		String expected = "(" + Printer.toString(left) + "<="
				+ Printer.toString(right) + ")";
		assertEquals(expected, Printer.toString(expr));
	}

	@Test
	public void printThrowawayExpression() {
		assertEquals(
				"null;",
				Printer.toString(makeThrowawayExpression(makeConstant(makeNull()))));
	}

	@Test
	public void printValueDefinition() {
		assertEquals("val x=null;", Printer.toString(makeValueDefinition(
				new ValueLocation("x"), makeConstant(makeNull()))));
	}

	@Test
	public void printVariableAssignment() {
		assertEquals("x=null;", Printer.toString(makeVariableAssignment(
				new VariableLocation("x"), makeConstant(makeNull()))));
	}

	@Test
	public void printConditional() {
		VariableLocation var = new VariableLocation("x");
		Expression cond = makeVariableLookup(var);
		List<Statement> thenStmts = Collections
				.singletonList((Statement) makeVariableAssignment(var,
						makeConstant(makeBoolean(true))));
		List<Statement> elseStmts = Collections
				.singletonList((Statement) makeVariableAssignment(var,
						makeConstant(makeBoolean(false))));
		assertEquals("if x{x=true;}else{x=false;}",
				Printer.toString(makeConditional(cond, thenStmts, elseStmts)));
	}

	@Test
	public void printStatements() {
		List<Statement> stmts = new ArrayList<Statement>();
		stmts.add(makeThrowawayExpression(makeConstant(makeBoolean(true))));
		stmts.add(makeThrowawayExpression(makeConstant(makeBoolean(false))));
		assertEquals("true;false;", Printer.toString(stmts));
	}

}
