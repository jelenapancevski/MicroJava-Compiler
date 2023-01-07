package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor{
	private int mainPC;
	private Stack <Obj> designatorList = null;
	public int getMainPC() {
		return mainPC;
	}

	public void setMainPC(int mainPC) {
		this.mainPC = mainPC;
	}
	
	/* METHODS DECLARATIONS */
	
	public void generateEnterInstruction(Obj method) {
		// Generating enter instruction 
		 Code.put( Code.enter);
		 Code.put(method.getLevel()); // fromal arguments 
		 Code.put(method.getLocalSymbols().size()); // all function arguments
	}
	

	  // (MethodDecl) MethodTypeName LPAREN MethodParameterList RPAREN MethodVariableList LBRACE MethodStatementList RBRACE;
	  public void visit (MethodDecl methodDecl) {
		  Code.put(Code.exit);
		  Code.put(Code.return_);
	  }
	  
	  //MethodTypeName
	  public void visit (ReturnMethod returnMethod) {
		  returnMethod.obj.setAdr(Code.pc);
		  generateEnterInstruction(returnMethod.obj);
	  }

	  public void visit (VoidMethod voidMethod) {
		  voidMethod.obj.setAdr(Code.pc);
		  if(voidMethod.getMethodName().equalsIgnoreCase("main")){
				 mainPC = Code.pc;
			 }
		  generateEnterInstruction(voidMethod.obj);
	  }
	  
	  // (OneParameter) Type:argumentType IDENT:argumentName ArrayBrackets:isArray ;
	  public void visit (OneParameter oneParameter) {
		 }
	
	
	 /* DESIGNATOR STATEMENTS */
	  
//designatorList
	  
	  //NoDesignatorElement
	  public void visit (NoDesignatorElement noDesignatorElement) {
		  if (designatorList==null) {
			  designatorList = new Stack<>();
		  }
		  designatorList.add(null); // just comma added skip element of array
	  }
	  
	  //(OneDesignatorElement)Designator
	  public void visit (OneDesignatorElement oneDesignatorElement) {
		  if (designatorList==null) {
			  designatorList = new Stack<>();
		  }
		  designatorList.add(oneDesignatorElement.getDesignator().obj);
	  }
	  
	  //(MultipleAssignment) LBRACKET DesignatorList RBRACKET EQUAL Designator
	  public void visit (MultipleAssignment multipleAssignment) {
		  int index = designatorList.size()-1;
		 while (!designatorList.empty()) {
			 if(index<0) {
				 // runtime error
				 System.out.println("Error niz je kraci od broja designator-a");
			 }
			 Obj designator = designatorList.pop();
			 if(designator!=null) {
				 Code.load(multipleAssignment.getDesignator().obj);
				 Code.loadConst(index);
				 Code.put(Code.aload);
				 Code.store(designator);
			 }
			 index--;
		 }
		  
	  }
	
	 //(Assignment) Designator EQUAL AssignmentExpr
	  public void visit (Assignment assignment) {
		  Code.store(assignment.getDesignator().obj);
	  }
	  
	 //(ValueIncrement) Designator INCREMENT 
	public void visit (ValueIncrement valueIncrement) {
		Code.load(valueIncrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(valueIncrement.getDesignator().obj);
	}
	
	 //(ValueDecrement) Designator DECREMENT
	public void visit (ValueDecrement valueDecrement) {
		Code.load(valueDecrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(valueDecrement.getDesignator().obj);
	  }
	
	/* STATEMENTS */
	
	//(ReadStatement) READ LPAREN Designator RPAREN SEMICOLON
	  public void visit (ReadStatement readStatement) {
		  if (readStatement.getDesignator().obj.getType().equals(Tab.charType)) {
			  // read char - bread
			  Code.put(Code.bread);
		  } else {
			  // read bool / int 
			  Code.put(Code.read);  
		  }
		  Code.store(readStatement.getDesignator().obj);
	  }
	  
	  //(PrintStatement) PRINT LPAREN Expr PrintConstant RPAREN SEMICOLON
	  public void visit (PrintStatement printStatement) {
		  if(printStatement.getExpr().struct != Tab.charType) {
			 // 4 byte-a 
			 if (printStatement.getPrintConstant() instanceof NoPrintConstant)Code.loadConst(5);
				 Code.put(Code.print);
			 }else {
				 // char 1 byte
				 if (printStatement.getPrintConstant() instanceof NoPrintConstant)Code.loadConst(1);
				 	Code.put(Code.bprint);
				}
	  }
	  
	public void visit (HasPrintConstant hasPrintConstant) {
		Code.loadConst(hasPrintConstant.getN1());
	}
	
	
	/* EXPR */
	  //(NegativeExpression) MINUS TermList
	  public void visit (NegativeExpression negativeExpression) {
		  Code.put(Code.neg);
	  }
	
	/* TERMS */
	
	//(Terms) TermList Addop Term 
	public void visit (Terms terms) {
		  if (terms.getAddop() instanceof Plus) {
			  Code.put(Code.add);
		  }else {
			  Code.put(Code.sub);
		  }
	  }
	
	/* FACTORS */
	
	  //(Factors) FactorList Mulop Factor
	  public void visit (Factors factors) {
		  if( factors.getMulop() instanceof Multiply) {
			  Code.put(Code.mul);
		  }else if (factors.getMulop() instanceof Divide) {
			  Code.put(Code.div);
		  }else {
			  Code.put(Code.rem);
		  }
	  }
	  //(FactorNumber) NUMCONST 
	  public void visit (FactorNumber factorNumber) {
		  Code.loadConst(factorNumber.getN1());
	  }
	  
	  //(FactorCharacter) CHARCONST
	  public void visit (FactorCharacter factorCharacter) {
		 Code.loadConst(factorCharacter.getC1().charValue());
	  }
	  
	  //(FactorBoolean) BOOLCONST
	  public void visit (FactorBoolean factorBoolean) {
		  if( factorBoolean.getB1()==true) {
			  Code.loadConst(1);
		  } else {
			  Code.loadConst(0);
		  }
		  
	  }
	  
	  //(FactorDesignator) Designator
	  public void visit (FactorDesignator factorDesignator) {
		Code.load(factorDesignator.getDesignator().obj);
	 }
	  
	  //(ArrayCreation) NEW Type LBRACKET Expr RBRACKET
	  public void visit (ArrayCreation arrayCreation) {
		  Code.put(Code.newarray);
		  if (arrayCreation.getType().struct.equals(Tab.charType)) {
			  //0
			  Code.put(0);
		  }else {
			  //1
			  Code.put(1);
		  }
	  }
	  
/* DESIGNATORS */
	  
	  // (ArrayDesignator) Element LBRACKET Expr RBRACKET 
	  public void visit (DesignatorName designatorName) {
		  Code.load(designatorName.obj);
	  }
}
