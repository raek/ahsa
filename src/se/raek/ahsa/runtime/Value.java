package se.raek.ahsa.runtime;

public interface Value {
	
	<T> T matchValue(Matcher<T> m);

	public interface Matcher<T> {
		T caseNull();
		T caseBoolean(boolean b);
		T caseNumber(double n);
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
		
	}
	
}
