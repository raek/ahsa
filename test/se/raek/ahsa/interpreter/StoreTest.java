package se.raek.ahsa.interpreter;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.interpreter.Store;
import se.raek.ahsa.interpreter.Value;

public class StoreTest {
	
	private static final ValueLocation valX = new ValueLocation("x");
	private static final VariableLocation varY = new VariableLocation("y");
	private static final Value v1 = Value.makeNumber(1.0);
	private static final Value v2 = Value.makeNumber(1.0);

	@Test
	public void lookupExistingValue() {
		Store sto = new Store(null);
		sto.defineValue(valX, v1);
		assertEquals(v1, sto.lookupValue(valX));
	}

	@Test
	public void lookupExistingParentValue() {
		Store parent = new Store(null);
		parent.defineValue(valX, v1);
		Store child = new Store(parent);
		assertEquals(v1, child.lookupValue(valX));
	}
	
	@Test(expected=Store.ValueNotFoundException.class)
	public void lookupNonExistingValue() {
		Store sto = new Store(null);
		sto.lookupValue(valX);
	}
	
	@Test
	public void defineNonExistingValue() {
		Store sto = new Store(null);
		sto.defineValue(valX, v1);
		assertEquals(v1, sto.lookupValue(valX));
		
	}
	
	@Test(expected=Store.ValueAlreadyDefinedException.class)
	public void defineExistingValue() {
		Store sto = new Store(null);
		sto.defineValue(valX, v1);
		sto.defineValue(valX, v2);
		
	}

	@Test
	public void lookupExistingVariable() {
		Store sto = new Store(null);
		sto.assignVariable(varY, v1);
		assertEquals(v1, sto.lookupVariable(varY));
	}

	@Test(expected=Store.VariableNotFoundException.class)
	public void lookupNonExistingVariable() {
		Store sto = new Store(null);
		sto.lookupVariable(varY);
	}

	@Test(expected=Store.VariableNotFoundException.class)
	public void lookupExistingInaccessibleParentVariable() {
		Store parent = new Store(null);
		parent.assignVariable(varY, v1);
		Store child = new Store(parent);
		assertEquals(v1, child.lookupVariable(varY));
	}

	@Test
	public void assignVariable() {
		Store sto = new Store(null);
		sto.assignVariable(varY, v1);
		assertEquals(v1, sto.lookupVariable(varY));
		sto.assignVariable(varY, v2);
		assertEquals(v2, sto.lookupVariable(varY));
	}

	@Test
	public void assignExistingInaccessibleParentVariable() {
		Store parent = new Store(null);
		parent.assignVariable(varY, v1);
		Store child = new Store(parent);
		child.assignVariable(varY, v2);
		assertEquals(v2, child.lookupVariable(varY));
	}

}
