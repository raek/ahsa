package se.raek.ahsa.ast;

import java.util.concurrent.atomic.AtomicInteger;

public class LoopLabel {
	
	public final int id;
	public final String label;
	
	public static final String NO_LABEL = "<no_label>";
	
	private static AtomicInteger nextId = new AtomicInteger(0);
	
	public LoopLabel(String label) {
		if (label == null) throw new NullPointerException();
		this.id = nextId.getAndIncrement();
		this.label = label;
	}
	
	@Override
	public String toString() {
		return "loop" + id + "/" + label;
	}

}
