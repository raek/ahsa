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
import se.raek.ahsa.ast.EqualityOperator;
import se.raek.ahsa.ast.RelationalOperator;
import se.raek.ahsa.ast.ArithmeticOperator;
import se.raek.ahsa.runtime.Value;
}

@lexer::header {
package se.raek.ahsa.parser;
}

program
	: statements
	;

statements returns [List<Statement> stmts]
  @init{ $stmts = new ArrayList<Statement>(); }
	: (s=statement { $stmts.add($s.stmt); })*
	;

statement returns [Statement stmt]
	: e=expression ';' { $stmt = Statement.ThrowawayExpression.make($e.expr); }
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
	    $stmt = Statement.Conditional.make(cond, thenStmts, elseStmts);
	  }
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
	| '(' e=expression ')' { $expr = $e.expr; }
	;

constant returns [Value v]
	: null_literal		{ $v = Value.Null.make(); }
	| b=boolean_literal	{ $v = Value.Boolean.make($b.b); }
	| n=number_literal	{ $v = Value.Number.make($n.n); }
	;

eq_op returns [EqualityOperator op]
  : '=='  { $op = EqualityOperator.EQUAL; }
  | '!='  { $op = EqualityOperator.UNEQUAL; }
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

ID: ('a'..'z'|'A'..'Z')+ ;

WS: (' '|'\t'|'\r'|'\n')+ {skip();} ;