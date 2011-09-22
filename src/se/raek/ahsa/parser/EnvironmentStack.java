package se.raek.ahsa.parser;

import java.util.Stack;

public class EnvironmentStack {
	
	private Stack<Environment> stack;
	
	public EnvironmentStack() {
		stack = new Stack<Environment>();
		stack.push(new Environment());
	}
	
	public Environment getCurrent() {
		return stack.peek();
	}
	
	public void enterScope(Environment.Type type) {
		Environment env = new Environment(getCurrent(), type);
		stack.push(env);
	}
	
	public void exitScope() {
		stack.pop();
	}
	
	public void resetEnvironment(Environment env) {
		stack.clear();
		stack.push(env);
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}

}
