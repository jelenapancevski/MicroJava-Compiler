package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	private int mainPC;
	private Stack<Obj> designatorList = null;
	private Stack<IfElseStatement> statements = new Stack<>();
	public CodeGenerator() {
		chr();
		ord();
		len();
	}

	public int getMainPC() {
		return mainPC;
	}

	public void setMainPC(int mainPC) {
		this.mainPC = mainPC;
	}

	/* METHODS DECLARATIONS */

	public void generateExitInstruction() {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void generateEnterInstruction(Obj method) {
		// Generating enter instruction
		Code.put(Code.enter);
		Code.put(method.getLevel()); // fromal arguments
		Code.put(method.getLocalSymbols().size()); // all function arguments
	}

	public void chr() {
		Obj chrfunc = Tab.find("chr");
		chrfunc.setAdr(Code.pc);
		generateEnterInstruction(chrfunc);
		Code.put(Code.load_n + 0);
		generateExitInstruction();
	}

	public void ord() {
		Obj ordfunc = Tab.find("ord");
		ordfunc.setAdr(Code.pc);
		generateEnterInstruction(ordfunc);
		Code.put(Code.load_n + 0);
		generateExitInstruction();
	}

	public void len() {
		Obj lenfunc = Tab.find("len");
		lenfunc.setAdr(Code.pc);
		generateEnterInstruction(lenfunc);
		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		generateExitInstruction();

	}

	// (MethodDecl) MethodTypeName LPAREN MethodParameterList RPAREN
	// MethodVariableList LBRACE MethodStatementList RBRACE;
	public void visit(MethodDecl methodDecl) {
		if (methodDecl.getMethodTypeName().obj.getType() == Tab.noType) {
		}
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	// MethodTypeName
	public void visit(ReturnMethod returnMethod) {
		returnMethod.obj.setAdr(Code.pc);
		generateEnterInstruction(returnMethod.obj);
	}

	public void visit(VoidMethod voidMethod) {
		voidMethod.obj.setAdr(Code.pc);
		if (voidMethod.getMethodName().equalsIgnoreCase("main")) {
			mainPC = Code.pc;
		}
		generateEnterInstruction(voidMethod.obj);
	}

	// (OneParameter) Type:argumentType IDENT:argumentName ArrayBrackets:isArray ;
	public void visit(OneParameter oneParameter) {
	}

	/* DESIGNATOR STATEMENTS */

//designatorList

	// NoDesignatorElement
	public void visit(NoDesignatorElement noDesignatorElement) {
		if (designatorList == null) {
			designatorList = new Stack<>();
		}
		designatorList.add(null); // just comma added skip element of array
	}

	// (OneDesignatorElement)Designator
	public void visit(OneDesignatorElement oneDesignatorElement) {
		if (designatorList == null) {
			designatorList = new Stack<>();
		}
		designatorList.add(oneDesignatorElement.getDesignator().obj);
	}

	// (MultipleAssignment) LBRACKET DesignatorList RBRACKET EQUAL Designator
	public void visit(MultipleAssignment multipleAssignment) {
		int index = designatorList.size() - 1;
		while (!designatorList.empty()) {
			Obj designator = designatorList.pop();
			if (designator != null) {
				Code.load(multipleAssignment.getDesignator().obj);
				Code.loadConst(index);
				Code.put(Code.aload);
				Code.store(designator);
			}
			index--;
		}

	}

	// (Assignment) Designator EQUAL AssignmentExpr
	public void visit(Assignment assignment) {
		Code.store(assignment.getDesignator().obj);
	}

	// (FuncionCall) FunctionName LPAREN ActParams RPAREN
	public void visit(FuncionCall funcionCall) {
		int offset = (funcionCall.getFunctionName().obj.getAdr() - Code.pc);
		// instrukcija call
		Code.put(Code.call);
		// adresa funkcije
		Code.put2(offset);
		// treba skinuti return sa steka jer je samo statement
		if (funcionCall.getFunctionName().obj.getType() != Tab.noType) {
			Code.put(Code.pop);
		}
	}

	// (ValueIncrement) Designator INCREMENT
	public void visit(ValueIncrement valueIncrement) {
		Code.load(valueIncrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(valueIncrement.getDesignator().obj);
	}

	// (ValueDecrement) Designator DECREMENT
	public void visit(ValueDecrement valueDecrement) {
		Code.load(valueDecrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(valueDecrement.getDesignator().obj);
	}

	/* STATEMENTS */
	//Stack <Integer> positions= new Stack<>();
	//(ForEachStatement) Designator DOT ForEach LPAREN IDENT:ident ARROW ForStatements RPAREN SEMICOLON
	public void visit (ForEachLoop forEachLoop) {
		/*((ForEachStatement)forEachLoop.getParent()).getDesignator().obj.setFpPos(0);
		Code.load(((ForEachStatement)forEachLoop.getParent()).getDesignator().obj);
		Code.loadConst(((ForEachStatement)forEachLoop.getParent()).getDesignator().obj.getFpPos());
		Code.loadConst(((ForEachStatement)forEachLoop.getParent()).getDesignator().obj.getFpPos());
		positions.add(Code.pc); //beginfor
		Code.load(((ForEachStatement)forEachLoop.getParent()).getDesignator().obj);
		Code.put(Code.arraylength);
		Code.putFalseJump(Code.eq, 0);
		positions.add(Code.pc); //endfor
		//Code.store(new Obj(Obj.Var, "cnt", Struct.Int))*/
	}
	
	public void visit(ForStatements forStatements) {
		/*//inc counter
		Code.putJump(positions.elementAt(positions.size()-2));
		Code.fixup(positions.pop()-2);
		positions.pop();*/
	}
	// (ReadStatement) READ LPAREN Designator RPAREN SEMICOLON
	public void visit(ReadStatement readStatement) {
		if (readStatement.getDesignator().obj.getType().equals(Tab.charType)) {
			// read char - bread
			Code.put(Code.bread);
		} else {
			// read bool / int
			Code.put(Code.read);
		}
		if (readStatement.getDesignator().obj.getType().getKind() == Struct.Array) {
			Code.store(new Obj(Obj.Elem, readStatement.getDesignator().obj.getName(),
					readStatement.getDesignator().obj.getType().getElemType()));
		} else
			Code.store(readStatement.getDesignator().obj);

	}

	// (PrintStatement) PRINT LPAREN Expr PrintConstant RPAREN SEMICOLON
	public void visit(PrintStatement printStatement) {
		if (printStatement.getExpr().struct != Tab.charType) {
			// 4 byte-a
			if (printStatement.getPrintConstant() instanceof NoPrintConstant)
				Code.loadConst(5);
			Code.put(Code.print);
		} else {
			// char 1 byte
			if (printStatement.getPrintConstant() instanceof NoPrintConstant)
				Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}

	public void visit(HasPrintConstant hasPrintConstant) {
		Code.loadConst(hasPrintConstant.getN1());
	}

	// (ReturnStatement) RETURN SEMICOLON
	public void visit(ReturnStatement returnStatement) {
		generateExitInstruction();
	}

	// (ReturnExpression) RETURN Expr SEMICOLON
	public void visit(ReturnExpression returnExpression) {
		generateExitInstruction();
	}

	/* EXPR */
	// (NegativeExpression) MINUS TermList
	public void visit(NegativeExpression negativeExpression) {
		Code.put(Code.neg);
	}

	/* TERMS */

	// (Terms) TermList Addop Term
	public void visit(Terms terms) {
		if (terms.getAddop() instanceof Plus) {
			Code.put(Code.add);
		} else {
			Code.put(Code.sub);
		}
	}

	/* FACTORS */

	// (Factors) FactorList Mulop Factor
	public void visit(Factors factors) {
		if (factors.getMulop() instanceof Multiply) {
			Code.put(Code.mul);
		} else if (factors.getMulop() instanceof Divide) {
			Code.put(Code.div);
		} else {
			Code.put(Code.rem);
		}
	}

	// (FactorNumber) NUMCONST
	public void visit(FactorNumber factorNumber) {
		Code.loadConst(factorNumber.getN1());
	}

	// (FactorCharacter) CHARCONST
	public void visit(FactorCharacter factorCharacter) {
		Code.loadConst(factorCharacter.getC1().charValue());
	}

	// (FactorBoolean) BOOLCONST
	public void visit(FactorBoolean factorBoolean) {
		if (factorBoolean.getB1() == true) {
			Code.loadConst(1);
		} else {
			Code.loadConst(0);
		}

	}

	// (FactorDesignator) Designator
	public void visit(FactorDesignator factorDesignator) {
		Code.load(factorDesignator.getDesignator().obj);
	}

	// (ArrayCreation) NEW Type LBRACKET Expr RBRACKET
	public void visit(ArrayCreation arrayCreation) {
		Code.put(Code.newarray);
		if (arrayCreation.getType().struct.equals(Tab.charType)) {
			// 0
			Code.put(0);
		} else {
			// 1
			Code.put(1);
		}
	}

	// (FunctionCall) FunctionName LPAREN ActParams RPAREN
	public void visit(FunctionCall functionCall) {
		int offset = (functionCall.getFunctionName().obj.getAdr() - Code.pc);
		// instrukcija call
		Code.put(Code.call);
		// adresa funkcije
		Code.put2(offset);
	}

	/* DESIGNATORS */

	// (ArrayDesignator) Element LBRACKET Expr RBRACKET
	public void visit(DesignatorName designatorName) {
		Code.load(designatorName.obj);
	}
	
	 /* CONDITIONS */
	
	public class IfElseCondition{
		int ThenAddress=-1;
		int ElseAddress=-1;
		int EndIfStmtAddress=-1;
		ArrayList <Integer> NextConditionAddress = new ArrayList<>();			;
	}
	
	Stack <IfElseCondition> ifelseConditions= new Stack<>();
	//IfStart
	public void visit(IfStart ifStart) {
		ifelseConditions.push(new IfElseCondition());
	}
	
	public void visit (Then then) {
		ifelseConditions.peek().ThenAddress= Code.pc;
	}
	public void visit (ElseStart elseStart) {
		ifelseConditions.peek().ElseAddress = Code.pc;
	}
	public void visit (ElseStatement elseStatement) {
		ifelseConditions.peek().EndIfStmtAddress = Code.pc;
	}
	//(IfStatement) IfStart Then Statement
	public void visit (IfStatement ifStatement) {
		ifelseConditions.peek().EndIfStmtAddress = Code.pc;
		IfElseCondition cnd = ifelseConditions.pop()  ;
		System.out.println("\n\nIF STATEMENT");
		System.out.println("ThenAddress "+cnd.ThenAddress);
		System.out.println("ElseAddress "+cnd.ElseAddress);
		System.out.println("EndIfStmtAddress "+cnd.EndIfStmtAddress);
	}
	//(IfElseStatement) IfStart Then Statement ElseStart ElseStatement
	public void visit (IfElseStatement ifElseStatement) {
		IfElseCondition cnd = ifelseConditions.pop()  ;
		System.out.println("\n\nIF STATEMENT");
		System.out.println("ThenAddress "+cnd.ThenAddress);
		System.out.println("ElseAddress "+cnd.ElseAddress);
		System.out.println("EndIfStmtAddress "+cnd.EndIfStmtAddress);
		
	}
	
	 /* CONDITIONS */
	 /* Od pre 
	  * public class IfElseStatement{
		  int ifAdr=-1;
		  int elseAdr=-1;
		  int endStmt =-1;
		 public class Condition{
			  int jmpAdrs;
			  int code;
			  public Condition(int jmpAdrs, int code) {
				  this.jmpAdrs=jmpAdrs;
				  this.code= code;
			  }
		  }
		 public void addCondition(int jmpAdrs, int code) {
			 conditions.add(new Condition(jmpAdrs,code));
		 }
		 Stack<Condition> conditions = new Stack<>();
	  }
	  public void visit(IfStart ifStart) {
		  // start of if statement
		  IfElseStatement st = new IfElseStatement();
		  statements.push(st);
	  }
	  public void visit(ElseStart elseStart) {
		  // start of else statement
		  Code.putJump(0); // If statments finished skip Else statments
		  IfElseStatement st = statements.pop();
		  st.endStmt = Code.pc-2;
		  statements.push(st);
		  Code.fixup(st.ifAdr);
		 
	  }
	  
	  public void visit(Then then) {
		  IfElseStatement st = statements.pop();
		  st.ifAdr = Code.pc-2;
		  statements.push(st);
	  }
	  
	  //(ElseStatement) Statement
	 public void visit(ElseStatement elseStatement) {
		 IfElseStatement st=statements.pop();
		 Code.fixup(st.endStmt);
		 
		
	 }
	 //	(IfStatement) IfStart LPAREN Condition RPAREN Statement
	 public void visit (IfStatement ifStatement) {
		 IfElseStatement st= statements.pop();
		 Code.fixup(st.ifAdr);
		
	 }
	  public void visit (Or or) {
		  System.out.println("Usao u or");
	  }
	  // (Conditions) Condition OR CondTerm 
	  public void visit (Conditions conditions) {
		  
		  
	  }
	  
	  //(OneCondition) CondTerm 
	  public void visit (OneCondition oneCondition) {
	  }
	  
	  // (ConditionTerms) CondTerm AND CondFact 
	  public void visit (ConditionTerms conditionTerms) {
		 
	  }
	  
	  //(ConditionTerm) CondFact
	  public void visit (ConditionTerm conditionTerm) {
		  
	  }
	  
	  //(ConditionFact) Expr
	  public void visit (ConditionFact conditionFact) {
		 Code.loadConst(1);
		 Code.putFalseJump(Code.eq, 0); // skip if 
		// statements.peek().addCondition(Code.pc-3,Code.eq);
		 
	  }
	  
	 // (ConditionFacts) Expr Relop Expr  
	  public void visit (ConditionFacts conditionFacts) {
		  int code;
		  if (conditionFacts.getRelop() instanceof IsEqual) {
			  code = Code.eq;
		  }else if (conditionFacts.getRelop() instanceof NotEqual) {
			  code = Code.ne;
		  }else if (conditionFacts.getRelop() instanceof Greater) {
			  code = Code.gt;
		  }else if (conditionFacts.getRelop() instanceof GreaterEqual) {
			  code = Code.ge;
		  }else if (conditionFacts.getRelop() instanceof LessThan) {
			  code = Code.lt;
		  }else {
			  code = Code.le;
		  }
		  Code.putFalseJump(code, 0); // skip if 
		  //statements.peek().addCondition(Code.pc-3,code);
	  }*/
}
