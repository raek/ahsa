package se.raek.ahsa.interpreter;

import java.util.concurrent.atomic.AtomicInteger;

public final class Id {

	public final int id;
	
	private static AtomicInteger nextId = new AtomicInteger(0);
	
	public Id() {
		id = nextId.getAndIncrement();
	}
	
	@Override
	public String toString() {
		return "id" + id;
	}

}
