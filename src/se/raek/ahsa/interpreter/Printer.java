package se.raek.ahsa.interpreter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.ast.EqualityOperator;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.Literal;
import se.raek.ahsa.ast.LoopLabel;
import se.raek.ahsa.ast.RelationalOperator;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;

public class Printer implements Literal.Matcher<Void>, Value.Matcher<Void>, Expression.Matcher<Void>, Statement.Matcher<Void> {
	
	private final PrintWriter writer;
	
	private Printer(PrintWriter writer) {
		this.writer = writer;
	}
	
	public static void print(Literal l, PrintWriter writer) {
		Printer printer = new Printer(writer);
		l.matchLiteral(printer);
	}
	
	public static void print(Value v, PrintWriter writer) {
		Printer printer = new Printer(writer);
		v.matchValue(printer);
	}
	
	public static void print(Expression expr, PrintWriter writer) {
		Printer printer = new Printer(writer);
		expr.matchExpression(printer);
	}
	
	public static void print(Statement stmt, PrintWriter writer) {
		Printer printer = new Printer(writer);
		stmt.matchStatement(printer);
	}
	
	public static void print(List<Statement> stmts, PrintWriter writer) {
		Printer printer = new Printer(writer);
		for (Statement stmt : stmts) {
			stmt.matchStatement(printer);
		}
	}
	
	public static String toString(Literal l) {
		StringWriter sw = new StringWriter();
		print(l, new PrintWriter(sw));
		return sw.toString();
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
	
	public static String toString(List<Statement> stmts) {
		StringWriter sw = new StringWriter();
		print(stmts, new PrintWriter(sw));
		return sw.toString();
	}

	public Void caseNull() {
		writer.print("null");
		return null;
	}

	public Void caseBoolean(boolean b) {
		writer.print(b ? "true" : "false");
		return null;
	}

	public Void caseNumber(double n) {
		writer.print(n);
		return null;
	}

	public Void caseFunction(Function fn) {
		writer.print("#<fn>");
		return null;
	}

	public Void caseId(Id id) {
		writer.printf("#<id%d>", id.id);
		return null;
	}

	public Void caseBox(Box box) {
		writer.printf("#<box%d>", box.id);
		return null;
	}

	public Void caseArray(Array array) {
		writer.printf("#<array%d>", array.id);
		return null;
	}

	public Void caseConstant(Literal l) {
		l.matchLiteral(this);
		return null;
	}

	public Void caseValueLookup(ValueLocation val) {
		writer.print(val.label);
		return null;
	}

	public Void caseVariableLookup(VariableLocation var) {
		writer.print(var.label);
		return null;
	}

	public Void caseArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
		writer.print("(");
		left.matchExpression(this);
		op.matchArithmeticOperator(new ArithmeticOperator.Matcher<Void>() {
			public Void caseAddition() {
				writer.print("+");
				return null;
			}
			public Void caseSubtraction() {
				writer.print("-");
				return null;
			}
			public Void caseMultiplication() {
				writer.print("*");
				return null;
			}
			public Void caseDivision() {
				writer.print("/");
				return null;
			}
		});
		right.matchExpression(this);
		writer.print(")");
		return null;
	}

	public Void caseEqualityOperation(EqualityOperator op, Expression left,
			Expression right) {
		writer.print("(");
		left.matchExpression(this);
		op.matchEqualityOperator(new EqualityOperator.Matcher<Void>() {
			public Void caseEqual() {
				writer.print("==");
				return null;
			}
			public Void caseUnequal() {
				writer.print("!=");
				return null;
			}
		});
		right.matchExpression(this);
		writer.print(")");
		return null;
	}

	public Void caseRelationalOperation(RelationalOperator op, Expression left,
			Expression right) {
		writer.print("(");
		left.matchExpression(this);
		op.matchRelationalOperator(new RelationalOperator.Matcher<Void>() {
			public Void caseGreater() {
				writer.print(">");
				return null;
			}
			public Void caseLess() {
				writer.print("<");
				return null;
			}
			public Void caseGreaterEqual() {
				writer.print(">=");
				return null;
			}
			public Void caseLessEqual() {
				writer.print("<=");
				return null;
			}
		});
		right.matchExpression(this);
		writer.print(")");
		return null;
	}

	public Void caseFunctionApplication(Expression function, List<Expression> parameters) {
		writer.print("#<function application>");
		return null;
	}

	public Void caseFunctionAbstraction(ValueLocation self,
			List<ValueLocation> parameters, List<Statement> body) {
		writer.print("#<function abstraction>");
		return null;
	}

	public Void caseThrowawayExpression(Expression expr) {
		expr.matchExpression(this);
		writer.print(";");
		return null;
	}

	public Void caseValueDefinition(ValueLocation val, Expression expr) {
		writer.printf("val %s=", val.label);
		expr.matchExpression(this);
		writer.print(";");
		return null;
	}

	public Void caseVariableAssignment(VariableLocation var, Expression expr) {
		writer.printf("%s=", var.label);
		expr.matchExpression(this);
		writer.print(";");
		return null;
	}

	public Void caseConditional(Expression cond, List<Statement> thenStmts,
			List<Statement> elseStmts) {
		writer.print("if ");
		cond.matchExpression(this);
		writer.print("{");
		for (Statement stmt : thenStmts) {
			stmt.matchStatement(this);
		}
		writer.print("}else{");
		for (Statement stmt : elseStmts) {
			stmt.matchStatement(this);
		}
		writer.print("}");
		return null;
	}

	public Void caseLoop(LoopLabel label, List<Statement> body) {
		writer.print("#<loop>");
		return null;
	}

	public Void caseBreak(LoopLabel label) {
		writer.print("#<break>");
		return null;
	}

	public Void caseReturn(Expression expr) {
		writer.print("return ");
		expr.matchExpression(this);
		writer.print(";");
		return null;
	}

}
