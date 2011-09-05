package se.raek.ahsa;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.BinaryOperator;
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
	
	public static class CastException extends RuntimeException {
		
		private static final long serialVersionUID = -3113471139719820971L;

		public CastException(String who, String expected, String actual) {
			super(String.format("%s: expected %s argument, got %s", who, expected, actual));
		}
		
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
	public Value caseBinaryOperation(BinaryOperator op, Expression left, Expression right) {
		Value leftValue = left.matchExpression(this);
		Value rightValue = right.matchExpression(this);
		
		Value.Matcher<Double> caster = new Value.AbstractMatcher<Double>() {
			@Override
			public Double caseNumber(double n) {
				return n;
			}
			@Override
			public Double otherwise() {
				throw new CastException("binary operation", "number", "something else");
			}
		};
		
		final double leftNumber = leftValue.matchValue(caster);
		final double rightNumber = rightValue.matchValue(caster);
		
		double result = op.matchBinaryOperator(new BinaryOperator.Matcher<Double>() {
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
		});
		
		return Value.Number.make(result);
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
