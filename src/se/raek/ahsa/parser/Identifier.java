package se.raek.ahsa.parser;

import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;

public interface Identifier {
	
	<T> T matchIdentifier(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseUnbound();
		T caseValue(ValueLocation val);
		T caseVariable(VariableLocation var);
		T caseInaccessibleVariable(VariableLocation var);
	}
	
	public static class Unbound implements Identifier {
		
		private static final Unbound instance = new Unbound();
		
		private Unbound() {
		}
		
		public static Unbound make() {
			return instance;
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseUnbound();
		}
		
		@Override
		public String toString() {
			return "Unbound()"; 
		}
	}
	
	public static class Value implements Identifier {
		
		public final ValueLocation val;
		
		private Value(ValueLocation val) {
			if (val == null) throw new NullPointerException();
			this.val = val;
		}
		
		public static Value make(ValueLocation val) {
			return new Value(val);
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseValue(val);
		}
		
		@Override
		public String toString() {
			return "Value(" + val + ")"; 
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Value)) return false;
			Value otherValue = (Value) otherObject;
			return val.equals(otherValue.val);
		}
		
		@Override
		public int hashCode() {
			return val.hashCode();
		}
	}
	
	public static class Variable implements Identifier {
		
		public final VariableLocation var;
		
		private Variable(VariableLocation val) {
			if (val == null) throw new NullPointerException();
			this.var = val;
		}
		
		public static Variable make(VariableLocation val) {
			return new Variable(val);
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseVariable(var);
		}
		
		@Override
		public String toString() {
			return "Variable(" + var + ")"; 
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Variable)) return false;
			Variable otherVariable = (Variable) otherObject;
			return var.equals(otherVariable.var);
		}
		
		@Override
		public int hashCode() {
			return var.hashCode();
		}
	}
	
	public static class InaccessibleVariable implements Identifier {
		
		public final VariableLocation var;
		
		private InaccessibleVariable(VariableLocation val) {
			if (val == null) throw new NullPointerException();
			this.var = val;
		}
		
		public static InaccessibleVariable make(VariableLocation val) {
			return new InaccessibleVariable(val);
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseInaccessibleVariable(var);
		}
		
		@Override
		public String toString() {
			return "InaccessibleVariable(" + var + ")"; 
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof InaccessibleVariable)) return false;
			InaccessibleVariable otherVariable = (InaccessibleVariable) otherObject;
			return var.equals(otherVariable.var);
		}
		
		@Override
		public int hashCode() {
			return var.hashCode();
		}
	}
	
	public static abstract class AbstractMatcher<T> implements Matcher<T> {
		
		public abstract T otherwise();

		@Override
		public T caseUnbound() {
			return otherwise();
		}

		@Override
		public T caseValue(ValueLocation val) {
			return otherwise();
		}

		@Override
		public T caseVariable(VariableLocation var) {
			return otherwise();
		}

		@Override
		public T caseInaccessibleVariable(VariableLocation var) {
			return otherwise();
		}
		
	}

}
