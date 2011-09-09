package se.raek.ahsa.ast;

import java.util.List;

public interface Statement {
	
	<T> T matchStatement(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseThrowawayExpression(Expression expr);
		T caseValueDefinition(ValueLocation val, Expression expr);
		T caseVariableAssignment(VariableLocation var, Expression expr);
		T caseConditional(Expression cond, List<Statement> thenStmts, List<Statement> elseStmts);
	}
	
	public static final class ThrowawayExpression implements Statement {
		
		private final Expression expr;
		
		private ThrowawayExpression(Expression expr) {
			if (expr == null) throw new NullPointerException();
			this.expr = expr;
		}
		
		public static ThrowawayExpression make(Expression expr) {
			return new ThrowawayExpression(expr);
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseThrowawayExpression(expr);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof ThrowawayExpression)) return false;
			ThrowawayExpression otherThrowawayExpression = (ThrowawayExpression) otherObject;
			return expr.equals(otherThrowawayExpression.expr);
		}
		
		@Override
		public int hashCode() {
			return expr.hashCode();
		}
		
	}
	
	public static final class ValueDefinition implements Statement {
		
		private final ValueLocation val;
		private final Expression expr;
		
		private ValueDefinition(ValueLocation val, Expression expr) {
			if (val == null || expr == null) throw new NullPointerException();
			this.val = val;
			this.expr = expr;
		}
		
		public static ValueDefinition make(ValueLocation val, Expression expr) {
			return new ValueDefinition(val, expr);
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseValueDefinition(val, expr);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof ValueDefinition)) return false;
			ValueDefinition otherValueDefinition = (ValueDefinition) otherObject;
			return val.equals(otherValueDefinition.val) &&
					expr.equals(otherValueDefinition.expr);
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + val.hashCode();
			result = 31 * result + expr.hashCode();
			return result;
		}
		
	}
	
	public static final class VariableAssignment implements Statement {
		
		private final VariableLocation var;
		private final Expression expr;
		
		private VariableAssignment(VariableLocation var, Expression expr) {
			if (var == null || expr == null) throw new NullPointerException();
			this.var = var;
			this.expr = expr;
		}
		
		public static VariableAssignment make(VariableLocation var, Expression expr) {
			return new VariableAssignment(var, expr);
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseVariableAssignment(var, expr);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof VariableAssignment)) return false;
			VariableAssignment otherVariableAssignment = (VariableAssignment) otherObject;
			return var.equals(otherVariableAssignment.var) &&
					expr.equals(otherVariableAssignment.expr);
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + var.hashCode();
			result = 31 * result + expr.hashCode();
			return result;
		}
		
	}
	
	public static final class Conditional implements Statement {
		
		private final Expression cond;
		private final List<Statement> thenStmts;
		private final List<Statement> elseStmts;
		
		private Conditional(Expression cond, List<Statement> thenStmts, List<Statement> elseStmts) {
			if (cond == null || thenStmts == null || elseStmts == null) throw new NullPointerException();
			this.cond = cond;
			this.thenStmts = thenStmts;
			this.elseStmts = elseStmts;
		}
		
		public static Conditional make(Expression cond, List<Statement> thenStmts, List<Statement> elseStmts) {
			return new Conditional(cond, thenStmts, elseStmts);
		}

		@Override
		public <T> T matchStatement(Matcher<T> m) {
			return m.caseConditional(cond, thenStmts, elseStmts);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Conditional)) return false;
			Conditional otherConditional = (Conditional) otherObject;
			return cond.equals(otherConditional.cond) &&
					thenStmts.equals(otherConditional.thenStmts) &&
					elseStmts.equals(otherConditional.elseStmts);
		}
		
		@Override
		public int hashCode() {
			int result = 17;
			result = 31 * result + cond.hashCode();
			result = 31 * result + thenStmts.hashCode();
			result = 31 * result + elseStmts.hashCode();
			return result;
		}
		
	}

}
