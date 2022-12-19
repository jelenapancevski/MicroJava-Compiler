package rs.ac.bg.etf.pp1;

public class sym_old {
	// EOF
	public static final int EOF = 0;

	// Keywords
	public static final int PROG = 1;
	public static final int BREAK = 2;
	public static final int CLASS = 3;
	public static final int ENUM = 4;
	public static final int ELSE = 5;
	public static final int CONST = 6;
	public static final int IF = 7;
	public static final int DO = 8;
	public static final int WHILE = 9;
	public static final int NEW = 10;
	public static final int PRINT = 11;
	public static final int READ = 12;
	public static final int RETURN = 13;
	public static final int VOID = 14;
	public static final int EXTENDS = 15;
	public static final int CONTINUE = 16;
	public static final int THIS = 17;
	public static final int FOREACH = 18;

//Identifiers

	// ident = letter {letter | digit | "_"}.
	public static final int IDENT = 19;

	// numConst = digit {digit}.
	public static final int NUMCONST = 20;

	// charConst = "'" printableChar "'".
	public static final int CHARCONST = 21;

	// boolConst = ("true" | "false")
	public static final int BOOLCONST = 22;

	// +, â€�, *, /, %, ==, !=, >, >=, <, <=, &&, ||, =, ++, â€�â€�, ;, :, zarez, ., (, ),
	// [, ], {, }, =>

	// Operators
	public static final int PLUS = 23;
	public static final int MINUS = 24;
	public static final int MULTIPLY = 25;
	public static final int DIVIDE = 26;
	public static final int MODUO = 27;
	public static final int ISEQUAL = 28;
	public static final int NOTEQUAL = 29;
	public static final int GT = 30;
	public static final int GTE = 31;
	public static final int LT = 32;
	public static final int LTE = 33;
	public static final int AND = 34;
	public static final int OR = 35;
	public static final int EQUAL = 36;
	public static final int INCREMENT = 37;
	public static final int DECREMENT = 38;
	public static final int SEMICOLON = 39;
	public static final int COLON = 40;
	public static final int COMMA = 41;
	public static final int DOT = 42;
	public static final int LPAREN = 43;
	public static final int RPAREN = 44;
	public static final int LBRACKET = 45;
	public static final int RBRACKET = 46;
	public static final int LBRACE = 47;
	public static final int RBRACE = 48;
	public static final int ARROW = 49;

	// public static final int error = 50; //Is it important?

}