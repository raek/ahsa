package se.raek.ahsa.ast;

import java.util.concurrent.atomic.AtomicInteger;

public class VariableLocation {
	
	public final int id;
	public final String label;
	
	private static AtomicInteger nextId = new AtomicInteger(0);
	
	public VariableLocation(String label) {
		if (label == null) throw new NullPointerException();
		this.id = nextId.getAndIncrement();
		this.label = label;
	}

}