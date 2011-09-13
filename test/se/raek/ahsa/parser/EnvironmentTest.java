package se.raek.ahsa.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.parser.Environment.Type;
import se.raek.ahsa.parser.Identifier.AbstractMatcher;

public class EnvironmentTest {

	@Test
	public void resolveUnbound() {
		Environment env = new Environment();
		assertTrue(env.resolve("x").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseUnbound() {
				return true;
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveValue() {
		Environment env = new Environment();
		final ValueLocation val0 = env.installValue("x");
		assertTrue(env.resolve("x").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseValue(ValueLocation val) {
				return val.equals(val0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveVariable() {
		Environment env = new Environment();
		final VariableLocation var0 = env.installVariable("x");
		assertTrue(env.resolve("x").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return var.equals(var0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveParentValueBlock() {
		Environment parent = new Environment();
		final ValueLocation val0 = parent.installValue("x");
		Environment child = new Environment(parent, Type.BLOCK);
		assertTrue(child.resolve("x").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseValue(ValueLocation val) {
				return val.equals(val0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveParentValueFunction() {
		Environment parent = new Environment();
		final ValueLocation val0 = parent.installValue("x");
		Environment child = new Environment(parent, Type.FUNCTION);
		assertTrue(child.resolve("x").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseValue(ValueLocation val) {
				return val.equals(val0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveParentVariableBlock() {
		Environment parent = new Environment();
		final VariableLocation var0 = parent.installVariable("y");
		Environment child = new Environment(parent, Type.BLOCK);
		assertTrue(child.resolve("y").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return var.equals(var0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveParentVariableFunction() {
		Environment parent = new Environment();
		final VariableLocation var0 = parent.installVariable("y");
		Environment child = new Environment(parent, Type.FUNCTION);
		assertTrue(child.resolve("y").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseInaccessibleVariable(VariableLocation var) {
				return var.equals(var0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveShadowedParentValueBlock() {
		Environment parent = new Environment();
		parent.installValue("x");
		Environment child = new Environment(parent, Type.BLOCK);
		final ValueLocation val0 = child.installValue("x");
		assertTrue(child.resolve("x").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseValue(ValueLocation val) {
				return val.equals(val0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveShadowedParentValueFunction() {
		Environment parent = new Environment();
		parent.installValue("x");
		Environment child = new Environment(parent, Type.FUNCTION);
		final ValueLocation val0 = child.installValue("x");
		assertTrue(child.resolve("x").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseValue(ValueLocation val) {
				return val.equals(val0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveShadowedParentVariableBlock() {
		Environment parent = new Environment();
		parent.installVariable("y");
		Environment child = new Environment(parent, Type.BLOCK);
		final VariableLocation var0 = child.installVariable("y");
		assertTrue(child.resolve("y").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return var.equals(var0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

	@Test
	public void resolveShadowedParentVariableFunction() {
		Environment parent = new Environment();
		parent.installVariable("y");
		Environment child = new Environment(parent, Type.FUNCTION);
		final VariableLocation var0 = child.installVariable("y");
		assertTrue(child.resolve("y").matchIdentifier(new AbstractMatcher<Boolean>() {
			@Override
			public Boolean caseVariable(VariableLocation var) {
				return var.equals(var0);
			}
			@Override
			public Boolean otherwise() {
				return false;
			}
		}));
	}

}
