package se.raek.ahsa.ast;

import java.util.concurrent.atomic.AtomicInteger;

public final class ValueLocation {
	
	public final int id;
	public final String label;
	
	private static AtomicInteger nextId = new AtomicInteger(0);
	
	public ValueLocation(String label) {
		if (label == null) throw new NullPointerException();
		this.id = nextId.getAndIncrement();
		this.label = label;
	}
	
	@Override
	public String toString() {
		return "val" + id + "/" + label; 
	}

}
