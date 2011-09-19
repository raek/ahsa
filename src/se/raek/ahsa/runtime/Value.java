package se.raek.ahsa.runtime;

public interface Value {
	
	<T> T matchValue(Matcher<T> m);

	public interface Matcher<T> {
		T caseNull();
		T caseBoolean(boolean b);
		T caseNumber(double n);
		T caseFunction(se.raek.ahsa.runtime.Function fn);
	}

	public static final class Null implements Value {
		
		private static final Null instance = new Null();
		
		private Null() {
		}
		
		public static Null make() {
			return instance;
		}
	
		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseNull();
		}
		
		@Override
		public String toString() {
			return "Null()";
		}
	
	}
	
	public static final class Boolean implements Value {
		
		public final boolean b;
		
		private static final Boolean trueInstance = new Boolean(true);
		private static final Boolean falseInstance = new Boolean(false);
		
		private Boolean(boolean b) {
			this.b = b;
		}
		
		public static Boolean make(boolean b) {
			return b ? trueInstance : falseInstance;
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseBoolean(b);
		}
		
		@Override
		public String toString() {
			return "Boolean(" + b + ")";
		}
	}
	
	public static final class Number implements Value {
		
		public final double n;
		
		private Number(double n) {
			this.n = n;
		}
		
		public static Number make(double n) {
			return new Number(n);
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseNumber(n);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Number)) return false;
			Number otherNumber = (Number) otherObject;
			return Double.compare(n, otherNumber.n) == 0;
		}
		
		@Override
		public int hashCode() {
			long v = Double.doubleToLongBits(n);
			return  (int)(v^(v>>>32));
		}
		
		@Override
		public String toString() {
			return "Number(" + n + ")";
		}
	}
	
	public static final class Function implements Value {
		
		public final se.raek.ahsa.runtime.Function fn;
		
		private Function(se.raek.ahsa.runtime.Function fn) {
			if (fn == null) throw new NullPointerException();
			this.fn = fn;
		}
		
		public static Function make(se.raek.ahsa.runtime.Function fn) {
			return new Function(fn);
		}

		@Override
		public <T> T matchValue(Matcher<T> m) {
			return m.caseFunction(fn);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Function)) return false;
			Function otherFunction = (Function) otherObject;
			return fn.equals(otherFunction.fn);
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
	
	public static abstract class AbstractMatcher<T> implements Matcher<T> {
		
		public abstract T otherwise();

		@Override
		public T caseNull() {
			return otherwise();
		}

		@Override
		public T caseBoolean(boolean b) {
			return otherwise();
		}

		@Override
		public T caseNumber(double n) {
			return otherwise();
		}

		@Override
		public T caseFunction(se.raek.ahsa.runtime.Function fn) {
			return otherwise();
		}
		
	}
	
}
