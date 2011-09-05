package se.raek.ahsa.ast;

import se.raek.ahsa.runtime.Value;

public interface Expression {
	
	<T> T matchExpression(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseConstant(Value v);
		T caseValueLookup(ValueLocation val);
		T caseVariableLookup(VariableLocation var);
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
	
	public static final class ValueLookup implements Expression {
		
		public final ValueLocation val;
		
		private ValueLookup(ValueLocation val) {
			if (val == null) throw new NullPointerException();
			this.val = val;
		}
		
		public static ValueLookup make(ValueLocation val) {
			return new ValueLookup(val);
		}
		
		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseValueLookup(val);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			 if (this == otherObject) return true;
			 if (!(otherObject instanceof ValueLookup)) return false;
			 ValueLookup otherLookup = (ValueLookup) otherObject;
			 return val.equals(otherLookup.val);
		}
		
		@Override
		public int hashCode() {
			return val.hashCode();
		}
		
	}
	
	public static final class VariableLookup implements Expression {
		
		public final VariableLocation var;
		
		private VariableLookup(VariableLocation var) {
			if (var == null) throw new NullPointerException();
			this.var = var;
		}
		
		public static VariableLookup make(VariableLocation var) {
			return new VariableLookup(var);
		}
		
		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseVariableLookup(var);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			 if (this == otherObject) return true;
			 if (!(otherObject instanceof VariableLookup)) return false;
			 VariableLookup otherLookup = (VariableLookup) otherObject;
			 return var.equals(otherLookup.var);
		}
		
		@Override
		public int hashCode() {
			return var.hashCode();
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
