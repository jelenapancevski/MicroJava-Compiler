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
	/*Struct boolArray;
	Struct charArray;
	Struct intArray;*/
	Type currentType;
	Obj currentMethod;
	int methodArguments = 0;
	public SemanticAnalyzer() {
		// Inserting boolean type into table of symbols
		boolType = new Struct(Struct.Bool);
		Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
		
		/*// Creating boolean Array struct
		boolArray = new Struct(Struct.Array);
		boolArray.setElementType(boolType);
		
		// Creating char Array struct 
		charArray = new Struct(Struct.Array);
		charArray.setElementType(Tab.charType);
		
		// Creating int Array struct 
		intArray = new Struct(Struct.Array);
		intArray.setElementType(Tab.intType);
		*/
		
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
	 
	 
	 /* DECLARATIONS OF CONSTANTS */
	 
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
	  
	  // ConstantDeclaration - IDENT:constName EQUAL Constant
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
		  // Set value for constant in table of symbols 
		  switch(constantDeclaration.obj.getType().getKind()) {
		  // Int
		  case 1:
			  NumberConstant numberConst= (NumberConstant)constantDeclaration.getConstant();
			  constantDeclaration.obj.setAdr(numberConst.getNumConstant());
			  report_info("Deklarisana konstanta "+ constantDeclaration.getConstName() + " jednaka "+ numberConst.getNumConstant(), constantDeclaration);
			  break;
		  // Char	  
		  case 2:
			  CharConstant charConst= (CharConstant)constantDeclaration.getConstant();
			  constantDeclaration.obj.setAdr(charConst.getCharConstant());
			  report_info("Deklarisana konstanta "+ constantDeclaration.getConstName() + " jednaka "+ charConst.getCharConstant(), constantDeclaration);
			  break;
		  // Bool	  
		  case 5:
			  BoolConstant boolConst= (BoolConstant)constantDeclaration.getConstant();
			  report_info("Deklarisana konstanta "+ constantDeclaration.getConstName() + " jednaka "+ boolConst.getBoolConstant(), constantDeclaration);
			  if( boolConst.getBoolConstant()) {
				  // TRUE
				  constantDeclaration.obj.setAdr(1);
			  }else {
				  // FALSE
				  constantDeclaration.obj.setAdr(0);
			  }
			 
			  break;
		  }
		

	  }
	  
	  /* DECLARATION OF VARIABLES */
	  
	  // Variable - IDENT:varName ArrayBrackets:isArray 
	  public void visit (Variable variable) {
		  // Check if variable name is taken
		  if( Tab.find(variable.getVarName())!=Tab.noObj) {
			  // Variable is already declared error
			  report_error("Greska na liniji "+ variable.getLine()+" : promenljiva sa nazivom "+ variable.getVarName()+"je vec deklarisana", null);
			  variable.obj = Tab.noObj;
			  return;
		  }
		  
		  if (variable.getArrayBrackets() instanceof HasArrayBrackets) {
			  // Array declared
			  variable.obj = Tab.insert(Obj.Var, variable.getVarName(), new Struct(Struct.Array, currentType.struct));
			  report_info("Deklarisan niz "+ variable.getVarName(), variable);

		  }
		  else {
			  // Variable declared
			  variable.obj = Tab.insert(Obj.Var, variable.getVarName(), currentType.struct);
			  report_info("Deklarisana promenljiva "+ variable.getVarName(), variable);

		  }
	  }
	  
	 
	  
	  /* METHODS DECLARATIONS */

	  // (MethodDecl) MethodTypeName LPAREN MethodParameterList RPAREN MethodVariableList LBRACE MethodStatementList RBRACE;
	  public void visit (MethodDecl methodDecl) {
		  // Set number of method arguments 
		  currentMethod.setLevel(methodArguments);
		  report_info("Deklarisana funkcija "+ currentMethod.getName() + " sa "+ currentMethod.getLevel()+" ulazna parametra" , null);

		  Tab.chainLocalSymbols(methodDecl.getMethodTypeName().obj);
		  currentMethod = null;
		 // returnFound= false;
		  Tab.closeScope(); 

		/*
		  if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
    	}
		
		*/
	  }
	  
	  // (ReturnMethod) Type:returnType IDENT:methodName 
	  public void visit (ReturnMethod returnMethod) {
		  returnMethod.obj = Tab.insert(Obj.Meth, returnMethod.getMethodName(), returnMethod.getType().struct);
		  currentMethod = returnMethod.obj;
		  Tab.openScope();
		  report_info("Deklarisana funkcija "+ returnMethod.getMethodName() + " povratna vrednost je tipa "+ returnMethod.getType().getTypeName(), returnMethod);
	  }
	  
	  // (VoidMethod) VOID IDENT:methodName
	  public void visit (VoidMethod voidMethod) {
		  voidMethod.obj = Tab.insert(Obj.Meth, voidMethod.getMethodName(), Tab.noType );
		  currentMethod = voidMethod.obj;
		  methodArguments = 0;
		  Tab.openScope();
		  report_info("Deklarisana funkcija "+ voidMethod.getMethodName() + " povratna vrednost je tipa VOID", voidMethod);
	  }
	  
	  // (OneParameter) Type:argumentType IDENT:argumentName ArrayBrackets:isArray ;
	  public void visit (OneParameter oneParameter) {
		  methodArguments++;
		  if (oneParameter.getArrayBrackets() instanceof HasArrayBrackets) {
			  // Array argument
			  oneParameter.obj = Tab.insert(Obj.Var, oneParameter.getArgumentName(), new Struct(Struct.Array, oneParameter.getType().struct));
			  report_info(methodArguments+". argument "+ oneParameter.getArgumentName()+" niz tipa "+oneParameter.getType().getTypeName(), oneParameter);

		  }
		  else {
			  // Variable argument
			  oneParameter.obj = Tab.insert(Obj.Var, oneParameter.getArgumentName(), oneParameter.getType().struct);
			  report_info(methodArguments+". argument "+ oneParameter.getArgumentName()+" tipa "+oneParameter.getType().getTypeName(), oneParameter);

		  }
	  }
	  
}
