package se.raek.ahsa;

import java.util.ArrayList;
import java.util.List;

import se.raek.ahsa.ast.EqualityOperator;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.ast.RelationalOperator;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.runtime.CompoundFunction;
import se.raek.ahsa.runtime.ControlAction;
import se.raek.ahsa.runtime.ControlAction.Return;
import se.raek.ahsa.runtime.Function;
import se.raek.ahsa.runtime.Store;
import se.raek.ahsa.runtime.Value;
import se.raek.ahsa.runtime.ControlAction.Next;

public class Interpreter implements Expression.Matcher<Value>, Statement.Matcher<ControlAction> {
	
	private final Store sto;
	
	private Interpreter(Store sto) {
		this.sto = sto;
	}
	
	public static Value eval(Expression expr, Store sto) {
		return expr.matchExpression(new Interpreter(sto));
	}
	
	public static ControlAction execute(Statement stmt, Store sto) {
		return stmt.matchStatement(new Interpreter(sto));
	}
	
	public static ControlAction execute(List<Statement> stmts, Store sto) {
		Interpreter interp = new Interpreter(sto);
		return interp.executeStatements(stmts);
	}
	
	public static class CastException extends RuntimeException {
		
		private static final long serialVersionUID = -3113471139719820971L;

		public CastException(String expected, String actual) {
			super(String.format("expected %s, got %s", expected, actual));
		}
		
	}
	
	public static String typeName(final Value v) {
		return v.matchValue(new Value.Matcher<String>() {
			@Override
			public String caseNull() {
				return "null";
			}
			@Override
			public String caseBoolean(boolean b) {
				return "boolean";
			}
			@Override
			public String caseNumber(double n) {
				return "number";
			}
			@Override
			public String caseFunction(Function fn) {
				return "function";
			}
		});
	}
	
	public static double castToNumber(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Double>() {
			@Override
			public Double caseNumber(double n) {
				return n;
			}
			@Override
			public Double otherwise() {
				throw new CastException("number", typeName(v));
			}
		});
	}
	
	public static Function castToFunction(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Function>() {
			@Override
			public Function caseFunction(Function fn) {
				return fn;
			}
			@Override
			public Function otherwise() {
				throw new CastException("function", typeName(v));
			}
		});
	}
	
	public static boolean isTruthy(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseNull() {
				return false;
			}
			@Override
			public Boolean caseBoolean(boolean b) {
				return b;
			}
			@Override
			public Boolean otherwise() {
				return true;
			}
		});
	}
	
	private ControlAction executeStatements(List<Statement> stmts) {
		for (Statement stmt : stmts) {
			ControlAction action = stmt.matchStatement(this);
			if (action instanceof Next) {
				continue;
			} else if (action instanceof Return) {
				return action;
			} else {
				throw new AssertionError("inexhaustive match");
			}
		}
		return Next.make();
	}

	@Override
	public Value caseConstant(Value v) {
		return v;
	}

	@Override
	public Value caseValueLookup(ValueLocation val) {
		return sto.lookupValue(val);
	}

	@Override
	public Value caseVariableLookup(VariableLocation var) {
		return sto.lookupVariable(var);
	}

	@Override
	public Value caseArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
		Value leftValue = left.matchExpression(this);
		Value rightValue = right.matchExpression(this);
		final double leftNumber = castToNumber(leftValue);
		final double rightNumber = castToNumber(rightValue);
		return Value.Number.make(op.matchArithmeticOperator(new ArithmeticOperator.Matcher<Double>() {
			@Override
			public Double caseAddition() {
				return leftNumber + rightNumber;
			}
			@Override
			public Double caseSubtraction() {
				return leftNumber - rightNumber;
			}
			@Override
			public Double caseMultiplication() {
				return leftNumber * rightNumber;
			}
			@Override
			public Double caseDivision() {
				return leftNumber / rightNumber;
			}
		}));
	}

	@Override
	public Value caseEqualityOperation(EqualityOperator op, Expression left, Expression right) {
		final Value leftValue = left.matchExpression(this);
		final Value rightValue = right.matchExpression(this);
		return Value.Boolean.make(op.matchEqualityOperator(new EqualityOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseEqual() {
				return leftValue.equals(rightValue);
			}
			@Override
			public Boolean caseUnequal() {
				return !leftValue.equals(rightValue);
			}
		}));
	}

	@Override
	public Value caseRelationalOperation(RelationalOperator op,
			Expression left, Expression right) {
		Value leftValue = left.matchExpression(this);
		Value rightValue = right.matchExpression(this);
		final double leftNumber = castToNumber(leftValue);
		final double rightNumber = castToNumber(rightValue);
		return Value.Boolean.make(op.matchRelationalOperator(new RelationalOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseGreater() {
				return leftNumber > rightNumber;
			}
			@Override
			public Boolean caseLess() {
				return leftNumber < rightNumber;
			}
			@Override
			public Boolean caseGreaterEqual() {
				return leftNumber >= rightNumber;
			}
			@Override
			public Boolean caseLessEqual() {
				return leftNumber <= rightNumber;
			}
		}));
	}

	@Override
	public Value caseFunctionApplicaion(Expression function,
			List<Expression> parameters) {
		Value evaledFunction = function.matchExpression(this);
		Function fn = castToFunction(evaledFunction);
		List<Value> evaledParams = new ArrayList<Value>();
		for (Expression expr : parameters) {
			evaledParams.add(expr.matchExpression(this));
		}
		return fn.apply(evaledParams);
	}

	@Override
	public Value caseFunctionAbstraction(List<ValueLocation> parameters,
			List<Statement> body) {
		return Value.Function.make(new CompoundFunction(parameters, body, sto));
	}

	@Override
	public ControlAction caseThrowawayExpression(Expression expr) {
		expr.matchExpression(this);
		return Next.make();
	}

	@Override
	public ControlAction caseValueDefinition(ValueLocation val, Expression expr) {
		Value result = expr.matchExpression(this);
		sto.defineValue(val, result);
		return Next.make();
	}

	@Override
	public ControlAction caseVariableAssignment(VariableLocation var, Expression expr) {
		Value result = expr.matchExpression(this);
		sto.assignVariable(var, result);
		return Next.make();
	}

	@Override
	public ControlAction caseConditional(Expression cond, List<Statement> thenStmts,
			List<Statement> elseStmts) {
		if (isTruthy(cond.matchExpression(this))) {
			return executeStatements(thenStmts);
		} else {
			return executeStatements(elseStmts);
		}
	}

	@Override
	public ControlAction caseReturn(Expression expr) {
		return Return.make(expr.matchExpression(this));
	}
}
