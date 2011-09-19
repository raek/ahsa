package se.raek.ahsa.runtime;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.runtime.Value;

public class ValueTest {

	@Test
	public void matchNull() {
		assertTrue(Value.Null.make().matchValue(new Value.Matcher<Boolean>() {
			@Override
			public Boolean caseNull() {
				return true;
			}
			@Override
			public Boolean caseBoolean(boolean b) {
				return false;
			}
			@Override
			public Boolean caseNumber(double n) {
				return false;
			}
			@Override
			public Boolean caseFunction(Function fn) {
				return false;
			}
		}));
	}

	@Test
	public void matchTrue() {
		assertTrue(Value.Boolean.make(true).matchValue(new Value.Matcher<Boolean>() {
			@Override
			public Boolean caseNull() {
				return false;
			}
			@Override
			public Boolean caseBoolean(boolean b) {
				return b == true;
			}
			@Override
			public Boolean caseNumber(double n) {
				return false;
			}
			@Override
			public Boolean caseFunction(Function fn) {
				return false;
			}
		}));
	}

	@Test
	public void matchFalse() {
		assertTrue(Value.Boolean.make(false).matchValue(new Value.Matcher<Boolean>() {
			@Override
			public Boolean caseNull() {
				return false;
			}
			@Override
			public Boolean caseBoolean(boolean b) {
				return b == false;
			}
			@Override
			public Boolean caseNumber(double n) {
				return false;
			}
			@Override
			public Boolean caseFunction(Function fn) {
				return false;
			}
		}));
	}

	@Test
	public void matchNumber() {
		final double x = 123.0;
		assertTrue(Value.Number.make(x).matchValue(new Value.Matcher<Boolean>() {
			@Override
			public Boolean caseNull() {
				return false;
			}
			@Override
			public Boolean caseBoolean(boolean b) {
				return false;
			}
			@Override
			public Boolean caseNumber(double n) {
				return n == x;
			}
			@Override
			public Boolean caseFunction(Function fn) {
				return false;
			}
		}));
	}
	
	@Test
	public void equalsNull() {
		assertTrue(Value.Null.make().equals(Value.Null.make()));
	}
	
	@Test
	public void equalsTrue() {
		assertTrue(Value.Boolean.make(true).equals(Value.Boolean.make(true)));
	}
	
	@Test
	public void equalsFalse() {
		assertTrue(Value.Boolean.make(false).equals(Value.Boolean.make(false)));
	}
	
	@Test
	public void equalsNumber() {
		assertTrue(Value.Number.make(123.0).equals(Value.Number.make(123.0)));
	}
	
	@Test
	public void equalHashCodeNull() {
		assertTrue(Value.Null.make().hashCode() == Value.Null.make().hashCode());
	}
	
	@Test
	public void equalHashCodeTrue() {
		assertTrue(Value.Boolean.make(true).hashCode() == Value.Boolean.make(true).hashCode());
	}
	
	@Test
	public void equalHashCodeFalse() {
		assertTrue(Value.Boolean.make(false).hashCode() == Value.Boolean.make(false).hashCode());
	}
	
	@Test
	public void equalHashCodeNumber() {
		assertTrue(Value.Number.make(123.0).hashCode() == Value.Number.make(123.0).hashCode());
	}
	
	@Test
	public void notEqualBoolean() {
		assertFalse(Value.Boolean.make(true).equals(Value.Boolean.make(false)));
	}
	
	@Test
	public void notEqualNumber() {
		assertFalse(Value.Number.make(1.0).equals(Value.Number.make(2.0)));
	}
	
	@Test
	public void matchOtherwiseNull() {
		assertTrue(Value.Null.make().matchValue(new Value.AbstractMatcher<Boolean>() {
			@Override
			public Boolean otherwise() {
				return true;
			}
			@Override
			public Boolean caseBoolean(boolean b) {
				return false;
			}
			@Override
			public Boolean caseNumber(double n) {
				return false;
			}
		}));
	}
	
	@Test
	public void matchOtherwiseBoolean() {
		assertTrue(Value.Boolean.make(true).matchValue(new Value.AbstractMatcher<Boolean>() {
			@Override
			public Boolean otherwise() {
				return true;
			}
			@Override
			public Boolean caseNull() {
				return false;
			}
			@Override
			public Boolean caseNumber(double n) {
				return false;
			}
		}));
	}
	
	@Test
	public void matchOtherwiseNumber() {
		assertTrue(Value.Number.make(123.0).matchValue(new Value.AbstractMatcher<Boolean>() {
			@Override
			public Boolean otherwise() {
				return true;
			}
			@Override
			public Boolean caseNull() {
				return false;
			}
			@Override
			public Boolean caseBoolean(boolean b) {
				return false;
			}
		}));
	}

}
