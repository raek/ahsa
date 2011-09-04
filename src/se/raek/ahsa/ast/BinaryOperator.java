package se.raek.ahsa.ast;

public enum BinaryOperator {
	
	ADDITION {
		@Override
		public <T> T matchBinaryOperator(Matcher<T> m) {
			return m.caseAddition();
		}
	},
	SUBTRACTION {
		@Override
		public <T> T matchBinaryOperator(Matcher<T> m) {
			return m.caseSubtraction();
		}
	},
	MULTIPLICATION {
		@Override
		public <T> T matchBinaryOperator(Matcher<T> m) {
			return m.caseMultiplication();
		}
	},
	DIVISION {
		@Override
		public <T> T matchBinaryOperator(Matcher<T> m) {
			return m.caseDivision();
		}
	};
	
	public abstract <T> T matchBinaryOperator(Matcher<T> m);
	
	public interface Matcher<T> {
		T caseAddition();
		T caseSubtraction();
		T caseMultiplication();
		T caseDivision();
	}

}
