package se.raek.ahsa.ast;

public enum EqualityOperator {

	EQUAL {
		@Override
		public <T> T matchEqualityOperator(Matcher<T> m) {
			return m.caseEqual();
		}
	},
	UNEQUAL {
		@Override
		public <T> T matchEqualityOperator(Matcher<T> m) {
			return m.caseUnequal();
		}
	};
	
	public abstract <T> T matchEqualityOperator(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseEqual();
		T caseUnequal();
	}

}
