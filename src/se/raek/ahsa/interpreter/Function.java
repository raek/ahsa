package se.raek.ahsa.interpreter;

import java.util.List;


public interface Function {

	Value apply(List<Value> parameters);
	
	public static class ArityException extends RuntimeException {

		private static final long serialVersionUID = 72149012886064001L;

		public ArityException(int expected, int actual) {
			super(String.format("expected %d parameters, got %d", expected, actual));
		}
		
	}

}
