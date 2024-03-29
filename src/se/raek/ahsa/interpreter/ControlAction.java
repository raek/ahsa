package se.raek.ahsa.interpreter;

import se.raek.ahsa.ast.LoopLabel;
import se.raek.ahsa.interpreter.Value;

public abstract class ControlAction {

	private ControlAction() {
	}

	public abstract <T> T matchControlAction(Matcher<T> m);

	public interface Matcher<T> {
		T caseNext();
		T caseBreak(LoopLabel loop);
		T caseReturn(Value v);
	}

	public static abstract class AbstractMatcher<T> implements Matcher<T> {

		public abstract T otherwise();

		public T caseNext() {
			return otherwise();
		}

		public T caseBreak(LoopLabel loop) {
			return otherwise();
		}

		public T caseReturn(Value v) {
			return otherwise();
		}

	}

	private static final Next singletonNext = new Next();

	public static ControlAction makeNext() {
		return singletonNext;
	}

	public static ControlAction makeBreak(LoopLabel loop) {
		return new Break(loop);
	}

	public static ControlAction makeReturn(Value v) {
		return new Return(v);
	}

	private static final class Next extends ControlAction {

		public Next() {
		}

		@Override
		public <T> T matchControlAction(Matcher<T> m) {
			return m.caseNext();
		}

		// Using identity-based .equals for the interning Next class

		// Using identity-based .hashCode for the interning Next class

		@Override
		public String toString() {
			return "Next()";
		}

	}

	private static final class Break extends ControlAction {

		private final LoopLabel loop;

		public Break(LoopLabel loop) {
			if (loop == null) throw new NullPointerException();
			this.loop = loop;
		}

		@Override
		public <T> T matchControlAction(Matcher<T> m) {
			return m.caseBreak(loop);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Break)) return false;
			Break other = (Break) otherObject;
			return (loop.equals(other.loop));
		}

		@Override
		public int hashCode() {
			return loop.hashCode();
		}

		@Override
		public String toString() {
			return "Break(" + loop + ")";
		}

	}

	private static final class Return extends ControlAction {

		private final Value v;

		public Return(Value v) {
			if (v == null) throw new NullPointerException();
			this.v = v;
		}

		@Override
		public <T> T matchControlAction(Matcher<T> m) {
			return m.caseReturn(v);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Return)) return false;
			Return other = (Return) otherObject;
			return (v.equals(other.v));
		}

		@Override
		public int hashCode() {
			return v.hashCode();
		}

		@Override
		public String toString() {
			return "Return(" + v + ")";
		}

	}

}
