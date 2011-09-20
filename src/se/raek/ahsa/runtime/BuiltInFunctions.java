package se.raek.ahsa.runtime;

import se.raek.ahsa.Printer;

public class BuiltInFunctions {
	
	private BuiltInFunctions() {
	}
	
	public static final Function print = new AbstractFunctions.Function1() {
		@Override
		protected Value invoke(Value v0) {
			System.out.println(Printer.toString(v0));
			return Value.makeNull();
		}
	};

}
