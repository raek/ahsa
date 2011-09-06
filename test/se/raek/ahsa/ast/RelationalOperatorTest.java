package se.raek.ahsa.ast;

import static org.junit.Assert.*;

import org.junit.Test;

public class RelationalOperatorTest {

	@Test
	public void matchGreater() {
		assertTrue(RelationalOperator.GREATER.matchRelationalOperator(new RelationalOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseGreater() {
				return true;
			}
			@Override
			public Boolean caseLess() {
				return false;
			}
			@Override
			public Boolean caseGreaterEqual() {
				return false;
			}
			@Override
			public Boolean caseLessEqual() {
				return false;
			}
		}));
	}

	@Test
	public void matchLess() {
		assertTrue(RelationalOperator.LESS.matchRelationalOperator(new RelationalOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseGreater() {
				return false;
			}
			@Override
			public Boolean caseLess() {
				return true;
			}
			@Override
			public Boolean caseGreaterEqual() {
				return false;
			}
			@Override
			public Boolean caseLessEqual() {
				return false;
			}
		}));
	}

	@Test
	public void matchGreaterEqual() {
		assertTrue(RelationalOperator.GREATER_EQUAL.matchRelationalOperator(new RelationalOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseGreater() {
				return false;
			}
			@Override
			public Boolean caseLess() {
				return false;
			}
			@Override
			public Boolean caseGreaterEqual() {
				return true;
			}
			@Override
			public Boolean caseLessEqual() {
				return false;
			}
		}));
	}

	@Test
	public void matchLessEqual() {
		assertTrue(RelationalOperator.LESS_EQUAL.matchRelationalOperator(new RelationalOperator.Matcher<Boolean>() {
			@Override
			public Boolean caseGreater() {
				return false;
			}
			@Override
			public Boolean caseLess() {
				return false;
			}
			@Override
			public Boolean caseGreaterEqual() {
				return false;
			}
			@Override
			public Boolean caseLessEqual() {
				return true;
			}
		}));
	}
	
}
