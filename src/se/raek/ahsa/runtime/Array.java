package se.raek.ahsa.runtime;

import java.util.concurrent.atomic.AtomicInteger;

public final class Array {

	public final int id;
	private final Value[] values;

	private static AtomicInteger nextId = new AtomicInteger(0);

	public Array(int length) {
		id = nextId.getAndIncrement();
		values = new Value[length];
		Value init = Value.makeNull();
		for (int i = 0; i < length; i++) {
			values[i] = init;
		}
	}

	public Value subscript(int i) {
		return values[i];
	}

	public void assignSubscript(int i, Value v) {
		values[i] = v;
	}

	public int getLength() {
		return values.length;
	}

	@Override
	public String toString() {
		return "array" + id;
	}

}
