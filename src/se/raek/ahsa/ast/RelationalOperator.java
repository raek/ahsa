package se.raek.ahsa.ast;

public enum RelationalOperator {
	
	GREATER {
		@Override
		public <T> T matchRelationalOperator(Matcher<T> m) {
			return m.caseGreater();
		}
	},
	LESS {
		@Override
		public <T> T matchRelationalOperator(Matcher<T> m) {
			return m.caseLess();
		}
	},
	GREATER_EQUAL {
		@Override
		public <T> T matchRelationalOperator(Matcher<T> m) {
			return m.caseGreaterEqual();
		}
	},
	LESS_EQUAL {
		@Override
		public <T> T matchRelationalOperator(Matcher<T> m) {
			return m.caseLessEqual();
		}
	};
	
	public abstract <T> T matchRelationalOperator(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseGreater();
		T caseLess();
		T caseGreaterEqual();
		T caseLessEqual();
	}

}
