package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public class SemanticAnalyzer extends VisitorAdaptor{
	
	// Printing info and error messages
	Logger log = Logger.getLogger(getClass());
	
	int nVars;
	int inloop = 0;
	boolean errorDetected = false;
	boolean returnFound;
	boolean inMethod = false; // mozda mi ne treba
	Struct boolType;
	Type currentType;
	Obj currentMethod;
	Obj currentFunction;
	int methodArguments = 0;
	//int parameters;
	ArrayList <Integer> parameters = new ArrayList<>();
	ArrayList <OneDesignatorElement> designatorList = null;
	
	public SemanticAnalyzer() {
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
	
	public void report_detection(Obj objectNode,SyntaxNode info) {
		StringBuilder msg = new StringBuilder(); 
		msg.append("Detektovana upotreba simbola "+objectNode.getName()+" na liniji "+ info.getLine() + ": ");
		SymbolTableVisitor visitor = new DumpSymbolTableVisitor();
		visitor.visitObjNode(objectNode);
		msg.append(visitor.getOutput());
		log.info(msg.toString());
	}
	
	public boolean passed() {
		return !errorDetected;
	}
	
	 public void visit (Type type) {
		  currentType=type;
		  Obj objType = Tab.find(type.getTypeName());
		  if (objType == Tab.noObj) {
			  // Type does not exist 
			  report_error("Greska na liniji "+ type.getLine()+" : tip sa nazivom "+ type.getTypeName()+" ne postoji", null);
			  type.struct = Tab.noType;
			  currentType.struct = Tab.noType;
			  return;
		  }
		  if(objType.getKind()!=Obj.Type) {
			  // Given type name does not present any type 
			  report_error("Greska na liniji "+ type.getLine()+" : promenljiva sa nazivom "+ type.getTypeName()+" ne predstavlja tip", null);
			  type.struct = Tab.noType;
			  currentType.struct = Tab.noType;
			  return;

		  }
		  type.struct = objType.getType();
		  
	  }
	
	
	 public void visit(Program Program) {
		 	nVars = Tab.currentScope.getnVars();
		 	
		 	// Check if method main exists and has no formal arguments and is VOID
		 	Obj mainMethod = Tab.find("main");
	    	if((mainMethod == Tab.noObj) || mainMethod.getKind()!=3) {
				  report_error("Greska: Metoda main nije deklarisana u programu", null);
	    	}
	    	// Has arguments
	    	else if (mainMethod.getLevel()!=0) {
				  report_error("Greska: Metoda main ne sme imati formalne argumente "+ mainMethod.getLevel(), null);
	    	}
	    	// Is void
	    	else if(!mainMethod.getType().equals(Tab.noType)) {
				  report_error("Greska: Metoda main ne sme imati povratnu vrednost, mora biti tipa VOID", null);
	   
	    	}
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
			  report_error("Greska na liniji "+ constantDeclaration.getLine()+" : promenljiva sa nazivom "+ constantDeclaration.getConstName()+" nije kompatibilnog tipa sa dodeljenom vrednoscu", null);
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
	  public void visit (VariableDecl variable) {
		  // Check if variable name is taken
		  if( Tab.currentScope().findSymbol(variable.getVarName())!=null) {
			  // Variable is already declared error
			  report_error("Greska na liniji "+ variable.getLine()+" : promenljiva sa nazivom "+ variable.getVarName()+"je vec deklarisana", null);
			  variable.obj = Tab.noObj;
			  return;
		  }
		  
		 if( (currentType == null) || (currentType.struct) == Tab.noType) {
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
		  if(!inMethod) {
			  this.nVars++;
		  }
	  }
	  
	 
	  
	  /* METHODS DECLARATIONS */

	  // (MethodDecl) MethodTypeName LPAREN MethodParameterList RPAREN MethodVariableList LBRACE MethodStatementList RBRACE;
	  public void visit (MethodDecl methodDecl) {
		  if(methodDecl.getMethodTypeName().obj==Tab.noObj) {
			  // Error
			  currentMethod = null;
			  inMethod = false;
			  methodArguments = 0;
			  return;
		  }
		  
		  // Set number of method arguments 
		  currentMethod.setLevel(methodArguments);
		
		  if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
		  }
		  else {
			  report_info("Deklarisana funkcija "+ currentMethod.getName() + " sa "+ currentMethod.getLevel()+" ulazna parametra" , methodDecl);	  
		  }
		  
		  Tab.chainLocalSymbols(methodDecl.getMethodTypeName().obj);
		  currentMethod = null;
		  inMethod = false;
		  methodArguments = 0; 
		 // returnFound= false;
		  Tab.closeScope(); 
	
	  }
	  
	  // (ReturnMethod) Type:returnType IDENT:methodName 
	  public void visit (ReturnMethod returnMethod) {
		  if (Tab.currentScope().findSymbol(returnMethod.getMethodName())!=null) {
			  report_error("Greska na liniji "+ returnMethod.getLine()+" : Ime "+ returnMethod.getMethodName()+" vec postoji unutar istog opsega", null);
			  returnMethod.obj = Tab.noObj;
			  return;
		  }
		  returnFound = false;
		  inMethod = true;
		  methodArguments = 0;
		  
		  returnMethod.obj = Tab.insert(Obj.Meth, returnMethod.getMethodName(), returnMethod.getType().struct);
		  currentMethod = returnMethod.obj;
		  
		  Tab.openScope();
		  report_info("Deklarisana funkcija "+ returnMethod.getMethodName() + " povratna vrednost je tipa "+ returnMethod.getType().getTypeName(), returnMethod);
	  }
	  
	  // (VoidMethod) VOID IDENT:methodName
	  public void visit (VoidMethod voidMethod) {
		  if (Tab.currentScope().findSymbol(voidMethod.getMethodName())!=null) {
			  report_error("Greska na liniji "+ voidMethod.getLine()+" : Ime "+ voidMethod.getMethodName()+" vec postoji unutar istog opsega", null);
			  voidMethod.obj = Tab.noObj;
			  return;
		  }
		  returnFound = false;
		  inMethod = true;
		  methodArguments = 0;
		  
		  voidMethod.obj = Tab.insert(Obj.Meth, voidMethod.getMethodName(), Tab.noType );
		  currentMethod = voidMethod.obj;
		  
		  Tab.openScope();
		  report_info("Deklarisana funkcija "+ voidMethod.getMethodName() + " povratna vrednost je tipa VOID", voidMethod);
	  }
	  
	  // (OneParameter) Type:argumentType IDENT:argumentName ArrayBrackets:isArray ;
	  public void visit (OneParameter oneParameter) {
		 if( currentMethod == null) return;
		 
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
	  
	  
	  /* DESIGNATOR STATEMENTS */
	  /*
	   *  Statement ::= (DesignatorStmt) DesignatorStatement SEMICOLON
					
		DesignatorList ::= (Designators) DesignatorList COMMA DesignatorListElement 
				   |
				   (OneDesignatorListElement) DesignatorListElement
				   ;	
				   			
		DesignatorListElement ::= (OneDesignatorElement)Designator 
						  |
						  (NoDesignatorElement)
						  ;			  
						 
	   * */
	  
	  //designatorList
	  
	  //NoDesignatorElement
	  public void visit (NoDesignatorElement noDesignatorElement) {
		  if (designatorList==null) {
			  designatorList = new ArrayList<>();
		  }
		  designatorList.add(null); // just comma added skip element of array
	  }
	  
	  //(OneDesignatorElement)Designator
	  public void visit (OneDesignatorElement oneDesignatorElement) {
		  if(oneDesignatorElement.getDesignator().obj == Tab.noObj) return;
			  // Check if it is a variable or array element
			  if((oneDesignatorElement.getDesignator().obj.getKind()!=Obj.Var) && (oneDesignatorElement.getDesignator().obj.getKind()!=Obj.Elem)){
				  report_error("Semanticka greska na liniji " + oneDesignatorElement.getLine() + ": Designator " + oneDesignatorElement.getDesignator().obj.getName()+" mora predstavljati promenljivu ili element niza", null);  
			  }
			  
		  if (designatorList==null) {
			  designatorList = new ArrayList<>();
		  }
		  designatorList.add(oneDesignatorElement);
	  }
	  
	  //(MultipleAssignment) LBRACKET DesignatorList RBRACKET EQUAL Designator
	  public void visit (MultipleAssignment multipleAssignment) {
		  if(multipleAssignment.getMultipleDesignator() instanceof MultipleAssignmentError) {
			  designatorList = null;
			  return;}
		  if(((MultipleAssignmentDesignator)multipleAssignment.getMultipleDesignator()).getDesignator().obj==Tab.noObj) {
			  designatorList = null;
			  return;
		  }
		  // Check if designator is type array 
		  if (((MultipleAssignmentDesignator)multipleAssignment.getMultipleDesignator()).getDesignator().obj.getType().getKind()!=Struct.Array) {
			  report_error("Semanticka greska na liniji " + multipleAssignment.getLine() + ": Designator "+((MultipleAssignmentDesignator)multipleAssignment.getMultipleDesignator()).getDesignator().obj.getName()+ " mora predstavljati niz", null);
			  designatorList=null;
			  return;
		  }
		  Struct elemType = ((MultipleAssignmentDesignator)multipleAssignment.getMultipleDesignator()).getDesignator().obj.getType().getElemType();
		  
		  
		  for (OneDesignatorElement designator :designatorList ) {
			  if(designator==null) continue;
			  if(designator.getDesignator().obj==Tab.noObj) continue;
			  if(!designator.getDesignator().obj.getType().compatibleWith(elemType)) {
				  report_error("Semanticka greska na liniji " + multipleAssignment.getLine() + ": Tip designatora "+designator.getDesignator().obj.getName()+ " nije kompatibilan sa tipom elementa niza "+((MultipleAssignmentDesignator)multipleAssignment.getMultipleDesignator()).getDesignator().obj.getName(), null);
			  }
		  }
		  designatorList=null;
		  
	  }
	  public void checkPostOp(Designator designator, SyntaxNode node) {
		  Struct type;
		  if(designator instanceof OneDesignator) {
			  // Check if designator is a variable 
			  Obj obj = Tab.find(((OneDesignator)designator).getDesignatorName());
			  if(obj == Tab.noObj) return;
			  if(obj.getKind()!=Obj.Var) {
				  report_error("Semanticka greska na liniji " + node.getLine() + ": Designator mora predstavljati promenljivu ili element niza", null);
				  return;
			  }
			  type = obj.getType();
		  }
		  else {
			  Obj obj = Tab.find(((ArrayDesignator)designator).getDesignatorName().getDesignatorName());
			  if(obj == Tab.noObj) return;
			  type = obj.getType().getElemType();
		  }
		  // check if type is equal to int 
		  if (type.equals(Tab.intType)) return; 	
		  if ((type.getKind() == Struct.Array)) {
			  report_error("Semanticka greska na liniji " + node.getLine() + ": Designator mora predstavljati promenljivu ili element niza", null);
			  return;
		  }
		  report_error("Semanticka greska na liniji " + node.getLine() + ": Designator mora biti tipa int", null);
	  }
	  
	  // (ValueIncrement) Designator INCREMENT 
	  public void visit (ValueIncrement valueIncrement) {
		  checkPostOp(valueIncrement.getDesignator(), valueIncrement);
	  }
	  // (ValueDecrement) Designator DECREMENT
	  public void visit (ValueDecrement valueDecrement) {
		  checkPostOp(valueDecrement.getDesignator(), valueDecrement);
	  }
	  
	 /* // AssignmentExpr ::= (AssignmentExpression) Expr
	  public void visit (AssignmentExpression assignmentExpression) {
		  assignmentExpression.struct = assignmentExpression.getExpr().struct;
	  }*/
	  //(Assignment) Designator EQUAL AssignmentExpr
	  public void visit (Assignment assignment) {
		  Struct type;
		  if( assignment.getAssignmentExpr() instanceof AssignmentExprError) return;
		  if((assignment.getDesignator().obj==Tab.noObj)||(((AssignmentExpression)assignment.getAssignmentExpr()).getExpr().struct==Tab.noType))  {
			  return;
		  } 
		  if(assignment.getDesignator() instanceof OneDesignator) {
			  // Check if designator is a variable 
			  Obj obj = Tab.find(((OneDesignator)assignment.getDesignator()).getDesignatorName());
			  if(obj.getKind()!=Obj.Var) {
				  report_error("Semanticka greska na liniji " + assignment.getLine() + ": Designator mora predstavljati promenljivu ili element niza", null);
				  return;
			  }
			  type = obj.getType();
		  }
		  else {
			  Obj obj = Tab.find(((ArrayDesignator)assignment.getDesignator()).getDesignatorName().getDesignatorName());
			  type = obj.getType().getElemType();
		  }
		  // check if type Expr is compatible with type of designator
		  if (((AssignmentExpression)assignment.getAssignmentExpr()).getExpr().struct.compatibleWith(type)) return; 		
		  report_error("Semanticka greska na liniji " + assignment.getLine() + ": Expr nije kompatibilan sa tipom designator-a pri dodeli vrednosti", null);
	  }
	  
	 //(FuncionCall) FunctionName LPAREN ActParams RPAREN
	  public void visit (FuncionCall funcionCall) {
		  int currentparameters = parameters.get(parameters.size()-1);
		  parameters.remove(parameters.size()-1);
		  if (funcionCall.getFunctionName().obj==Tab.noObj) {
			  	//funcionCall.struct = Tab.noType;
				 return;
			 }
		  if (funcionCall.getFunctionName().obj.getLevel()!=currentparameters) {
				 report_error("Semanticka greska na liniji " + funcionCall.getLine() + ": Broj formalnih i stvarnih argumenata metode "+ funcionCall.getFunctionName().obj.getName()+" nije isti!", null);
				 //funcionCall.struct = Tab.noType;
				 return;
			 }
		  //funcionCall.struct = funcionCall.getFunctionName().obj.getType();
	  }
	  
	  /* STATEMENTS */
	  
	  
	  //(IfElseStatement) IF LPAREN Condition RPAREN Statement ELSE Statement
	  public void visit (IfElseStatement ifElseStatement) {
		 if(!ifElseStatement.getThen().getCondition().struct.equals(boolType)) {
			 report_error("Semanticka greska na liniji " + ifElseStatement.getLine() + ": Condition nije tipa bool ", null);
			 return;
		 }
	  }
	  //(IfStatement) IF LPAREN Condition RPAREN Statement
	  public void visit (IfStatement ifStatement) {
		  if(!ifStatement.getThen().getCondition().struct.equals(boolType)) {
			  report_error("Semanticka greska na liniji " + ifStatement.getLine() + ":  Condition nije tipa bool", null);
				 return; 
			 }
	  }

	  //(WhileStatement) WHILE LPAREN Condition RPAREN Statement
	  public void visit (WhileLoop whileLoop) {
		  inloop++;
	  }
	  
	  public void visit (WhileStatement whileStatement) {
		  inloop--;
		  if(!whileStatement.getCondition().struct.equals(boolType)) {
			  report_error("Semanticka greska na liniji " + whileStatement.getLine() + ":  Condition nije tipa bool", null);
				 return; 
			 }
	  }
	  
	  //(ForEachStatement) Designator DOT FOREACH LPAREN IDENT ARROW Statement RPAREN SEMICOLON
	  public void visit (ForEachLoop forEachLoop) {
		  inloop++;
		 forEachLoop.obj= Tab.find(forEachLoop.getIdent());
	  }
	  
	  public void visit (ForEachStatement forEachStatement) {
		  inloop--;
		// Check if designator is Array type
		  Struct type;
		  if(forEachStatement.getDesignator() instanceof OneDesignator) {
			  // Check if designator is a variable 
			  Obj obj = Tab.find(((OneDesignator)forEachStatement.getDesignator()).getDesignatorName());
			  if(obj.getType().getKind()!=Struct.Array) {
				  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Designator mora predstavljati niz", null);
				  return;
			  }
			  type = obj.getType().getElemType();
		  }
		  else {
				  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Designator mora predstavljati niz", null);
				  return;
			  
		  }
		  // Check if ident is variable 
		  if (forEachStatement.getForEach().obj==Tab.noObj) {
			  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Ident"+forEachStatement.getForEach().obj.getName()+" mora biti globalna ili lokalna promenljiva - nije deklarisan", null);
			  return;
		  }
		  // Check if it is a type var 
		  if (forEachStatement.getForEach().obj.getKind()!=Obj.Var) {
			  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Ident"+forEachStatement.getForEach().obj.getName()+" mora biti globalna ili lokalna promenljiva", null);
			  return;
		  }
		  // check if type of ident is equal to type
		  if (type.equals(forEachStatement.getForEach().obj.getType())) return; 
		 		
		  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Promenljiva ident u foreach petlji mora biti istog tipa kao tip elementa niza Designator", null);
		  
	  }
	  //(BreakStatement) BREAK SEMICOLON
	  public void visit (BreakStatement breakStatement) {
		  if (inloop==0) {
			  report_error("Semanticka greska na liniji " + breakStatement.getLine() + ": iskaz break moze biti samo u foreach ili while petlji ", null);

		  }
	  }
	  
	  //(ContinueStatement) CONTINUE SEMICOLON
	  public void visit (ContinueStatement continueStatement) {
		  if (inloop==0) {
			  report_error("Semanticka greska na liniji " + continueStatement.getLine() + ": iskaz continue moze biti samo u foreach ili while petlji ", null);

		  }
	  }
	  
	  //(ReadStatement) READ LPAREN Designator RPAREN SEMICOLON
	  public void visit (ReadStatement readStatement) {
		  Struct type;
		  Obj obj;
		  if(readStatement.getDesignator() instanceof OneDesignator) {
			  // Check if designator is a variable 
			  obj = Tab.find(((OneDesignator)readStatement.getDesignator()).getDesignatorName());
			  if(obj == Tab.noObj) return;
			  if(obj.getKind()!=Obj.Var) {
				  report_error("Semanticka greska na liniji " + readStatement.getLine() + ": Designator mora predstavljati promenljivu ili element niza", null);
				  readStatement.getDesignator().obj = obj;
				  return;
			  }
			  type = obj.getType();
		  }
		  else {
			  obj = Tab.find(((ArrayDesignator)readStatement.getDesignator()).getDesignatorName().getDesignatorName());
			  if(obj == Tab.noObj) return;
			  type = obj.getType().getElemType();
		  }
		  // check if type is equal to int char or bool 
		  if ((!type.equals(Tab.charType)) && (!type.equals(Tab.intType)) && (!type.equals(boolType))) {
			  report_error("Semanticka greska na liniji " + readStatement.getLine() + ": Designator mora biti tipa int, char ili bool ", null);
			 
		  } 	
		  readStatement.getDesignator().obj = obj;
	  }
	  
	  //(PrintStatement) PRINT LPAREN Expr PrintConstant RPAREN SEMICOLON
	  public void visit (PrintStatement printStatement) {
		  if (printStatement.getExpr().struct.equals(Tab.charType)) return; 	//ok
		  if (printStatement.getExpr().struct.equals(Tab.intType)) return; 	//ok
		  if (printStatement.getExpr().struct.equals(boolType)) return; 	//ok
		  report_error("Semanticka greska na liniji " + printStatement.getLine() + ": Expr unutar PRINT Statment mora biti tipa int, char ili bool ", null);

	  }
	  
	  //(ReturnExpression) RETURN Expr SEMICOLON
	  public void visit (ReturnExpression returnExpression) {
		  // Check if method is void 
		  returnFound = true;
		  if(currentMethod == null) {
			  report_error("Semanticka greska na liniji " + returnExpression.getLine() + ": Return iskaz ne moze postojati van tela metode", null);
			  return;
		  }
		  if (currentMethod.getType().equals(Tab.noType)) {
			report_error("Semanticka greska na liniji " + returnExpression.getLine() + ": funkcija " + currentMethod.getName() + "je deklarisana kao void - nije moguce imati povratnu vrednost!", null);
			return;
		  }
		  // Check if Expr type is equal to return method type
		  if (!returnExpression.getExpr().struct.equals(currentMethod.getType())) {
			report_error("Semanticka greska na liniji " + returnExpression.getLine() + ": Tip return naredbe nije ekvivalentan tipu povratne vrednosti funkcije " + currentMethod.getName(), null);
			return;
		  }
	  }
	  
	  /* CONDITIONS */
	  
	  // (Conditions) Condition OR CondTerm 
	  public void visit (Conditions conditions) {
		  if ((conditions.getCondition().struct==null)||(!conditions.getCondition().struct.equals(boolType)) || (!conditions.getCondTerm().struct.equals(boolType))){
			  report_error("Semanticka greska na liniji " + conditions.getLine() + ": U izrazu sa || oba operanda moraju biti tipa boolean", null);
			  conditions.struct = Tab.noType;
			  return;
		  }
		  
		  conditions.struct = boolType;
		  
	  }
	  
	  public void visit (ConditionError conditionError) {
		  conditionError.struct=Tab.noType;
	  }
	  
	  //(OneCondition) CondTerm 
	  public void visit (OneCondition oneCondition) {
		  oneCondition.struct = oneCondition.getCondTerm().struct;
	  }
	  
	  // (ConditionTerms) CondTerm AND CondFact 
	  public void visit (ConditionTerms conditionTerms) {
		  if ((!conditionTerms.getCondTerm().struct.equals(boolType)) || (!conditionTerms.getCondFact().struct.equals(boolType))){
			  report_error("Semanticka greska na liniji " + conditionTerms.getLine() + ": U izrazu sa && oba operanda moraju biti tipa boolean", null);
			  conditionTerms.struct = Tab.noType;
			  return;
		  }
		  
		  conditionTerms.struct = boolType;
		  
	  }
	  
	  //(ConditionTerm) CondFact
	  public void visit (ConditionTerm conditionTerm) {
		  conditionTerm.struct = conditionTerm.getCondFact().struct;
	  }
	  
	  //(ConditionFact) Expr
	  public void visit (ConditionFact conditionFact) {
		  if (!conditionFact.getExpr().struct.equals(boolType)) {
			  conditionFact.struct = Tab.noType;
			  return;
		  }
		  conditionFact.struct = boolType;
	  }
	  
	 // (ConditionFacts) Expr Relop Expr  
	  public void visit (ConditionFacts conditionFacts) {
		  if (!conditionFacts.getExpr1().struct.compatibleWith(conditionFacts.getExpr().struct)){
			  // Types arent compatible
				report_error("Semanticka greska na liniji " + conditionFacts.getLine() + ": Tipovi oba expressiona moraju biti kompatibilni", null);
				conditionFacts.struct = Tab.noType;
				return;
		  }
		  if (conditionFacts.getExpr1().struct.getKind()==Struct.Array) {
			  // Expressions are both arrays
			  // Check if Relop is type != or ==
			  if (!((conditionFacts.getRelop() instanceof IsEqual)||(conditionFacts.getRelop() instanceof NotEqual))) {
				  report_error("Semanticka greska na liniji " + conditionFacts.getLine() + ": Uz nizovne tipove mogu se koristiti samo operatori != , ==", null);
				  conditionFacts.struct = Tab.noType;
				  return;
			  }
					 
		  }
		  conditionFacts.struct = boolType;
		  
	  }
	  
	  /* EXPR */
	  //(NegativeExpression) MINUS TermList
	  public void visit (NegativeExpression negativeExpression) {
		  // Check if type of TermList is IntType
		  if (!negativeExpression.getTermList().struct.equals(Tab.intType)) {
				report_error("Semanticka greska na liniji " + negativeExpression.getLine() + ": Operacija - podrzava samo tip int ", null);
				 negativeExpression.struct= Tab.noType;
				return;
		  }
		  negativeExpression.struct= negativeExpression.getTermList().struct;
	  }
	  //(Expression) TermList
	  public void visit (Expression expression) {
		  expression.struct = expression.getTermList().struct;
	  }
	  
	  /* TERMS */
	  //(Terms) TermList Addop Term 
	  public void visit (Terms terms) {
		  if ((!terms.getTerm().struct.equals(Tab.intType)) ||(!terms.getTermList().struct.equals(Tab.intType))) {
			  report_error("Semanticka greska na liniji " + terms.getLine() + ": Operacije + i - iziskuju da oba argumenta budu tipa int", null);
			  terms.struct = Tab.noType;
			  return;
		  }
		  terms.struct = terms.getTermList().struct;
	  }
	  //(OneTerm) Term
	  public void visit (OneTerm oneTerm) {
		  oneTerm.struct = oneTerm.getTerm().struct;
	  }
	  
	  // (Term) FactorList
	  public void visit (Term term) {
		  term.struct = term.getFactorList().struct;
	  }
	  
	  //(OneFactor) Factor
	  public void visit (OneFactor oneFactor) {
		  oneFactor.struct = oneFactor.getFactor().struct;
	  }
	  // (Factors) FactorList Mulop Factor
	  public void visit (Factors factors) {
		  if ((!factors.getFactor().struct.equals(Tab.intType)) ||(!factors.getFactorList().struct.equals(Tab.intType))) {
			  report_error("Semanticka greska na liniji " + factors.getLine() + ": Operacije *, /, % iziskuju da oba argumenta budu tipa int", null);
			  factors.struct = Tab.noType;
			  return;
		  }
		  factors.struct = factors.getFactorList().struct;
	  }
	  
	  /* DESIGNATORS */
	  
	  public void visit (DesignatorName designatorName) {
		  designatorName.obj = Tab.find(designatorName.getDesignatorName());
	  }
	  // (ArrayDesignator) IDENT LBRACKET Expr RBRACKET 
	  public void visit (ArrayDesignator arrayDesignator) {
		  Obj designator = Tab.find(arrayDesignator.getDesignatorName().getDesignatorName());
		  if (designator == Tab.noObj) {
				report_error("Semanticka greska na liniji " + arrayDesignator.getLine() + ": Promenljiva "+ arrayDesignator.getDesignatorName().getDesignatorName()+" nije deklarisana!", null);
				arrayDesignator.obj = Tab.noObj;
				return;
		  }
		  // Check if IDENT is Array 
		  if (designator.getType().getKind()!=Struct.Array) {
				report_error("Semanticka greska na liniji " + arrayDesignator.getLine() + ": Promenljiva "+ arrayDesignator.getDesignatorName().getDesignatorName()+" nije nizovnog tipa", null);
				arrayDesignator.obj = Tab.noObj;
				return;
		  }
		  // Check if Expr is intType
		  if(!arrayDesignator.getExpr().struct.equals(Tab.intType)) {
				report_error("Semanticka greska na liniji " + arrayDesignator.getLine() + ": Expr nije tipa int", null);
				arrayDesignator.obj = Tab.noObj;
				return;
		  }
		  
		  arrayDesignator.obj = new Obj(Obj.Elem,designator.getName(),designator.getType().getElemType());

		  // ?
		  report_detection(designator,arrayDesignator);
	  }
	  
	  //(OneDesignator) IDENT:designatorName 
	  public void visit (OneDesignator oneDesignator) {
		  Obj designator = Tab.find(oneDesignator.getDesignatorName());
		  if (designator == Tab.noObj) {
				report_error("Semanticka greska na liniji " + oneDesignator.getLine() + ": Promenljiva "+ oneDesignator.getDesignatorName()+" nije deklarisana!", null);
				oneDesignator.obj = Tab.noObj;
				return;
		  }
		  
		  oneDesignator.obj = designator;
		  // *** DODATI INFO MOZDA 
		  report_detection(designator,oneDesignator);
	  }
	  
	  /* FACTORS */

	  //(FactorNumber) NUMCONST 
	  public void visit (FactorNumber factorNumber) {
		  factorNumber.struct = Tab.intType;
	  }
	  
	  //(FactorCharacter) CHARCONST
	  public void visit (FactorCharacter factorCharacter) {
		  factorCharacter.struct = Tab.charType;
	  }
	  
	  //(FactorBoolean) BOOLCONST
	  public void visit (FactorBoolean factorBoolean) {
		  factorBoolean.struct = boolType;
	  }
	  
	  // (BracketExpression) LPAREN Expr RPAREN
	  public void visit (BracketExpression bracketExpression) {
		  bracketExpression.struct = bracketExpression.getExpr().struct;
	  }
	  
	  //(FactorDesignator) Designator
	  public void visit (FactorDesignator factorDesignator) {
		 /* if (factorDesignator.getDesignator().obj.getType().getKind()==3) {
			  // Array 
			  factorDesignator.struct = factorDesignator.getDesignator().obj.getType().getElemType();
		  }
		  else {*/
			  factorDesignator.struct = factorDesignator.getDesignator().obj.getType();
			  /*}*/
			 // report_detection(factorDesignator.getDesignator().obj,factorDesignator);
	  }
	  
	  //(ArrayCreation) NEW Type LBRACKET Expr RBRACKET
	  public void visit (ArrayCreation arrayCreation) {
		  arrayCreation.struct = new Struct(Struct.Array, arrayCreation.getType().struct);
		  // Check if Expr is intType
		  if(!arrayCreation.getExpr().struct.equals(Tab.intType)) {
				report_error("Semanticka greska na liniji " + arrayCreation.getLine() + ": Expr za kreiranje niza nije tipa int", null);
				//arrayCreation.struct = Tab.noType; ***
				return;
		  }
		  
	  }
	  
	  // (FunctionName) Designator
	  public void visit (FunctionName functionName) {
		  parameters.add(0); // added new parameter count
		  if (functionName.getDesignator().obj==Tab.noObj) {
			  functionName.obj= currentFunction= Tab.noObj;
			  return;
		  }
		  Obj function =  Tab.find(functionName.getDesignator().obj.getName());
		  if (function == Tab.noObj) {
			  	functionName.obj= currentFunction= Tab.noObj;
			  	report_error("Semanticka greska na liniji " + functionName.getLine() + ": Funkcija "+ functionName.getDesignator().obj.getName()+" nije deklarisana!", null);
				return;
		  }
		  if ( function.getKind()!=3) {
				report_error("Semanticka greska na liniji " + functionName.getLine() + ": Promenljiva "+ functionName.getDesignator().obj.getName()+" nije funkcija!", null);
				functionName.obj= currentFunction= Tab.noObj;
				return;
		  }
		  
		 functionName.obj= function;
		 currentFunction = function;
		 //report_detection(function, functionName);
	  }
	  
	  /* ACTPARAMS */
	  // (Parameter) Expr 
	  public void visit (Parameter parameter) {
		 if( currentFunction == Tab.noObj) {
			 return;
		 }
		 int numofarguments = currentFunction.getLevel();
		 int index = parameters.size()-1;
		 if(numofarguments<=parameters.get(index)) {
				report_error("Semanticka greska na liniji " + parameter.getLine() + ": Broj formalnih i stvarnih argumenata metode "+ currentFunction.getName()+" nije isti!", null);
				return;
		 }
		Object [] arguments= currentFunction.getLocalSymbols().toArray();
		Obj currentarg = (Obj)arguments[parameters.get(index)];
		if (!parameter.getExpr().struct.compatibleWith(currentarg.getType())) {
			if(!parameter.getExpr().struct.assignableTo(currentarg.getType()) ) {
				report_error("Semanticka greska na liniji " + parameter.getLine() + ": Tip stvarnog argumenta nije kompatibilan sa tipom formalnog argumenta "+ currentarg.getName(), null);
				parameters.set(index, parameters.get(index)+1);
				return;
			}
			
		}
		parameter.struct = parameter.getExpr().struct;
		parameters.set(index, parameters.get(index)+1);
	  }
	  
	  //(FunctionCall) FunctionName LPAREN ActParams RPAREN
	  public void visit (FunctionCall functionCall) {
		  int currentparameters = parameters.get(parameters.size()-1);
		  parameters.remove(parameters.size()-1);
		 if (functionCall.getFunctionName().obj==Tab.noObj) {
			 functionCall.struct = Tab.noType;
			 return;
		 }
		 if (functionCall.getFunctionName().obj.getLevel()!=currentparameters) {
			 report_error("Semanticka greska na liniji " + functionCall.getLine() + ": Broj formalnih i stvarnih argumenata metode "+ functionCall.getFunctionName().obj.getName()+" nije isti!", null);
			 functionCall.struct = Tab.noType;
			 return;
		 }
		 functionCall.struct = functionCall.getFunctionName().obj.getType();
	  }
}
