package se.raek.ahsa.ast;

import se.raek.ahsa.runtime.Value;

public interface Expression {
	
	<T> T matchExpression(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseConstant(Value v);
		T caseBinaryOperation(BinaryOperator op, Expression left, Expression right);
	}
	
	public static final class Constant implements Expression {
		
		public final Value v;
		
		private Constant(Value v) {
			this.v = v;
		}
		
		public static Constant make(Value v) {
			return new Constant(v);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseConstant(v);
		}

	}
	
	public static final class BinaryOperation implements Expression {
		
		public final BinaryOperator op;
		public final Expression left;
		public final Expression right;
		
		private BinaryOperation(BinaryOperator op, Expression left, Expression right) {
			this.op = op;
			this.left = left;
			this.right = right;
		}
		
		public static BinaryOperation make(BinaryOperator op, Expression left, Expression right) {
			return new BinaryOperation(op, left, right);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseBinaryOperation(op, left, right);
		}
		
	}
	
}
