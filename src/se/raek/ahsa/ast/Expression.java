package se.raek.ahsa.ast;

import java.util.List;

import se.raek.ahsa.runtime.Value;

public interface Expression {
	
	<T> T matchExpression(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseConstant(Value v);
		T caseValueLookup(ValueLocation val);
		T caseVariableLookup(VariableLocation var);
		T caseArithmeticOperation(ArithmeticOperator op, Expression left, Expression right);
		T caseEqualityOperation(EqualityOperator op, Expression left, Expression right);
		T caseRelationalOperation(RelationalOperator op, Expression left, Expression right);
		T caseFunctionApplicaion(Expression function, List<Expression> parameters);
		T caseFunctionAbstraction(List<ValueLocation> parameters, List<Statement> body);
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
		
		@Override
		public String toString() {
			return "Constant(" + v + ")";
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
		
		@Override
		public String toString() {
			return "ValueLookup(" + val + ")";
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
		
		@Override
		public String toString() {
			return "VariableLookup(" + var + ")";
		}
		
	}
	
	public static final class ArithmeticOperation implements Expression {
		
		public final ArithmeticOperator op;
		public final Expression left;
		public final Expression right;
		
		private ArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
			if (op == null || left == null || right == null) throw new NullPointerException();
			this.op = op;
			this.left = left;
			this.right = right;
		}
		
		public static ArithmeticOperation make(ArithmeticOperator op, Expression left, Expression right) {
			return new ArithmeticOperation(op, left, right);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseArithmeticOperation(op, left, right);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof ArithmeticOperation)) return false;
			ArithmeticOperation otherBinOp = (ArithmeticOperation) otherObject;
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
		
		@Override
		public String toString() {
			return "ArithmeticOperation(" + op + ", " + left + ", " + right + ")";
		}
		
	}
	
	public static final class EqualityOperation implements Expression {
		
		public final EqualityOperator op;
		public final Expression left;
		public final Expression right;
		
		private EqualityOperation(EqualityOperator op, Expression left, Expression right) {
			if (op == null || left == null || right == null) throw new NullPointerException();
			this.op = op;
			this.left = left;
			this.right = right;
		}
		
		public static EqualityOperation make(EqualityOperator op, Expression left, Expression right) {
			return new EqualityOperation(op, left, right);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseEqualityOperation(op, left, right);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof EqualityOperation)) return false;
			EqualityOperation otherBinOp = (EqualityOperation) otherObject;
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
		
		@Override
		public String toString() {
			return "EqualityOperation(" + op + ", " + left + ", " + right + ")";
		}
		
	}
	
	public static final class RelationalOperation implements Expression {
		
		public final RelationalOperator op;
		public final Expression left;
		public final Expression right;
		
		private RelationalOperation(RelationalOperator op, Expression left, Expression right) {
			if (op == null || left == null || right == null) throw new NullPointerException();
			this.op = op;
			this.left = left;
			this.right = right;
		}
		
		public static RelationalOperation make(RelationalOperator op, Expression left, Expression right) {
			return new RelationalOperation(op, left, right);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseRelationalOperation(op, left, right);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof RelationalOperation)) return false;
			RelationalOperation otherBinOp = (RelationalOperation) otherObject;
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
		
		@Override
		public String toString() {
			return "RelationalOperation(" + op + ", " + left + ", " + right + ")";
		}
		
	}
	
	public static final class FunctionApplication implements Expression {
		
		public final Expression function;
		public final List<Expression> parameters;
		
		private FunctionApplication(Expression function, List<Expression> parameters) {
			if (function == null || parameters == null) throw new NullPointerException();
			this.function = function;
			this.parameters = parameters;
		}
		
		public static FunctionApplication make(Expression function, List<Expression> parameters) {
			return new FunctionApplication(function, parameters);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseFunctionApplicaion(function, parameters);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof FunctionApplication)) return false;
			FunctionApplication otherApp = (FunctionApplication) otherObject;
			return function.equals(otherApp.function) &&
					parameters.equals(otherApp.parameters);
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + function.hashCode();
			result = 31 * result + parameters.hashCode();
			return result;
		}
		
		@Override
		public String toString() {
			return "FunctionApplication(" + function + ", " + parameters + ")";
		}
		
	}
	
	public static final class FunctionAbstraction implements Expression {
		
		public final List<ValueLocation> parameters;
		public final List<Statement> body;
		
		private FunctionAbstraction(List<ValueLocation> parameters, List<Statement> body) {
			if (parameters == null || body == null) throw new NullPointerException();
			this.parameters = parameters;
			this.body = body;
		}
		
		public static FunctionAbstraction make(List<ValueLocation> parameters, List<Statement> body) {
			return new FunctionAbstraction(parameters, body);
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseFunctionAbstraction(parameters, body);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof FunctionAbstraction)) return false;
			FunctionAbstraction otherAbs = (FunctionAbstraction) otherObject;
			return parameters.equals(otherAbs.parameters) &&
					body.equals(otherAbs.body);
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + parameters.hashCode();
			result = 31 * result + body.hashCode();
			return result;
		}
		
		@Override
		public String toString() {
			return "FunctionAbstraction(" + parameters + ", " + body + ")";
		}
		
	}
	
}
