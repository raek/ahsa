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
			if (v == null) throw new NullPointerException();
			this.v = v;
		}
		
		public static Constant make(Value v) {
			return new Constant(v);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseConstant(v);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Constant)) return false;
			Constant otherConstant = (Constant) otherObject;
			return v.equals(otherConstant.v);
		}
		
		@Override
		public int hashCode() {
			return v.hashCode();
		}

	}
	
	public static final class BinaryOperation implements Expression {
		
		public final BinaryOperator op;
		public final Expression left;
		public final Expression right;
		
		private BinaryOperation(BinaryOperator op, Expression left, Expression right) {
			if (op == null || left == null || right == null) throw new NullPointerException();
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
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof BinaryOperation)) return false;
			BinaryOperation otherBinOp = (BinaryOperation) otherObject;
			return op.equals(otherBinOp.op) &&
					left.equals(otherBinOp.left) &&
					right.equals(otherBinOp.right);
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + op.hashCode();
			result = 31 * result + left.hashCode();
			result = 31 * result + right.hashCode();
			return result;
		}
		
	}
	
}
