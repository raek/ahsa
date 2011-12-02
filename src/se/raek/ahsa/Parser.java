package se.raek.ahsa;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import se.raek.ahsa.ast.Statement;
import se.raek.ahsa.parser.AhsaLexer;
import se.raek.ahsa.parser.AhsaParser;

public class Parser {
	
	public static List<Statement> parseProgram(String s) throws RecognitionException {
		CharStream input = new ANTLRStringStream(s); 
		AhsaLexer lexer = new AhsaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		AhsaParser parser = new AhsaParser(tokens);
		return parser.statements();
	}

}
