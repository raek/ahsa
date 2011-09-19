grammar Ahsa;

options {
  language = Java;
}

@parser::header {
package se.raek.ahsa.parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.ast.Expression;
import se.raek.ahsa.ast.ValueLocation;
import se.raek.ahsa.ast.VariableLocation;
import se.raek.ahsa.ast.EqualityOperator;
import se.raek.ahsa.ast.RelationalOperator;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.runtime.Value;
}

@lexer::header {
package se.raek.ahsa.parser;
}

@parser::members {

private EnvironmentStack envStack = new EnvironmentStack();

public void resetEnvironment(Environment env) {
  envStack.resetEnvironment(env);
}

}

program
	: statements
	;

statements returns [List<Statement> stmts]
  @init{ $stmts = new ArrayList<Statement>(); }
	: (s=statement { $stmts.addAll($s.stmts); })*
	;

statement returns [List<Statement> stmts]
  @init{ $stmts = new ArrayList<Statement>(); }
	: e=expression ';' { $stmts.add(Statement.ThrowawayExpression.make($e.expr)); }
	| { Expression cond; List<Statement> thenStmts, elseStmts = null; }
	  'if'
	  c=expression       { cond = $c.expr; }
	  '{'
	  thens=statements   { thenStmts = $thens.stmts; }
	  '}'
	  ('else'
	    '{'
	    elses=statements { elseStmts = $elses.stmts; }
	    '}')?
	  { if (elseStmts == null) {
	        elseStmts = Collections.emptyList();
	    } 
	    $stmts.add(Statement.Conditional.make(cond, thenStmts, elseStmts));
	  }
	| 'val' ID '=' expression ';' {
	    ValueLocation val = envStack.getCurrent().installValue($ID.text);
	    $stmts.add(Statement.ValueDefinition.make(val, $expression.expr));
	  }
	| { Expression expr = null; }
	  'var' ID
	  ('=' expression
	    { expr = $expression.expr; }
	  )?
	  ';'
	  {
	    VariableLocation var = envStack.getCurrent().installVariable($ID.text);
	    if (expr != null) {
	      $stmts.add(Statement.VariableAssignment.make(var, $expression.expr));
	    }
	  }
  | ID '=' expression ';'
    {
      final String label = $ID.text;
      Identifier id = envStack.getCurrent().resolve(label);
      VariableLocation var = id.matchIdentifier(new Identifier.Matcher<VariableLocation>() {
        @Override
        public VariableLocation caseUnbound() {
          throw new RuntimeException("Unbound identifier: " + label);
        }
        @Override
        public VariableLocation caseValue(ValueLocation val) {
          throw new RuntimeException("Cannot assign named value: " + label);
        }
        @Override
        public VariableLocation caseVariable(VariableLocation var) {
          return var;
        }
        @Override
        public VariableLocation caseInaccessibleVariable(VariableLocation var) {
          throw new RuntimeException("Variable inaccessible from here: " + var.label);
        }
      });
      $stmts.add(Statement.VariableAssignment.make(var, $expression.expr));
    }
  | '{'        { envStack.enterScope(Environment.Type.BLOCK); }
    statements { $stmts = $statements.stmts; }
    '}'        { envStack.exitScope(); }
	;

expression returns [Expression expr]
  : e1=expr1           { $expr = $e1.expr; }
    (op=eq_op e2=expr1 { $expr = Expression.EqualityOperation.make($op.op, $expr, $e2.expr); } 
    )*
  ;

expr1 returns [Expression expr]
  : e1=expr2            { $expr = $e1.expr; }
    (op=rel_op e2=expr2 { $expr = Expression.RelationalOperation.make($op.op, $expr, $e2.expr); } 
    )*
  ;

expr2 returns [Expression expr]
	: e1=expr3            { $expr = $e1.expr; }
		(op=add_op e2=expr3 { $expr = Expression.ArithmeticOperation.make($op.op, $expr, $e2.expr); } 
		)*
	;

expr3 returns [Expression expr]
	: e1=expr4				  	{ $expr = $e1.expr; }
		(op=mul_op e2=expr4 { $expr = Expression.ArithmeticOperation.make($op.op, $expr, $e2.expr); } 
		)*
	;

expr4 returns [Expression expr]
	: c=constant		       { $expr = Expression.Constant.make($c.v); }
	| l=lookup             { $expr = $l.expr; }
	| '(' e=expression ')' { $expr = $e.expr; }
	;

constant returns [Value v]
	: null_literal      { $v = Value.Null.make(); }
	| b=boolean_literal { $v = Value.Boolean.make($b.b); }
	| n=number_literal  { $v = Value.Number.make($n.n); }
	;

lookup returns [Expression expr]
  : ID {
      final String label = $ID.text;
      Identifier id = envStack.getCurrent().resolve(label);
      $expr = id.matchIdentifier(new Identifier.Matcher<Expression>() {
        @Override
        public Expression caseUnbound() {
          throw new RuntimeException("Unbound identifier: " + label);
        }
        @Override
        public Expression caseValue(ValueLocation val) {
          return Expression.ValueLookup.make(val);
        }
        @Override
        public Expression caseVariable(VariableLocation var) {
          return Expression.VariableLookup.make(var);
        }
        @Override
        public Expression caseInaccessibleVariable(VariableLocation var) {
          throw new RuntimeException("Variable inaccessible from here: " + var.label);
        }
      });
    }
  ;

eq_op returns [EqualityOperator op]
  : '==' { $op = EqualityOperator.EQUAL; }
  | '!=' { $op = EqualityOperator.UNEQUAL; }
  ;

rel_op returns [RelationalOperator op]
  : '>'  { $op = RelationalOperator.GREATER; }
  | '<'  { $op = RelationalOperator.LESS; }
  | '>=' { $op = RelationalOperator.GREATER_EQUAL; }
  | '<=' { $op = RelationalOperator.LESS_EQUAL; }
  ;

add_op returns [ArithmeticOperator op]
	: '+' { $op = ArithmeticOperator.ADDITION; }
	| '-' { $op = ArithmeticOperator.SUBTRACTION; }
	;

mul_op returns [ArithmeticOperator op]
	: '*' { $op = ArithmeticOperator.MULTIPLICATION; }
	| '/' { $op = ArithmeticOperator.DIVISION; }
	;

null_literal: 'null' ;

boolean_literal returns [boolean b]
	: 'true'	{ $b = true; }
	| 'false'	{ $b = false; }
	;

number_literal returns [double n]
	: NUMBER { $n = Double.valueOf($NUMBER.text); } 
	;

NUMBER: ('0'|'1'..'9' ('0'..'9')*)('.' ('0'..'9')*)?;

ID: ('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'0'..'9'|'_')* ;

WS: (' '|'\t'|'\r'|'\n')+ { $channel = HIDDEN; } ;