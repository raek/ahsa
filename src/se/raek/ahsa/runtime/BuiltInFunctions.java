package se.raek.ahsa.runtime;

import se.raek.ahsa.Interpreter;
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
	
	public static final Function id = new AbstractFunctions.Function0() {
		@Override
		protected Value invoke() {
			return Value.makeId(new Id());
		}
	};
	
	public static final Function box = new AbstractFunctions.Function1() {
		@Override
		protected Value invoke(Value v0) {
			return Value.makeBox(new Box(v0));
		}
	};
	
	public static final Function boxGet = new AbstractFunctions.Function1() {
		@Override
		protected Value invoke(Value v0) {
			return Interpreter.castToBox(v0).deref();
		}
	};
	
	public static final Function boxSet = new AbstractFunctions.Function2() {
		@Override
		protected Value invoke(Value v0, Value v1) {
			Interpreter.castToBox(v0).assign(v1);
			return Value.makeNull();
		}
	};

}
