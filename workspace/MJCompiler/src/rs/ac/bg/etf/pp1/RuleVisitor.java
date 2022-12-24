package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;

public class RuleVisitor extends VisitorAdaptor{

	int printCallCount = 0;
	int varDeclCount = 0;
	
	Logger log = Logger.getLogger(getClass());

	public void visit(Variable Variable) { 
		varDeclCount++;
	}
    public void visit(Variables Variables) { 
    	varDeclCount++;
    }
	
    public void visit(PrintStatement print) {
		printCallCount++;
	}

    int formalParametersCount=0;
    /*public void visit(OneFormalParameter OneFormalParameter) {
    	formalParametersCount++;
    }
    public void visit(FormalParameters FormalParameters) { 
    	formalParametersCount++;
    }*/
    
    /*public void visit(OneParameter OneParameter) { 
    	formalParametersCount++;
    }*/
}
