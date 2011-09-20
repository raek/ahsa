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

import static se.raek.ahsa.ast.Expression.*;
import static se.raek.ahsa.ast.ArithmeticOperator.*;
import static se.raek.ahsa.ast.EqualityOperator.*;
import static se.raek.ahsa.ast.RelationalOperator.*;
import static se.raek.ahsa.ast.Statement.*;
import static se.raek.ahsa.runtime.Value.*;

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
	: e=expression ';' { $stmts.add(makeThrowawayExpression($e.expr)); }
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
	    $stmts.add(makeConditional(cond, thenStmts, elseStmts));
	  }
	| 'val' ID '=' expression ';' {
	    ValueLocation val = envStack.getCurrent().installValue($ID.text);
	    $stmts.add(makeValueDefinition(val, $expression.expr));
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
	      $stmts.add(makeVariableAssignment(var, $expression.expr));
	    }
	  }
  | ID '=' expression ';'
    {
      final String label = $ID.text;
      Identifier id = envStack.getCurrent().resolve(label);
      VariableLocation var = id.matchIdentifier(new Identifier.Matcher<VariableLocation>() {
        public VariableLocation caseUnbound() {
          throw new RuntimeException("Unbound identifier: " + label);
        }
        public VariableLocation caseValue(ValueLocation val) {
          throw new RuntimeException("Cannot assign named value: " + label);
        }
        public VariableLocation caseVariable(VariableLocation var) {
          return var;
        }
        public VariableLocation caseInaccessibleVariable(VariableLocation var) {
          throw new RuntimeException("Variable inaccessible from here: " + var.label);
        }
      });
      $stmts.add(makeVariableAssignment(var, $expression.expr));
    }
  | '{'        { envStack.enterScope(Environment.Type.BLOCK); }
    statements { $stmts = $statements.stmts; }
    '}'        { envStack.exitScope(); }
  | 'return' expression ';' { $stmts.add(makeReturn($expression.expr)); }
	;

expressions returns [List<Expression> exprs]
  @init{ $exprs = new ArrayList<Expression>(); }
  : (e1=expression { $exprs.add($e1.expr); }
      (',' e2=expression { $exprs.add($e2.expr); } )*
      )?
  ;

expression returns [Expression expr]
  : e1=expr1           { $expr = $e1.expr; }
    (op=eq_op e2=expr1 { $expr = makeEqualityOperation($op.op, $expr, $e2.expr); } 
    )*
  ;

expr1 returns [Expression expr]
  : e1=expr2            { $expr = $e1.expr; }
    (op=rel_op e2=expr2 { $expr = makeRelationalOperation($op.op, $expr, $e2.expr); } 
    )*
  ;

expr2 returns [Expression expr]
	: e1=expr3            { $expr = $e1.expr; }
		(op=add_op e2=expr3 { $expr = makeArithmeticOperation($op.op, $expr, $e2.expr); } 
		)*
	;

expr3 returns [Expression expr]
	: e1=expr4				  	{ $expr = $e1.expr; }
		(op=mul_op e2=expr4 { $expr = makeArithmeticOperation($op.op, $expr, $e2.expr); } 
		)*
	;

expr4 returns [Expression expr]
  : e=expr5                 { $expr = $e.expr; }
    ('(' es=expressions ')' { $expr = makeFunctionApplication($expr, $es.exprs); }
    )*
  ;

expr5 returns [Expression expr]
	: c=constant		       { $expr = makeConstant($c.v); }
	| l=lookup             { $expr = $l.expr; }
	| fn=lambda            { $expr = $fn.expr; }
	| '(' e=expression ')' { $expr = $e.expr; }
	;

constant returns [Value v]
	: null_literal      { $v = makeNull(); }
	| b=boolean_literal { $v = makeBoolean($b.b); }
	| n=number_literal  { $v = makeNumber($n.n); }
	;

lookup returns [Expression expr]
  : ID {
      final String label = $ID.text;
      Identifier id = envStack.getCurrent().resolve(label);
      $expr = id.matchIdentifier(new Identifier.Matcher<Expression>() {
        public Expression caseUnbound() {
          throw new RuntimeException("Unbound identifier: " + label);
        }
        public Expression caseValue(ValueLocation val) {
          return makeValueLookup(val);
        }
        public Expression caseVariable(VariableLocation var) {
          return makeVariableLookup(var);
        }
        public Expression caseInaccessibleVariable(VariableLocation var) {
          throw new RuntimeException("Variable inaccessible from here: " + var.label);
        }
      });
    }
  ;

lambda returns [Expression expr]
  : 'fn'             { envStack.enterScope(Environment.Type.BLOCK); }
    '('
    ps=parameters
    ')'
    '{'
    stmts=statements { $expr = makeFunctionAbstraction($ps.vals, $stmts.stmts); }
    '}'              { envStack.exitScope(); }
  ;

parameters returns [List<ValueLocation> vals]
  @init{ $vals = new ArrayList<ValueLocation>(); }
  : (p1=parameter       { vals.add($p1.val); }
      (',' p2=parameter { vals.add($p2.val); }
      )*
    )?
  ;

parameter returns [ValueLocation val]
  : ID { $val = envStack.getCurrent().installValue($ID.text); }
  ;

eq_op returns [EqualityOperator op]
  : '==' { $op = EQUAL; }
  | '!=' { $op = UNEQUAL; }
  ;

rel_op returns [RelationalOperator op]
  : '>'  { $op = GREATER; }
  | '<'  { $op = LESS; }
  | '>=' { $op = GREATER_EQUAL; }
  | '<=' { $op = LESS_EQUAL; }
  ;

add_op returns [ArithmeticOperator op]
	: '+' { $op = ADDITION; }
	| '-' { $op = SUBTRACTION; }
	;

mul_op returns [ArithmeticOperator op]
	: '*' { $op = MULTIPLICATION; }
	| '/' { $op = DIVISION; }
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