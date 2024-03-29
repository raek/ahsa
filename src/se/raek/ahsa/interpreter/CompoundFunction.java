package se.raek.ahsa.interpreter;

import java.util.Iterator;
import java.util.List;

import se.raek.ahsa.ast.LoopLabel;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.interpreter.ControlAction.Matcher;

public class CompoundFunction implements Function {
	
	public final ValueLocation self;
	public final List<ValueLocation> formalParameters;
	public final List<Statement> body;
	public final Store sto;
	
	public CompoundFunction(ValueLocation self, List<ValueLocation> formalParameters, List<Statement> body, Store sto) {
		if (formalParameters == null || body == null || sto == null) throw new NullPointerException();
		this.self = self;
		this.formalParameters = formalParameters;
		this.body = body;
		this.sto = sto;
	}

	public Value apply(List<Value> actualParameters) {
		Store newStore = new Store(sto);
		if (self != null) {
			newStore.defineValue(self, Value.makeFunction(this));
		}
		Iterator<ValueLocation> formals = formalParameters.iterator();
		Iterator<Value> actuals = actualParameters.iterator();
		while(formals.hasNext() && actuals.hasNext()) {
			newStore.defineValue(formals.next(), actuals.next());
		}
		if (formals.hasNext() || actuals.hasNext()) {
			throw new Function.ArityException(formalParameters.size(), actualParameters.size());
		}
		ControlAction action = Interpreter.execute(body, newStore);
		return action.matchControlAction(new Matcher<Value>() {
			public Value caseNext() {
				return Value.makeNull();
			}
			public Value caseBreak(LoopLabel loop) {
				throw new AssertionError("Tried to break from loop, reached function top level. Loop label: " + loop);
			}
			public Value caseReturn(Value v) {
				return v;
			}
		});
	}

}
