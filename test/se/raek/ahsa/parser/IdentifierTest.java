package se.raek.ahsa.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.parser.Identifier.InaccessibleVariable;
import se.raek.ahsa.parser.Identifier.Matcher;
import se.raek.ahsa.parser.Identifier.Unbound;
import se.raek.ahsa.parser.Identifier.Value;
import se.raek.ahsa.parser.Identifier.Variable;

public class IdentifierTest {

	@Test
	public void matchUnbound() {
		assertTrue(Unbound.make().matchIdentifier(new Matcher<Boolean>() {
			@Override
			public Boolean caseUnbound() {
				return true;
			}
			@Override
			public Boolean caseValue(ValueLocation val) {
				return false;
			}
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return false;
			}
			@Override
			public Boolean caseInaccessibleVariable(VariableLocation var) {
				return false;
			}
		}));
	}

	@Test
	public void matchValue() {
		final ValueLocation val0 = new ValueLocation("x");
		assertTrue(Value.make(val0).matchIdentifier(new Matcher<Boolean>() {
			@Override
			public Boolean caseUnbound() {
				return false;
			}
			@Override
			public Boolean caseValue(ValueLocation val) {
				return val.equals(val0);
			}
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return false;
			}
			@Override
			public Boolean caseInaccessibleVariable(VariableLocation var) {
				return false;
			}
		}));
	}

	@Test
	public void matchVariable() {
		final VariableLocation var0 = new VariableLocation("y");
		assertTrue(Variable.make(var0).matchIdentifier(new Matcher<Boolean>() {
			@Override
			public Boolean caseUnbound() {
				return false;
			}
			@Override
			public Boolean caseValue(ValueLocation val) {
				return false;
			}
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return var.equals(var0);
			}
			@Override
			public Boolean caseInaccessibleVariable(VariableLocation var) {
				return false;
			}
		}));
	}

	@Test
	public void matchInaccessibleVariable() {
		final VariableLocation var0 = new VariableLocation("y");
		assertTrue(InaccessibleVariable.make(var0).matchIdentifier(new Matcher<Boolean>() {
			@Override
			public Boolean caseUnbound() {
				return false;
			}
			@Override
			public Boolean caseValue(ValueLocation val) {
				return false;
			}
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return false;
			}
			@Override
			public Boolean caseInaccessibleVariable(VariableLocation var) {
				return var.equals(var0);
			}
		}));
	}
	
	@Test
	public void equalsUnbound() {
		assertTrue(Unbound.make().equals(Unbound.make()));
	}
	
	@Test
	public void equalsValue() {
		ValueLocation val = new ValueLocation("x");
		assertTrue(Value.make(val).equals(Value.make(val)));
	}
	
	@Test
	public void equalsVariable() {
		VariableLocation var = new VariableLocation("y");
		assertTrue(Variable.make(var).equals(Variable.make(var)));
	}
	
	@Test
	public void equalsInaccessibleVariable() {
		VariableLocation var = new VariableLocation("y");
		assertTrue(InaccessibleVariable.make(var).equals(InaccessibleVariable.make(var)));
	}
	
	@Test
	public void equalHashCodeUnbound() {
		assertTrue(Unbound.make().hashCode() == Unbound.make().hashCode());
	}
	
	@Test
	public void equalHashCodeValue() {
		ValueLocation val = new ValueLocation("x");
		assertTrue(Value.make(val).hashCode() == Value.make(val).hashCode());
	}
	
	@Test
	public void equalHashCodeVariable() {
		VariableLocation var = new VariableLocation("y");
		assertTrue(Variable.make(var).hashCode() == Variable.make(var).hashCode());
	}
	
	@Test
	public void equalHashCodeInaccessibleVariable() {
		VariableLocation var = new VariableLocation("y");
		assertTrue(InaccessibleVariable.make(var).hashCode() == InaccessibleVariable.make(var).hashCode());
	}
	
	@Test
	public void notEqualValue() {
		ValueLocation val1 = new ValueLocation("x");
		ValueLocation val2 = new ValueLocation("y");
		assertFalse(Value.make(val1).equals(Value.make(val2)));
	}
	
	@Test
	public void notEqualVariable() {
		VariableLocation var1 = new VariableLocation("x");
		VariableLocation var2 = new VariableLocation("y");
		assertFalse(Variable.make(var1).equals(Variable.make(var2)));
	}
	
	@Test
	public void notEqualInaccessibleVariable() {
		VariableLocation var1 = new VariableLocation("x");
		VariableLocation var2 = new VariableLocation("y");
		assertFalse(InaccessibleVariable.make(var1).equals(InaccessibleVariable.make(var2)));
	}

}
