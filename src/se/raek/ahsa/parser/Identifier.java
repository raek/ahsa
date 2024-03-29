package se.raek.ahsa.parser;

import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.ast.LoopLabel;

public abstract class Identifier {

	private Identifier() {
	}

	public abstract <T> T matchIdentifier(Matcher<T> m);

	public interface Matcher<T> {
		T caseUnbound();
		T caseValue(ValueLocation val);
		T caseVariable(VariableLocation var);
		T caseInaccessibleVariable(VariableLocation var);
		T caseLoop(LoopLabel loop);
		T caseInaccessibleLoop(LoopLabel loop);
	}

	public static abstract class AbstractMatcher<T> implements Matcher<T> {

		public abstract T otherwise();

		public T caseUnbound() {
			return otherwise();
		}

		public T caseValue(ValueLocation val) {
			return otherwise();
		}

		public T caseVariable(VariableLocation var) {
			return otherwise();
		}

		public T caseInaccessibleVariable(VariableLocation var) {
			return otherwise();
		}

		public T caseLoop(LoopLabel loop) {
			return otherwise();
		}

		public T caseInaccessibleLoop(LoopLabel loop) {
			return otherwise();
		}

	}

	private static final Unbound singletonUnbound = new Unbound();

	public static Identifier makeUnbound() {
		return singletonUnbound;
	}

	public static Identifier makeValue(ValueLocation val) {
		return new Value(val);
	}

	public static Identifier makeVariable(VariableLocation var) {
		return new Variable(var);
	}

	public static Identifier makeInaccessibleVariable(VariableLocation var) {
		return new InaccessibleVariable(var);
	}

	public static Identifier makeLoop(LoopLabel loop) {
		return new Loop(loop);
	}

	public static Identifier makeInaccessibleLoop(LoopLabel loop) {
		return new InaccessibleLoop(loop);
	}

	private static final class Unbound extends Identifier {

		public Unbound() {
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseUnbound();
		}

		// Using identity-based .equals for the interning Unbound class

		// Using identity-based .hashCode for the interning Unbound class

		@Override
		public String toString() {
			return "Unbound()";
		}

	}

	private static final class Value extends Identifier {

		private final ValueLocation val;

		public Value(ValueLocation val) {
			if (val == null) throw new NullPointerException();
			this.val = val;
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseValue(val);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Value)) return false;
			Value other = (Value) otherObject;
			return (val.equals(other.val));
		}

		@Override
		public int hashCode() {
			return val.hashCode();
		}

		@Override
		public String toString() {
			return "Value(" + val + ")";
		}

	}

	private static final class Variable extends Identifier {

		private final VariableLocation var;

		public Variable(VariableLocation var) {
			if (var == null) throw new NullPointerException();
			this.var = var;
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseVariable(var);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Variable)) return false;
			Variable other = (Variable) otherObject;
			return (var.equals(other.var));
		}

		@Override
		public int hashCode() {
			return var.hashCode();
		}

		@Override
		public String toString() {
			return "Variable(" + var + ")";
		}

	}

	private static final class InaccessibleVariable extends Identifier {

		private final VariableLocation var;

		public InaccessibleVariable(VariableLocation var) {
			if (var == null) throw new NullPointerException();
			this.var = var;
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseInaccessibleVariable(var);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof InaccessibleVariable)) return false;
			InaccessibleVariable other = (InaccessibleVariable) otherObject;
			return (var.equals(other.var));
		}

		@Override
		public int hashCode() {
			return var.hashCode();
		}

		@Override
		public String toString() {
			return "InaccessibleVariable(" + var + ")";
		}

	}

	private static final class Loop extends Identifier {

		private final LoopLabel loop;

		public Loop(LoopLabel loop) {
			if (loop == null) throw new NullPointerException();
			this.loop = loop;
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseLoop(loop);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Loop)) return false;
			Loop other = (Loop) otherObject;
			return (loop.equals(other.loop));
		}

		@Override
		public int hashCode() {
			return loop.hashCode();
		}

		@Override
		public String toString() {
			return "Loop(" + loop + ")";
		}

	}

	private static final class InaccessibleLoop extends Identifier {

		private final LoopLabel loop;

		public InaccessibleLoop(LoopLabel loop) {
			if (loop == null) throw new NullPointerException();
			this.loop = loop;
		}

		@Override
		public <T> T matchIdentifier(Matcher<T> m) {
			return m.caseInaccessibleLoop(loop);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof InaccessibleLoop)) return false;
			InaccessibleLoop other = (InaccessibleLoop) otherObject;
			return (loop.equals(other.loop));
		}

		@Override
		public int hashCode() {
			return loop.hashCode();
		}

		@Override
		public String toString() {
			return "InaccessibleLoop(" + loop + ")";
		}

	}

}
