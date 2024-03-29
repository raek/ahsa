package se.raek.ahsa.parser;

import java.util.HashMap;
import java.util.Map;

import se.raek.ahsa.ast.LoopLabel;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;

public class Environment {
	
	public enum Type {
		BLOCK,
		FUNCTION
	}
	
	private final Map<String, Identifier> bindings;
	
	public Environment() {
		bindings = new HashMap<String, Identifier>();
	}

	public Environment(Environment parent, final Type type) {
		if (type == null) throw new NullPointerException();
		bindings = new HashMap<String, Identifier>();
		if (parent == null) return;
		for (Map.Entry<String, Identifier> entry : parent.bindings.entrySet()) {
			final String label = entry.getKey();
			final Identifier id = entry.getValue();
			id.matchIdentifier(new Identifier.AbstractMatcher<Void>() {
				@Override
				public Void caseVariable(VariableLocation var) {
					if (type == Type.FUNCTION) {
						bindings.put(label, Identifier.makeInaccessibleVariable(var));
					} else { // if (type == Type.BLOCK) {
						bindings.put(label, id);
					}
					return null;
				}
				@Override
				public Void caseLoop(LoopLabel loop) {
					if (type == Type.FUNCTION) {
						bindings.put(label, Identifier.makeInaccessibleLoop(loop));
					} else { // if (type == Type.BLOCK) {
						bindings.put(label, id);
					}
					return null;
				}
				@Override
				public Void otherwise() {
					bindings.put(label, id);
					return null;
				}
			});
		}
	}
	
	@Override
	public String toString() {
		return bindings.toString();
	}

	public Identifier resolve(String id) {
		Identifier result = bindings.get(id);
		if (result == null) {
			result = Identifier.makeUnbound();
		}
		return result;
	}
	
	public ValueLocation installValue(String id) {
		ValueLocation val = new ValueLocation(id);
		bindings.put(id, Identifier.makeValue(val));
		return val;
	}
	
	public VariableLocation installVariable(String id) {
		VariableLocation var = new VariableLocation(id);
		bindings.put(id, Identifier.makeVariable(var));
		return var;
	}
	
	public LoopLabel installLoop(String id) {
		LoopLabel loop = new LoopLabel(id);
		Identifier identifier = Identifier.makeLoop(loop);
		bindings.put(id, identifier);
		bindings.put(LoopLabel.NO_LABEL, identifier);
		return loop;
	}
	
}
