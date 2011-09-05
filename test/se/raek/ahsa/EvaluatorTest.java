package se.raek.ahsa;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.BinaryOperator;
import se.raek.ahsa.ast.Expression.Constant;
import se.raek.ahsa.ast.Expression.BinaryOperation;
import se.raek.ahsa.runtime.Value;
import se.raek.ahsa.runtime.Value.Null;
import se.raek.ahsa.runtime.Value.Number;

public class EvaluatorTest {

	@Test
	public void constant() {
		Value v = Number.make(0);
		assertEquals(v, Evaluator.eval(Constant.make(v)));
	}

	@Test
	public void addition() {
		assertEquals(Number.make(5), Evaluator.eval(BinaryOperation.make(
				BinaryOperator.ADDITION, Constant.make(Number.make(2)),
				Constant.make(Number.make(3)))));
	}

	@Test
	public void subtraction() {
		assertEquals(Number.make(-1), Evaluator.eval(BinaryOperation.make(
				BinaryOperator.SUBTRACTION, Constant.make(Number.make(2)),
				Constant.make(Number.make(3)))));
	}

	@Test
	public void multiplication() {
		assertEquals(Number.make(6), Evaluator.eval(BinaryOperation.make(
				BinaryOperator.MULTIPLICATION, Constant.make(Number.make(2)),
				Constant.make(Number.make(3)))));
	}

	@Test
	public void division() {
		assertEquals(Number.make(2), Evaluator.eval(BinaryOperation.make(
				BinaryOperator.DIVISION, Constant.make(Number.make(6)),
				Constant.make(Number.make(3)))));
	}

	@Test(expected=Evaluator.CastException.class)
	public void nonNumberArgument() {
		Evaluator.eval(BinaryOperation.make(BinaryOperator.ADDITION,
				Constant.make(Number.make(1)), Constant.make(Null.make())));
	}

}
