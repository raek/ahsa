package se.raek.ahsa.parser;

import java.util.ArrayDeque;
import java.util.Deque;

public class EnvironmentStack {
	
	private Deque<Environment> stack;
	
	public EnvironmentStack() {
		stack = new ArrayDeque<Environment>();
		stack.addFirst(new Environment());
	}
	
	public Environment getCurrent() {
		return stack.peekFirst();
	}
	
	public void enterScope(Environment.Type type) {
		Environment env = new Environment(getCurrent(), type);
		stack.addFirst(env);
	}
	
	public void exitScope() {
		stack.removeFirst();
	}

}
