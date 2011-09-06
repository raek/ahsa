package se.raek.ahsa;

import java.io.PrintWriter;

import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.ast.Expression.ArithmeticOperation;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.runtime.Value;
import se.raek.ahsa.runtime.Value.Number;

public class Main {

	public static void main(String[] args) {
		PrintWriter out = new PrintWriter(System.out);
		Expression expr = ArithmeticOperation.make(ArithmeticOperator.ADDITION,
				Constant.make(Number.make(1)), Constant.make(Number.make(2)));
		out.print("Expression to evaluate: ");
		Printer.print(expr, out);
		out.println();
		Value result = Interpreter.eval(expr, null);
		out.print("Value: ");
		Printer.print(result, out);
		out.println();
		out.flush();
	}

}
