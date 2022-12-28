package rs.ac.bg.etf.pp1;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

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
	int parameters;
	
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
		
		  if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
		  }
		  else {
			  report_info("Deklarisana funkcija "+ currentMethod.getName() + " sa "+ currentMethod.getLevel()+" ulazna parametra" , null);	  
		  }
		  
		  Tab.chainLocalSymbols(methodDecl.getMethodTypeName().obj);
		  currentMethod = null;
		  inMethod = false;
		 // returnFound= false;
		  Tab.closeScope(); 
	
	  }
	  
	  // (ReturnMethod) Type:returnType IDENT:methodName 
	  public void visit (ReturnMethod returnMethod) {
		  returnFound = false;
		  inMethod = true;
		  returnMethod.obj = Tab.insert(Obj.Meth, returnMethod.getMethodName(), returnMethod.getType().struct);
		  currentMethod = returnMethod.obj;
		  Tab.openScope();
		  report_info("Deklarisana funkcija "+ returnMethod.getMethodName() + " povratna vrednost je tipa "+ returnMethod.getType().getTypeName(), returnMethod);
	  }
	  
	  // (VoidMethod) VOID IDENT:methodName
	  public void visit (VoidMethod voidMethod) {
		  returnFound = false;
		  inMethod = true;
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
	  
	  
	  /* DESIGNATOR STATEMENTS */
	  /*
	   *  Statement ::= (DesignatorStmt) DesignatorStatement SEMICOLON
	   * DesignatorStatement ::=
						(MultipleAssignment) LBRACKET DesignatorList RBRACKET EQUAL Designator
						|
						(MultipleAssignmentError) LBRACKET DesignatorList RBRACKET EQUAL error 
						;
					
		DesignatorList ::= (Designators) DesignatorList COMMA DesignatorListElement 
				   |
				   (OneDesignatorListElement) DesignatorListElement
				   ;	
				   			
		DesignatorListElement ::= (OneDesignatorElement)Designator 
						  |
						  (NoDesignatorElement)
						  ;
	   * */
	  public void checkPostOp(Designator designator, SyntaxNode node) {
		  Struct type;
		  if(designator instanceof OneDesignator) {
			  // Check if designator is a variable 
			  Obj obj = Tab.find(((OneDesignator)designator).getDesignatorName());
			  if(obj.getKind()!=1) {
				  report_error("Semanticka greska na liniji " + node.getLine() + ": Designator mora predstavljati promenljivu ili element niza", null);
				  return;
			  }
			  type = obj.getType();
		  }
		  else {
			  Obj obj = Tab.find(((ArrayDesignator)designator).getDesignatorName());
			  type = obj.getType().getElemType();
		  }
		  // check if type is equal to int 
		  if (type.equals(Tab.intType)) return; 		
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
	  //(Assignment) Designator EQUAL AssignmentExpr
	  public void visit (Assignment assignment) {
		  Struct type;
		  if(assignment.getDesignator() instanceof OneDesignator) {
			  // Check if designator is a variable 
			  Obj obj = Tab.find(((OneDesignator)assignment.getDesignator()).getDesignatorName());
			  if(obj.getKind()!=1) {
				  report_error("Semanticka greska na liniji " + assignment.getLine() + ": Designator mora predstavljati promenljivu ili element niza", null);
				  return;
			  }
			  type = obj.getType();
		  }
		  else {
			  Obj obj = Tab.find(((ArrayDesignator)assignment.getDesignator()).getDesignatorName());
			  type = obj.getType().getElemType();
		  }
		  // check if type Expr is compatible with type of designator
		  if (((AssignmentExpression)assignment.getAssignmentExpr()).getExpr().struct.compatibleWith(type)) return; 		
		  report_error("Semanticka greska na liniji " + assignment.getLine() + ": Ti Expr nije kompatibilan sa tipom designator-a", null);
	  }
	 //(FuncionCall) FunctionName LPAREN ActParams RPAREN
	  public void visit (FuncionCall funcionCall) {
		  if (funcionCall.getFunctionName().obj.getLevel()!=parameters) {
				 report_error("Semanticka greska na liniji " + funcionCall.getLine() + ": Broj formalnih i stvarnih argumenata metode "+ funcionCall.getFunctionName().obj.getName()+" nije isti!", null);
				return;
			 }
		  //funcionCall.struct = funcionCall.getFunctionName().obj.getType();
	  }
	  
	  /* STATEMENTS */
	  
	  //(IfElseStatement) IF LPAREN Condition RPAREN Statement ELSE Statement
	  public void visit (IfElseStatement ifElseStatement) {
		 if(!ifElseStatement.getCondition().struct.equals(boolType)) {
			 report_error("Semanticka greska na liniji " + ifElseStatement.getLine() + ": Condition nije tipa bool ", null);
			 return;
		 }
	  }
	  //(IfStatement) IF LPAREN Condition RPAREN Statement
	  public void visit (IfStatement ifStatement) {
		  if(!ifStatement.getCondition().struct.equals(boolType)) {
			  report_error("Semanticka greska na liniji " + ifStatement.getLine() + ":  Condition nije tipa bool", null);
				 return; 
			 }
	  }
	  
	
	  //(WhileStatement) WHILE LPAREN Condition RPAREN Statement
	  public void visit (While while1) {
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
	  public void visit (ForEach forEach) {
		  inloop++;
	  }
	  public void visit (ForEachStatement forEachStatement) {
		  inloop--;
		// Check if designator is Array type
		  Struct type;
		  if(forEachStatement.getDesignator() instanceof OneDesignator) {
			  // Check if designator is a variable 
			  Obj obj = Tab.find(((OneDesignator)forEachStatement.getDesignator()).getDesignatorName());
			  if(obj.getType().getKind()!=3) {
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
		  if (Tab.find(forEachStatement.getIdent())==Tab.noObj) {
			  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Ident"+forEachStatement.getIdent()+" mora biti globalna ili lokalna promenljiva - nije deklarisan", null);
			  return;
		  }
		  // Check if it is a type var 
		  if (Tab.find(forEachStatement.getIdent()).getKind()!=1) {
			  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Ident"+forEachStatement.getIdent()+" mora biti globalna ili lokalna promenljiva", null);
			  return;
		  }
		  // check if type of ident is equal to type
		  if (type.equals(Tab.find(forEachStatement.getIdent()).getType())) return; 		
		  report_error("Semanticka greska na liniji " + forEachStatement.getLine() + ": Designator mora biti tipa int", null);
		  
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
		  if(readStatement.getDesignator() instanceof OneDesignator) {
			  // Check if designator is a variable 
			  Obj obj = Tab.find(((OneDesignator)readStatement.getDesignator()).getDesignatorName());
			  if(obj.getKind()!=1) {
				  report_error("Semanticka greska na liniji " + readStatement.getLine() + ": Designator mora predstavljati promenljivu ili element niza", null);
				  return;
			  }
			  type = obj.getType();
		  }
		  else {
			  Obj obj = Tab.find(((ArrayDesignator)readStatement.getDesignator()).getDesignatorName());
			  type = obj.getType().getElemType();
		  }
		  // check if type is equal to int char or bool 
		  if (type.equals(Tab.charType)) return; 	
		  if (type.equals(Tab.intType)) return; 	
		  if (type.equals(boolType)) return; 	
		  report_error("Semanticka greska na liniji " + readStatement.getLine() + ": Designator mora biti tipa int, char ili bool ", null);

	  }
	  
	  //(PrintStatement) PRINT LPAREN Expr PrintConstant RPAREN SEMICOLON
	  public void visit (PrintStatement printStatement) {
		  if (printStatement.getExpr().struct.equals(Tab.charType)) return; 	//ok
		  if (printStatement.getExpr().struct.equals(Tab.intType)) return; 	//ok
		  if (printStatement.getExpr().struct.equals(boolType)) return; 	//ok
		  report_error("Semanticka greska na liniji " + printStatement.getLine() + ": Expr mora biti tipa int, char ili bool ", null);

	  }
	  
	  //(ReturnExpression) RETURN Expr SEMICOLON
	  public void visit (ReturnExpression returnExpression) {
		  // Check if method is void 
		  returnFound = true;
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
	  /*
	   Condition ::= (Conditions) Condition OR CondTerm 
			  |
			  (OneCondition) CondTerm 
			  ;

CondTerm ::= (ConditionTerms) CondTerm AND CondFact 
			|
			(ConditionTerm) CondFact
			;

CondFact ::= (ConditionFacts) Expr Relop Expr 
			|
			(ConditionFact) Expr
			;		
	   * */
	 // (ConditionFacts) Expr Relop Expr  
	  public void visit (ConditionFacts conditionFacts) {
		  if (!conditionFacts.getExpr1().struct.compatibleWith(conditionFacts.getExpr().struct)){
			  // Types arent compatible
				report_error("Semanticka greska na liniji " + conditionFacts.getLine() + ": Tipovi oba expressiona moraju biti kompatibilni", null);
				return;
		  }
		  if (conditionFacts.getExpr1().struct.getKind()==3) {
			  // Expressions are both arrays
			  // Check if Relop is type != or ==
			  if (!((conditionFacts.getRelop() instanceof IsEqual)||(conditionFacts.getRelop() instanceof NotEqual))) {
				  report_error("Semanticka greska na liniji " + conditionFacts.getLine() + ": Uz nizovne tipove mogu se koristiti samo operatori != , ==", null);
					return;
			  }
					 
		  }
		  
	  }
	  
	  /* EXPR */
	  //(NegativeExpression) MINUS TermList
	  public void visit (NegativeExpression negativeExpression) {
		  // Check if type of TermList is IntType
		  if (!negativeExpression.getTermList().struct.equals(Tab.intType)) {
				report_error("Semanticka greska na liniji " + negativeExpression.getLine() + ": Tip TermList nije ekvivalentan tipu int ", null);
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
		  if ((terms.getTerm().struct!=Tab.intType) ||(terms.getTermList().struct!=Tab.intType)) {
			  report_error("Semanticka greska na liniji " + terms.getLine() + ": Operacije + i - iziskuju da oba argumenta budu tipa int", null);
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
		  if ((factors.getFactor().struct!=Tab.intType) ||(factors.getFactorList().struct!=Tab.intType)) {
			  report_error("Semanticka greska na liniji " + factors.getLine() + ": Operacije *, /, % iziskuju da oba argumenta budu tipa int", null);
			  return;
		  }
		  factors.struct = factors.getFactorList().struct;
	  }
	  
	  /* DESIGNATORS */
	  
	  // (ArrayDesignator) IDENT LBRACKET Expr RBRACKET 
	  public void visit (ArrayDesignator arrayDesignator) {
		  Obj designator = Tab.find(arrayDesignator.getDesignatorName());
		  if (designator == Tab.noObj) {
				report_error("Semanticka greska na liniji " + arrayDesignator.getLine() + ": Promenljiva "+ arrayDesignator.getDesignatorName()+" nije deklarisana!", null);
				arrayDesignator.obj = Tab.noObj;
				return;
		  }
		  // Check if IDENT is Array 
		  if (designator.getType().getKind()!=3) {
				report_error("Semanticka greska na liniji " + arrayDesignator.getLine() + ": Promenljiva "+ arrayDesignator.getDesignatorName()+" nije nizovnog tipa", null);
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
				  //designator;
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
		  
	  }
	  
	  //(ArrayCreation) NEW Type LBRACKET Expr RBRACKET
	  public void visit (ArrayCreation arrayCreation) {
		  arrayCreation.struct = new Struct(Struct.Array, arrayCreation.getType().struct);
		  // Check if Expr is intType
		  if(!arrayCreation.getExpr().struct.equals(Tab.intType)) {
				report_error("Semanticka greska na liniji " + arrayCreation.getLine() + ": Expr nije tipa int", null);
				arrayCreation.struct = Tab.noType;
				return;
		  }
		  
	  }
	  
	  // (FunctionName) Designator
	  public void visit (FunctionName functionName) {
		  Obj function =  Tab.find(functionName.getDesignator().obj.getName());
		  if (function == Tab.noObj) {
				report_error("Semanticka greska na liniji " + functionName.getLine() + ": Funkcija "+ functionName.getDesignator().obj.getName()+" nije deklarisana!", null);
				return;
		  }
		  if ( function.getKind()!=3) {
				report_error("Semanticka greska na liniji " + functionName.getLine() + ": Promenljiva "+ functionName.getDesignator().obj.getName()+" nije funkcija!", null);
				return;
		  }
		  
		 functionName.obj= function;
		 currentFunction = function;
		 parameters = 0;
	  }
	  
	  /* ACTPARAMS */
	  // (Parameter) Expr 
	  public void visit (Parameter parameter) {
		 int numofarguments = currentFunction.getLevel();
		 if(numofarguments<=parameters) {
				report_error("Semanticka greska na liniji " + parameter.getLine() + ": Broj formalnih i stvarnih argumenata metode "+ currentFunction.getName()+" nije isti!", null);
				return;
		 }
		Object [] arguments= currentFunction.getLocalSymbols().toArray();
		Obj currentarg = (Obj)arguments[parameters];
		if (!parameter.getExpr().struct.compatibleWith(currentarg.getType())) {
			report_error("Semanticka greska na liniji " + parameter.getLine() + ": Tip stvarnog argumenta  nije kompatibilan sa tipom formalnog argumenta "+ currentarg.getName(), null);
			return;
		}
		parameter.struct = parameter.getExpr().struct;
		parameters++;
	  }
	  
	  //(FunctionCall) FunctionName LPAREN ActParams RPAREN
	  public void visit (FunctionCall functionCall) {
		 if (functionCall.getFunctionName().obj.getLevel()!=parameters) {
			 report_error("Semanticka greska na liniji " + functionCall.getLine() + ": Broj formalnih i stvarnih argumenata metode "+ functionCall.getFunctionName().obj.getName()+" nije isti!", null);
			return;
		 }
		 functionCall.struct = functionCall.getFunctionName().obj.getType();
	  }
}
