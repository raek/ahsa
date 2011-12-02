package se.raek.ahsa.interpreter;

import static se.raek.ahsa.interpreter.Interpreter.castToArray;
import static se.raek.ahsa.interpreter.Interpreter.castToBox;
import static se.raek.ahsa.interpreter.Interpreter.castToInt;

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
			return castToBox(v0).deref();
		}
	};
	
	public static final Function boxSet = new AbstractFunctions.Function2() {
		@Override
		protected Value invoke(Value v0, Value v1) {
			castToBox(v0).assign(v1);
			return Value.makeNull();
		}
	};

	public static final Function array = new AbstractFunctions.Function1() {
		@Override
		protected Value invoke(Value v0) {
			return Value.makeArray(new Array(castToInt(v0)));
		}
	};

	public static final Function arrayGet = new AbstractFunctions.Function2() {
		@Override
		protected Value invoke(Value v0, Value v1) {
			return castToArray(v0).subscript(castToInt(v1));
		}
	};

	public static final Function arraySet = new AbstractFunctions.Function3() {
		@Override
		protected Value invoke(Value v0, Value v1, Value v2) {
			castToArray(v0).assignSubscript(castToInt(v1), v2);
			return Value.makeNull();
		}
	};

	public static final Function arrayLength = new AbstractFunctions.Function1() {
		@Override
		protected Value invoke(Value v0) {
			return Value.makeNumber(castToArray(v0).getLength());
		}
	};

}
