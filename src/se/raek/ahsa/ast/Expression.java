package se.raek.ahsa.ast;

import java.util.List;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.ast.EqualityOperator;
import se.raek.ahsa.ast.RelationalOperator;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.runtime.Value;

public abstract class Expression {

	private Expression() {
	}

	public abstract <T> T matchExpression(Matcher<T> m);

	public interface Matcher<T> {
		T caseConstant(Value v);
		T caseValueLookup(ValueLocation val);
		T caseVariableLookup(VariableLocation val);
		T caseArithmeticOperation(ArithmeticOperator op, Expression left, Expression right);
		T caseEqualityOperation(EqualityOperator op, Expression left, Expression right);
		T caseRelationalOperation(RelationalOperator op, Expression left, Expression right);
		T caseFunctionAbstraction(ValueLocation self, List<ValueLocation> parameters, List<Statement> body);
		T caseFunctionApplication(Expression function, List<Expression> parameters);
	}

	public static abstract class AbstractMatcher<T> implements Matcher<T> {

		public abstract T otherwise();

		public T caseConstant(Value v) {
			return otherwise();
		}

		public T caseValueLookup(ValueLocation val) {
			return otherwise();
		}

		public T caseVariableLookup(VariableLocation val) {
			return otherwise();
		}

		public T caseArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
			return otherwise();
		}

		public T caseEqualityOperation(EqualityOperator op, Expression left, Expression right) {
			return otherwise();
		}

		public T caseRelationalOperation(RelationalOperator op, Expression left, Expression right) {
			return otherwise();
		}

		public T caseFunctionAbstraction(ValueLocation self, List<ValueLocation> parameters, List<Statement> body) {
			return otherwise();
		}

		public T caseFunctionApplication(Expression function, List<Expression> parameters) {
			return otherwise();
		}

	}

	public static Expression makeConstant(Value v) {
		return new Constant(v);
	}

	public static Expression makeValueLookup(ValueLocation val) {
		return new ValueLookup(val);
	}

	public static Expression makeVariableLookup(VariableLocation val) {
		return new VariableLookup(val);
	}

	public static Expression makeArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
		return new ArithmeticOperation(op, left, right);
	}

	public static Expression makeEqualityOperation(EqualityOperator op, Expression left, Expression right) {
		return new EqualityOperation(op, left, right);
	}

	public static Expression makeRelationalOperation(RelationalOperator op, Expression left, Expression right) {
		return new RelationalOperation(op, left, right);
	}

	public static Expression makeFunctionAbstraction(ValueLocation self, List<ValueLocation> parameters, List<Statement> body) {
		return new FunctionAbstraction(self, parameters, body);
	}

	public static Expression makeFunctionApplication(Expression function, List<Expression> parameters) {
		return new FunctionApplication(function, parameters);
	}

	private static final class Constant extends Expression {

		private final Value v;

		public Constant(Value v) {
			if (v == null) throw new NullPointerException();
			this.v = v;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseConstant(v);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Constant)) return false;
			Constant other = (Constant) otherObject;
			return (v.equals(other.v));
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

	private static final class ValueLookup extends Expression {

		private final ValueLocation val;

		public ValueLookup(ValueLocation val) {
			if (val == null) throw new NullPointerException();
			this.val = val;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseValueLookup(val);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof ValueLookup)) return false;
			ValueLookup other = (ValueLookup) otherObject;
			return (val.equals(other.val));
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

	private static final class VariableLookup extends Expression {

		private final VariableLocation val;

		public VariableLookup(VariableLocation val) {
			if (val == null) throw new NullPointerException();
			this.val = val;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseVariableLookup(val);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof VariableLookup)) return false;
			VariableLookup other = (VariableLookup) otherObject;
			return (val.equals(other.val));
		}

		@Override
		public int hashCode() {
			return val.hashCode();
		}

		@Override
		public String toString() {
			return "VariableLookup(" + val + ")";
		}

	}

	private static final class ArithmeticOperation extends Expression {

		private final ArithmeticOperator op;
		private final Expression left;
		private final Expression right;

		public ArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
			if (op == null || left == null || right == null) throw new NullPointerException();
			this.op = op;
			this.left = left;
			this.right = right;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseArithmeticOperation(op, left, right);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof ArithmeticOperation)) return false;
			ArithmeticOperation other = (ArithmeticOperation) otherObject;
			return (op.equals(other.op)) && (left.equals(other.left)) && (right.equals(other.right));
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

	private static final class EqualityOperation extends Expression {

		private final EqualityOperator op;
		private final Expression left;
		private final Expression right;

		public EqualityOperation(EqualityOperator op, Expression left, Expression right) {
			if (op == null || left == null || right == null) throw new NullPointerException();
			this.op = op;
			this.left = left;
			this.right = right;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseEqualityOperation(op, left, right);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof EqualityOperation)) return false;
			EqualityOperation other = (EqualityOperation) otherObject;
			return (op.equals(other.op)) && (left.equals(other.left)) && (right.equals(other.right));
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

	private static final class RelationalOperation extends Expression {

		private final RelationalOperator op;
		private final Expression left;
		private final Expression right;

		public RelationalOperation(RelationalOperator op, Expression left, Expression right) {
			if (op == null || left == null || right == null) throw new NullPointerException();
			this.op = op;
			this.left = left;
			this.right = right;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseRelationalOperation(op, left, right);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof RelationalOperation)) return false;
			RelationalOperation other = (RelationalOperation) otherObject;
			return (op.equals(other.op)) && (left.equals(other.left)) && (right.equals(other.right));
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

	private static final class FunctionAbstraction extends Expression {

		private final ValueLocation self;
		private final List<ValueLocation> parameters;
		private final List<Statement> body;

		public FunctionAbstraction(ValueLocation self, List<ValueLocation> parameters, List<Statement> body) {
			if (parameters == null || body == null) throw new NullPointerException();
			this.self = self;
			this.parameters = parameters;
			this.body = body;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseFunctionAbstraction(self, parameters, body);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof FunctionAbstraction)) return false;
			FunctionAbstraction other = (FunctionAbstraction) otherObject;
			return (self == null ? other.self == null : self.equals(other.self)) && (parameters.equals(other.parameters)) && (body.equals(other.body));
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + (self == null ? 0 : self.hashCode());
			result = 31 * result + parameters.hashCode();
			result = 31 * result + body.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "FunctionAbstraction(" + self + ", " + parameters + ", " + body + ")";
		}

	}

	private static final class FunctionApplication extends Expression {

		private final Expression function;
		private final List<Expression> parameters;

		public FunctionApplication(Expression function, List<Expression> parameters) {
			if (function == null || parameters == null) throw new NullPointerException();
			this.function = function;
			this.parameters = parameters;
		}

		@Override
		public <T> T matchExpression(Matcher<T> m) {
			return m.caseFunctionApplication(function, parameters);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof FunctionApplication)) return false;
			FunctionApplication other = (FunctionApplication) otherObject;
			return (function.equals(other.function)) && (parameters.equals(other.parameters));
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

}
