package se.raek.ahsa;

import java.util.ArrayList;
import java.util.List;

import se.raek.ahsa.ast.EqualityOperator;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.ast.LoopLabel;
import se.raek.ahsa.ast.RelationalOperator;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.runtime.Box;
import se.raek.ahsa.runtime.CompoundFunction;
import se.raek.ahsa.runtime.ControlAction;
import se.raek.ahsa.runtime.Function;
import se.raek.ahsa.runtime.Id;
import se.raek.ahsa.runtime.Store;
import se.raek.ahsa.runtime.Value;

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
	
	public static void executeTopLevel(List<Statement> stmts, Store sto) {
		Interpreter interp = new Interpreter(sto);
		ControlAction action = interp.executeStatements(stmts);
		action.matchControlAction(new ControlAction.Matcher<Void>() {
			public Void caseNext() {
				return null;
			}
			public Void caseBreak(LoopLabel loop) {
				throw new AssertionError("Tried to break from loop, reached program top level. Loop label: " + loop);
			}
			public Void caseReturn(Value v) {
				throw new AssertionError("Tried to return from function, reached program top level. Return value: " + v);
			}
		});
	}
	
	public static class CastException extends RuntimeException {
		
		private static final long serialVersionUID = -3113471139719820971L;

		public CastException(String expected, String actual) {
			super(String.format("expected %s, got %s", expected, actual));
		}
		
	}
	
	public static String typeName(final Value v) {
		return v.matchValue(new Value.Matcher<String>() {
			public String caseNull() {
				return "null";
			}
			public String caseBoolean(boolean b) {
				return "boolean";
			}
			public String caseNumber(double n) {
				return "number";
			}
			public String caseFunction(Function fn) {
				return "function";
			}
			public String caseId(Id id) {
				return "id";
			}
			public String caseBox(Box box) {
				return "box";
			}
		});
	}
	
	public static double castToNumber(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Double>() {
			public Double caseNumber(double n) {
				return n;
			}
			public Double otherwise() {
				throw new CastException("number", typeName(v));
			}
		});
	}
	
	public static Function castToFunction(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Function>() {
			public Function caseFunction(Function fn) {
				return fn;
			}
			public Function otherwise() {
				throw new CastException("function", typeName(v));
			}
		});
	}
	
	public static Box castToBox(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Box>() {
			public Box caseBox(Box box) {
				return box;
			}
			public Box otherwise() {
				throw new CastException("box", typeName(v));
			}
		});
	}
	
	public static boolean isTruthy(final Value v) {
		return v.matchValue(new Value.AbstractMatcher<Boolean>() {
			public Boolean caseNull() {
				return false;
			}
			public Boolean caseBoolean(boolean b) {
				return b;
			}
			public Boolean otherwise() {
				return true;
			}
		});
	}
	
	private boolean shouldExit(ControlAction action) {
		return action.matchControlAction(new ControlAction.Matcher<Boolean>() {
			public Boolean caseNext() {
				return false;
			}
			public Boolean caseBreak(LoopLabel loop) {
				return true;
			}
			public Boolean caseReturn(Value v) {
				return true;
			}
		});
	}
	
	private LoopLabel getLoopLabel(ControlAction action) {
		return action.matchControlAction(new ControlAction.AbstractMatcher<LoopLabel>() {
			@Override
			public LoopLabel caseBreak(LoopLabel loop) {
				return loop;
			}
			@Override
			public LoopLabel otherwise() {
				return null;
			}
		});
	}
	
	private ControlAction executeStatements(List<Statement> stmts) {
		for (Statement stmt : stmts) {
			ControlAction action = stmt.matchStatement(this);
			if (shouldExit(action)) {
				return action;
			}
		}
		return ControlAction.makeNext();
	}

	public Value caseConstant(Value v) {
		return v;
	}

	public Value caseValueLookup(ValueLocation val) {
		return sto.lookupValue(val);
	}

	public Value caseVariableLookup(VariableLocation var) {
		return sto.lookupVariable(var);
	}

	public Value caseArithmeticOperation(ArithmeticOperator op, Expression left, Expression right) {
		Value leftValue = left.matchExpression(this);
		Value rightValue = right.matchExpression(this);
		final double leftNumber = castToNumber(leftValue);
		final double rightNumber = castToNumber(rightValue);
		return Value.makeNumber(op.matchArithmeticOperator(new ArithmeticOperator.Matcher<Double>() {
			public Double caseAddition() {
				return leftNumber + rightNumber;
			}
			public Double caseSubtraction() {
				return leftNumber - rightNumber;
			}
			public Double caseMultiplication() {
				return leftNumber * rightNumber;
			}
			public Double caseDivision() {
				return leftNumber / rightNumber;
			}
		}));
	}

	public Value caseEqualityOperation(EqualityOperator op, Expression left, Expression right) {
		final Value leftValue = left.matchExpression(this);
		final Value rightValue = right.matchExpression(this);
		return Value.makeBoolean(op.matchEqualityOperator(new EqualityOperator.Matcher<Boolean>() {
			public Boolean caseEqual() {
				return leftValue.equals(rightValue);
			}
			public Boolean caseUnequal() {
				return !leftValue.equals(rightValue);
			}
		}));
	}

	public Value caseRelationalOperation(RelationalOperator op,
			Expression left, Expression right) {
		Value leftValue = left.matchExpression(this);
		Value rightValue = right.matchExpression(this);
		final double leftNumber = castToNumber(leftValue);
		final double rightNumber = castToNumber(rightValue);
		return Value.makeBoolean(op.matchRelationalOperator(new RelationalOperator.Matcher<Boolean>() {
			public Boolean caseGreater() {
				return leftNumber > rightNumber;
			}
			public Boolean caseLess() {
				return leftNumber < rightNumber;
			}
			public Boolean caseGreaterEqual() {
				return leftNumber >= rightNumber;
			}
			public Boolean caseLessEqual() {
				return leftNumber <= rightNumber;
			}
		}));
	}

	public Value caseFunctionApplication(Expression function,
			List<Expression> parameters) {
		Value evaledFunction = function.matchExpression(this);
		Function fn = castToFunction(evaledFunction);
		List<Value> evaledParams = new ArrayList<Value>();
		for (Expression expr : parameters) {
			evaledParams.add(expr.matchExpression(this));
		}
		return fn.apply(evaledParams);
	}

	public Value caseFunctionAbstraction(ValueLocation self,
			List<ValueLocation> parameters, List<Statement> body) {
		return Value.makeFunction(new CompoundFunction(self, parameters, body, sto));
	}

	public ControlAction caseThrowawayExpression(Expression expr) {
		expr.matchExpression(this);
		return ControlAction.makeNext();
	}

	public ControlAction caseValueDefinition(ValueLocation val, Expression expr) {
		Value result = expr.matchExpression(this);
		sto.defineValue(val, result);
		return ControlAction.makeNext();
	}

	public ControlAction caseVariableAssignment(VariableLocation var, Expression expr) {
		Value result = expr.matchExpression(this);
		sto.assignVariable(var, result);
		return ControlAction.makeNext();
	}

	public ControlAction caseConditional(Expression cond, List<Statement> thenStmts,
			List<Statement> elseStmts) {
		if (isTruthy(cond.matchExpression(this))) {
			return executeStatements(thenStmts);
		} else {
			return executeStatements(elseStmts);
		}
	}

	public ControlAction caseLoop(LoopLabel thisLoop, List<Statement> body) {
		while (true) {
			ControlAction action = executeStatements(body);
			LoopLabel thatLoop = getLoopLabel(action);
			if (thatLoop != null && thatLoop.equals(thisLoop)) {
				return ControlAction.makeNext();
			} else if (shouldExit(action)) {
				return action;
			}
		}
	}

	public ControlAction caseBreak(LoopLabel loop) {
		return ControlAction.makeBreak(loop);
	}

	public ControlAction caseReturn(Expression expr) {
		return ControlAction.makeReturn(expr.matchExpression(this));
	}
}
