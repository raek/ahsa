package se.raek.ahsa;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.BinaryOperator;
import se.raek.ahsa.ast.Expression.Matcher;
import se.raek.ahsa.runtime.Value;

public class Evaluator implements Matcher<Value> {
	
	private static final Evaluator instance = new Evaluator();
	
	private Evaluator() {
	}
	
	public static Value eval(Expression expr) {
		return expr.matchExpression(instance);
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
}
