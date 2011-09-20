package se.raek.ahsa.runtime;


public abstract class Value {

	private Value() {
	}

	public abstract <T> T matchValue(Matcher<T> m);

	public interface Matcher<T> {
		T caseNull();
		T caseBoolean(boolean b);
		T caseNumber(double n);
		T caseFunction(se.raek.ahsa.runtime.Function fn);
	}

	public static abstract class AbstractMatcher<T> implements Matcher<T> {

		public abstract T otherwise();

		public T caseNull() {
			return otherwise();
		}

		public T caseBoolean(boolean b) {
			return otherwise();
		}

		public T caseNumber(double n) {
			return otherwise();
		}

		public T caseFunction(se.raek.ahsa.runtime.Function fn) {
			return otherwise();
		}

	}

	private static final Null singletonNull = new Null();

	public static Value makeNull() {
		return singletonNull;
	}

	private static final Boolean singletonBooleanTrue = new Boolean(true);

	private static final Boolean singletonBooleanFalse = new Boolean(false);

	public static Value makeBoolean(boolean b) {
		return b ? singletonBooleanTrue : singletonBooleanFalse;
	}

	public static Value makeNumber(double n) {
		return new Number(n);
	}

	public static Value makeFunction(se.raek.ahsa.runtime.Function fn) {
		return new Function(fn);
	}

	private static final class Null extends Value {

		public Null() {
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseNull();
		}

		// Using identity-based .equals for the interning Null class

		// Using identity-based .hashCode for the interning Null class

		@Override
		public String toString() {
			return "Null()";
		}

	}

	private static final class Boolean extends Value {

		private final boolean b;

		public Boolean(boolean b) {
			this.b = b;
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseBoolean(b);
		}

		// Using identity-based .equals for the interning Boolean class

		// Using identity-based .hashCode for the interning Boolean class

		@Override
		public String toString() {
			return "Boolean(" + b + ")";
		}

	}

	private static final class Number extends Value {

		private final double n;

		public Number(double n) {
			this.n = n;
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseNumber(n);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Number)) return false;
			Number other = (Number) otherObject;
			return (Double.compare(n, other.n) == 0);
		}

		@Override
		public int hashCode() {
			return (int) (Double.doubleToLongBits(n) ^ (Double.doubleToLongBits(n) >>> 32));
		}

		@Override
		public String toString() {
			return "Number(" + n + ")";
		}

	}

	private static final class Function extends Value {

		private final se.raek.ahsa.runtime.Function fn;

		public Function(se.raek.ahsa.runtime.Function fn) {
			if (fn == null) throw new NullPointerException();
			this.fn = fn;
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseFunction(fn);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Function)) return false;
			Function other = (Function) otherObject;
			return (fn.equals(other.fn));
		}

		@Override
		public int hashCode() {
			return fn.hashCode();
		}

		@Override
		public String toString() {
			return "Function(" + fn + ")";
		}

	}

}
