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
		T caseId(se.raek.ahsa.runtime.Id id);
		T caseBox(se.raek.ahsa.runtime.Box box);
		T caseArray(se.raek.ahsa.runtime.Array array);
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

		public T caseId(se.raek.ahsa.runtime.Id id) {
			return otherwise();
		}

		public T caseBox(se.raek.ahsa.runtime.Box box) {
			return otherwise();
		}

		public T caseArray(se.raek.ahsa.runtime.Array array) {
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

	public static Value makeId(se.raek.ahsa.runtime.Id id) {
		return new Id(id);
	}

	public static Value makeBox(se.raek.ahsa.runtime.Box box) {
		return new Box(box);
	}

	public static Value makeArray(se.raek.ahsa.runtime.Array array) {
		return new Array(array);
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

	private static final class Id extends Value {

		private final se.raek.ahsa.runtime.Id id;

		public Id(se.raek.ahsa.runtime.Id id) {
			if (id == null) throw new NullPointerException();
			this.id = id;
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseId(id);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Id)) return false;
			Id other = (Id) otherObject;
			return (id.equals(other.id));
		}

		@Override
		public int hashCode() {
			return id.hashCode();
		}

		@Override
		public String toString() {
			return "Id(" + id + ")";
		}

	}

	private static final class Box extends Value {

		private final se.raek.ahsa.runtime.Box box;

		public Box(se.raek.ahsa.runtime.Box box) {
			if (box == null) throw new NullPointerException();
			this.box = box;
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseBox(box);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Box)) return false;
			Box other = (Box) otherObject;
			return (box.equals(other.box));
		}

		@Override
		public int hashCode() {
			return box.hashCode();
		}

		@Override
		public String toString() {
			return "Box(" + box + ")";
		}

	}

	private static final class Array extends Value {

		private final se.raek.ahsa.runtime.Array array;

		public Array(se.raek.ahsa.runtime.Array array) {
			if (array == null) throw new NullPointerException();
			this.array = array;
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseArray(array);
		}

		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Array)) return false;
			Array other = (Array) otherObject;
			return (array.equals(other.array));
		}

		@Override
		public int hashCode() {
			return array.hashCode();
		}

		@Override
		public String toString() {
			return "Array(" + array + ")";
		}

	}

}
