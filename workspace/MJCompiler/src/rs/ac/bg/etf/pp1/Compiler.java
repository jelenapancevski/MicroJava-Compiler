package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.CodeGenerator.IfElseCondition;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void tsdump() {
		Tab.dump();
	}
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		Logger log = Logger.getLogger(Compiler.class);
		
		Reader br = null;
		try {

			if(args.length<2) {
				log.error("Potrebno je uneti naziv ulaznog file-a");
				return;
			}
			
			File sourceCode = new File(args[0]);  
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			//Sintaksna analiza
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  
			//Symbol s= p.debug_parse();
	        
	        Program prog = (Program)(s.value); 
	        Tab.init();
			// ispis sintaksnog stabla
			log.info(prog.toString(""));
			log.info("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v); 
			
			log.info("===================================");
			
			tsdump();
			
			if (!p.errorDetected && v.passed()) {
				// Faza generisanja koda
				File objFile = new File(args[1]);
				if(objFile.exists()) objFile.delete();
				
				CodeGenerator codeGen = new CodeGenerator();
				prog.traverseBottomUp(codeGen);
				Code.dataSize = v.nVars;
				Code.mainPc = codeGen.getMainPC();
				Code.write(new FileOutputStream(objFile));
				log.info("Parsiranje koda je uspesno");
			}
			else {
				log.error("Parsiranje koda je neuspesno");
			}
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}

}
