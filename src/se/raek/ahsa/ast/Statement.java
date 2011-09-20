package se.raek.ahsa.ast;

import java.util.List;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;

public abstract class Statement {

	private Statement() {
	}

	public abstract <T> T matchStatement(Matcher<T> m);

	public interface Matcher<T> {
		T caseThrowawayExpression(Expression expr);
		T caseValueDefinition(ValueLocation val, Expression expr);
		T caseVariableAssignment(VariableLocation var, Expression expr);
		T caseConditional(Expression cond, List<Statement> thenStmts, List<Statement> elseStmts);
		T caseReturn(Expression expr);
	}

	public static abstract class AbstractMatcher<T> implements Matcher<T> {

		public abstract T otherwise();

		public T caseThrowawayExpression(Expression expr) {
			return otherwise();
		}

		public T caseValueDefinition(ValueLocation val, Expression expr) {
			return otherwise();
		}

		public T caseVariableAssignment(VariableLocation var, Expression expr) {
			return otherwise();
		}

		public T caseConditional(Expression cond, List<Statement> thenStmts, List<Statement> elseStmts) {
			return otherwise();
		}

		public T caseReturn(Expression expr) {
			return otherwise();
		}

	}

	public static Statement makeThrowawayExpression(Expression expr) {
		return new ThrowawayExpression(expr);
	}

	public static Statement makeValueDefinition(ValueLocation val, Expression expr) {
		return new ValueDefinition(val, expr);
	}

	public static Statement makeVariableAssignment(VariableLocation var, Expression expr) {
		return new VariableAssignment(var, expr);
	}

	public static Statement makeConditional(Expression cond, List<Statement> thenStmts, List<Statement> elseStmts) {
		return new Conditional(cond, thenStmts, elseStmts);
	}

	public static Statement makeReturn(Expression expr) {
		return new Return(expr);
	}

	private static final class ThrowawayExpression extends Statement {

		private final Expression expr;

		public ThrowawayExpression(Expression expr) {
			if (expr == null) throw new NullPointerException();
			this.expr = expr;
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseThrowawayExpression(expr);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof ThrowawayExpression)) return false;
			ThrowawayExpression other = (ThrowawayExpression) otherObject;
			return (expr.equals(other.expr));
		}

		@Override
		public int hashCode() {
			return expr.hashCode();
		}

		@Override
		public String toString() {
			return "ThrowawayExpression(" + expr + ")";
		}

	}

	private static final class ValueDefinition extends Statement {

		private final ValueLocation val;
		private final Expression expr;

		public ValueDefinition(ValueLocation val, Expression expr) {
			if (val == null || expr == null) throw new NullPointerException();
			this.val = val;
			this.expr = expr;
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseValueDefinition(val, expr);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof ValueDefinition)) return false;
			ValueDefinition other = (ValueDefinition) otherObject;
			return (val.equals(other.val)) && (expr.equals(other.expr));
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + val.hashCode();
			result = 31 * result + expr.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "ValueDefinition(" + val + ", " + expr + ")";
		}

	}

	private static final class VariableAssignment extends Statement {

		private final VariableLocation var;
		private final Expression expr;

		public VariableAssignment(VariableLocation var, Expression expr) {
			if (var == null || expr == null) throw new NullPointerException();
			this.var = var;
			this.expr = expr;
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseVariableAssignment(var, expr);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof VariableAssignment)) return false;
			VariableAssignment other = (VariableAssignment) otherObject;
			return (var.equals(other.var)) && (expr.equals(other.expr));
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + var.hashCode();
			result = 31 * result + expr.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "VariableAssignment(" + var + ", " + expr + ")";
		}

	}

	private static final class Conditional extends Statement {

		private final Expression cond;
		private final List<Statement> thenStmts;
		private final List<Statement> elseStmts;

		public Conditional(Expression cond, List<Statement> thenStmts, List<Statement> elseStmts) {
			if (cond == null || thenStmts == null || elseStmts == null) throw new NullPointerException();
			this.cond = cond;
			this.thenStmts = thenStmts;
			this.elseStmts = elseStmts;
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseConditional(cond, thenStmts, elseStmts);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Conditional)) return false;
			Conditional other = (Conditional) otherObject;
			return (cond.equals(other.cond)) && (thenStmts.equals(other.thenStmts)) && (elseStmts.equals(other.elseStmts));
		}

		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + cond.hashCode();
			result = 31 * result + thenStmts.hashCode();
			result = 31 * result + elseStmts.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "Conditional(" + cond + ", " + thenStmts + ", " + elseStmts + ")";
		}

	}

	private static final class Return extends Statement {

		private final Expression expr;

		public Return(Expression expr) {
			if (expr == null) throw new NullPointerException();
			this.expr = expr;
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseReturn(expr);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Return)) return false;
			Return other = (Return) otherObject;
			return (expr.equals(other.expr));
		}

		@Override
		public int hashCode() {
			return expr.hashCode();
		}

		@Override
		public String toString() {
			return "Return(" + expr + ")";
		}

	}

}
