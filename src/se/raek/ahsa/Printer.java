package se.raek.ahsa;

import java.io.PrintWriter;
import java.io.StringWriter;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.BinaryOperator;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.runtime.Value;

public class Printer implements Value.Matcher<Void>, Expression.Matcher<Void>, BinaryOperator.Matcher<Void>, Statement.Matcher<Void> {
	
	private final PrintWriter writer;
	
	private Printer(PrintWriter writer) {
		this.writer = writer;
	}
	
	public static void print(Value expr, PrintWriter writer) {
		Printer printer = new Printer(writer);
		expr.matchValue(printer);
	}
	
	public static void print(Expression expr, PrintWriter writer) {
		Printer printer = new Printer(writer);
		expr.matchExpression(printer);
	}
	
	public static void print(Statement stmt, PrintWriter writer) {
		Printer printer = new Printer(writer);
		stmt.matchStatement(printer);
	}
	
	public static String toString(Value v) {
		StringWriter sw = new StringWriter();
		print(v, new PrintWriter(sw));
		return sw.toString();
	}
	
	public static String toString(Expression expr) {
		StringWriter sw = new StringWriter();
		print(expr, new PrintWriter(sw));
		return sw.toString();
	}
	
	public static String toString(Statement stmt) {
		StringWriter sw = new StringWriter();
		print(stmt, new PrintWriter(sw));
		return sw.toString();
	}

	@Override
	public Void caseNull() {
		writer.print("null");
		return null;
	}

	@Override
	public Void caseBoolean(boolean b) {
		writer.print(b ? "true" : "false");
		return null;
	}

	@Override
	public Void caseNumber(double n) {
		writer.print(n);
		return null;
	}

	@Override
	public Void caseConstant(Value v) {
		v.matchValue(this);
		return null;
	}

	@Override
	public Void caseValueLookup(ValueLocation val) {
		writer.print(val.label);
		return null;
	}

	@Override
	public Void caseVariableLookup(VariableLocation var) {
		writer.print(var.label);
		return null;
	}

	@Override
	public Void caseBinaryOperation(BinaryOperator op, Expression left, Expression right) {
		writer.print("(");
		left.matchExpression(this);
		op.matchBinaryOperator(this);
		right.matchExpression(this);
		writer.print(")");
		return null;
	}

	@Override
	public Void caseAddition() {
		writer.print("+");
		return null;
	}

	@Override
	public Void caseSubtraction() {
		writer.print("-");
		return null;
	}

	@Override
	public Void caseMultiplication() {
		writer.print("*");
		return null;
	}

	@Override
	public Void caseDivision() {
		writer.print("/");
		return null;
	}

	@Override
	public Void caseThrowawayExpression(Expression expr) {
		expr.matchExpression(this);
		writer.print(";");
		return null;
	}

	@Override
	public Void caseValueDefinition(ValueLocation val, Expression expr) {
		writer.printf("val %s=", val.label);
		expr.matchExpression(this);
		writer.print(";");
		return null;
	}

	@Override
	public Void caseVariableAssignment(VariableLocation var, Expression expr) {
		writer.printf("%s=", var.label);
		expr.matchExpression(this);
		writer.print(";");
		return null;
	}

}
