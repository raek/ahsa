package se.raek.ahsa.runtime;

import se.raek.ahsa.runtime.Value.Function;

public interface ControlAction {
	
	<T> T matchControlAction(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseNext();
		T caseReturn(Value v);
	}
	
	public static final class Next implements ControlAction {
		
		private static final Next instance = new Next();
		
		private Next() {
		}
		
		public static Next make() {
			return instance;
		}
		
		public <T> T matchControlAction(Matcher<T> m) {
			return m.caseNext();
		}
		
		@Override
		public String toString() {
			return "Next()";
		}
		
	}
	
	public static final class Return implements ControlAction {
		
		private final Value v;
		
		private Return(Value v) {
			if (v == null) throw new NullPointerException(); 
			this.v = v;
		}
		
		public static Return make(Value v) {
			return new Return(v);
		}
		
		public <T> T matchControlAction(Matcher<T> m) {
			return m.caseReturn(v);
		}
		
		@Override
		public boolean equals(Object otherObject) {
			if (this == otherObject) return true;
			if (!(otherObject instanceof Function)) return false;
			Return otherReturn = (Return) otherObject;
			return v.equals(otherReturn.v);
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
