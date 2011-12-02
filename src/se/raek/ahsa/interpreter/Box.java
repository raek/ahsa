package se.raek.ahsa.interpreter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public final class Box {

	public final int id;
	private final AtomicReference<Value> ref;
	
	private static AtomicInteger nextId = new AtomicInteger(0);
	
	public Box(Value init) {
		id = nextId.getAndIncrement();
		ref = new AtomicReference<Value>(init);
	}
	
	public Value deref() {
		return ref.get();
	}
	
	public void assign(Value v) {
		ref.set(v);
	}
	
	@Override
	public String toString() {
		return "box" + id;
	}

}
