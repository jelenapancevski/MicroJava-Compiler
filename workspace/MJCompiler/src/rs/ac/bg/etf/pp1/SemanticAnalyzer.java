package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor{
	
	// Printing info and error messages
	Logger log = Logger.getLogger(getClass());
	
	int nVars;
	boolean errorDetected = false;
	Struct boolType;
	Type currentType;
	
	public void SemanticAnalyzer() {
		
		// Inserting boolean type into table of symbols
		boolType = new Struct(Struct.Bool);
		Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
		
	}
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	
	 public void visit(Program Program) {
		 nVars = Tab.currentScope.getnVars();
	    	Tab.chainLocalSymbols(Program.getProgramIdentificator().obj);
	    	Tab.closeScope();
	 }
	 
	 // Opens scope for program and inserts program identificator into table of symbols
	 public void visit(ProgramIdentificator programIdentificator) {
		 programIdentificator.obj = Tab.insert(Obj.Prog, programIdentificator.getProgIdent(), Tab.noType);
	     Tab.openScope();
	 }
	 
	 // NumberConstant - returns type int
	  public void visit(NumberConstant numberConstant){
		  numberConstant.struct = Tab.intType;
	   }
	  
	  //CharConstant - returns type char
	  public void visit(CharConstant charConstant){
		  charConstant.struct = Tab.charType;
	   }
	  
	  //BoolConstant - returns type char
	  public void visit(BoolConstant boolConstant){
		  boolConstant.struct = boolType;
	   }
	  
	  // ConstantDeclaration -  IDENT:constName EQUAL Constant
	  public void visit (ConstantDeclaration constantDeclaration) {
		  // Check if constant name is taken 
		  if( Tab.find(constantDeclaration.getConstName())!=Tab.noObj) {
			  // Variable is already declared error
			  report_error("Greska na liniji "+ constantDeclaration.getLine()+" : promenljiva sa nazivom "+ constantDeclaration.getConstName()+"je vec deklarisana", null);
			  constantDeclaration.obj = Tab.noObj;
			  return;
		  }
		  
		  // Check if constant type value is equivalent with current type
		  if(!currentType.struct.equals(constantDeclaration.getConstant().struct)) {
			  // Constant value has wrong type
			  report_error("Greska na liniji "+ constantDeclaration.getLine()+" : promenljiva sa nazivom "+ constantDeclaration.getConstName()+"nije kompatibilnog tipa sa dodeljenom vrednoscu", null);
			  constantDeclaration.obj=Tab.noObj;
			  return;
		  }
		  
		  // Type is equivalent with current type adding new constant to table of symbols
		  constantDeclaration.obj= Tab.insert(Obj.Con, constantDeclaration.getConstName(), constantDeclaration.getConstant().struct);
		  
	  }
	  
	  public void visit (Type type) {
		  currentType=type;
		  Obj objType = Tab.find(type.getTypeName());
		  if (objType == Tab.noObj) {
			  // Type does not exist 
			  report_error("Greska na liniji "+ type.getLine()+" : tip sa nazivom "+ type.getTypeName()+"ne postoji", null);
			  type.struct = Tab.noType;
			  currentType.struct = Tab.noType;
			  return;
		  }
		  if(objType.getKind()!=Obj.Type) {
			  // Given type name does not present any type 
			  report_error("Greska na liniji "+ type.getLine()+" : tip sa nazivom "+ type.getTypeName()+"ne predstavlja tip", null);
			  type.struct = Tab.noType;
			  currentType.struct = Tab.noType;
			  return;

		  }
		  type.struct = objType.getType();
		  
	  }
	 
}
