package se.raek.ahsa;

import java.util.List;

import se.raek.ahsa.ast.EqualityOperator;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.ast.RelationalOperator;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.runtime.Store;
import se.raek.ahsa.runtime.Value;

public class Interpreter implements Expression.Matcher<Value>, Statement.Matcher<Void> {
	
	private final Store sto;
	
	private Interpreter(Store sto) {
		this.sto = sto;
	}
	
	public static Value eval(Expression expr, Store sto) {
		return expr.matchExpression(new Interpreter(sto));
	}
	
	public static void execute(Statement stmt, Store sto) {
		stmt.matchStatement(new Interpreter(sto));
	}
	
	public static void execute(List<Statement> stmts, Store sto) {
		Interpreter interp = new Interpreter(sto);
		for (Statement stmt : stmts) {
			stmt.matchStatement(interp);
		}
	}
	
	public static class CastException extends RuntimeException {
		
		private static final long serialVersionUID = -3113471139719820971L;

		public CastException(String expected, String actual) {
			super(String.format("expected %s, got %s", expected, actual));
		}
		
	}
	
	public static String typeName(final Value v) {
		return v.matchValue(new Value.Matcher<String>() {
			@Override
			public String caseNull() {
				return "null";
			}
			@Override
			public String caseBoolean(boolean b) {
				return "boolean";
			}
			@Override
			public String caseNumber(double n) {
				return "number";
			}
		});
	}
	
	public static double castToNumber(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Double>() {
			@Override
			public Double caseNumber(double n) {
				return n;
			}
			@Override
			public Double otherwise() {
				throw new CastException("number", typeName(v));
			}
		});
	}

	@Override
	public Value caseConstant(Value v) {
		return v;
	}

	@Override
	public Value caseValueLookup(ValueLocation val) {
		return sto.lookupValue(val);
	}

	@Override
	public Value caseVariableLookup(VariableLocation var) {
		return sto.lookupVariable(var);
	}

	@Override
	public Value caseArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
		Value leftValue = left.matchExpression(this);
		Value rightValue = right.matchExpression(this);
		final double leftNumber = castToNumber(leftValue);
		final double rightNumber = castToNumber(rightValue);
		return Value.Number.make(op.matchArithmeticOperator(new ArithmeticOperator.Matcher<Double>() {
			@Override
			public Double caseAddition() {
				return leftNumber + rightNumber;
			}
			@Override
			public Double caseSubtraction() {
				return leftNumber - rightNumber;
			}
			@Override
			public Double caseMultiplication() {
				return leftNumber * rightNumber;
			}
			@Override
			public Double caseDivision() {
				return leftNumber / rightNumber;
			}
		}));
	}

	@Override
	public Value caseEqualityOperation(EqualityOperator op, Expression left, Expression right) {
		final Value leftValue = left.matchExpression(this);
		final Value rightValue = right.matchExpression(this);
		return Value.Boolean.make(op.matchEqualityOperator(new EqualityOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseEqual() {
				return leftValue.equals(rightValue);
			}
			@Override
			public Boolean caseUnequal() {
				return !leftValue.equals(rightValue);
			}
		}));
	}

	@Override
	public Value caseRelationalOperation(RelationalOperator op,
			Expression left, Expression right) {
		Value leftValue = left.matchExpression(this);
		Value rightValue = right.matchExpression(this);
		final double leftNumber = castToNumber(leftValue);
		final double rightNumber = castToNumber(rightValue);
		return Value.Boolean.make(op.matchRelationalOperator(new RelationalOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseGreater() {
				return leftNumber > rightNumber;
			}
			@Override
			public Boolean caseLess() {
				return leftNumber < rightNumber;
			}
			@Override
			public Boolean caseGreaterEqual() {
				return leftNumber >= rightNumber;
			}
			@Override
			public Boolean caseLessEqual() {
				return leftNumber <= rightNumber;
			}
		}));
	}

	@Override
	public Void caseThrowawayExpression(Expression expr) {
		expr.matchExpression(this); // Expression value is thrown away
		return null;
	}

	@Override
	public Void caseValueDefinition(ValueLocation val, Expression expr) {
		Value result = expr.matchExpression(this);
		sto.defineValue(val, result);
		return null;
	}

	@Override
	public Void caseVariableAssignment(VariableLocation var, Expression expr) {
		Value result = expr.matchExpression(this);
		sto.assignVariable(var, result);
		return null;
	}
}
