// generated with ast extension for cup
// version 0.8
// 26/11/2022 3:30:20


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodType MethodType;
    private String I2;
    private MethodParameterList MethodParameterList;
    private MethodVariableList MethodVariableList;
    private MethodStatementList MethodStatementList;

    public MethodDecl (MethodType MethodType, String I2, MethodParameterList MethodParameterList, MethodVariableList MethodVariableList, MethodStatementList MethodStatementList) {
        this.MethodType=MethodType;
        if(MethodType!=null) MethodType.setParent(this);
        this.I2=I2;
        this.MethodParameterList=MethodParameterList;
        if(MethodParameterList!=null) MethodParameterList.setParent(this);
        this.MethodVariableList=MethodVariableList;
        if(MethodVariableList!=null) MethodVariableList.setParent(this);
        this.MethodStatementList=MethodStatementList;
        if(MethodStatementList!=null) MethodStatementList.setParent(this);
    }

    public MethodType getMethodType() {
        return MethodType;
    }

    public void setMethodType(MethodType MethodType) {
        this.MethodType=MethodType;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public MethodParameterList getMethodParameterList() {
        return MethodParameterList;
    }

    public void setMethodParameterList(MethodParameterList MethodParameterList) {
        this.MethodParameterList=MethodParameterList;
    }

    public MethodVariableList getMethodVariableList() {
        return MethodVariableList;
    }

    public void setMethodVariableList(MethodVariableList MethodVariableList) {
        this.MethodVariableList=MethodVariableList;
    }

    public MethodStatementList getMethodStatementList() {
        return MethodStatementList;
    }

    public void setMethodStatementList(MethodStatementList MethodStatementList) {
        this.MethodStatementList=MethodStatementList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodType!=null) MethodType.accept(visitor);
        if(MethodParameterList!=null) MethodParameterList.accept(visitor);
        if(MethodVariableList!=null) MethodVariableList.accept(visitor);
        if(MethodStatementList!=null) MethodStatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodType!=null) MethodType.traverseTopDown(visitor);
        if(MethodParameterList!=null) MethodParameterList.traverseTopDown(visitor);
        if(MethodVariableList!=null) MethodVariableList.traverseTopDown(visitor);
        if(MethodStatementList!=null) MethodStatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodType!=null) MethodType.traverseBottomUp(visitor);
        if(MethodParameterList!=null) MethodParameterList.traverseBottomUp(visitor);
        if(MethodVariableList!=null) MethodVariableList.traverseBottomUp(visitor);
        if(MethodStatementList!=null) MethodStatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodType!=null)
            buffer.append(MethodType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(MethodParameterList!=null)
            buffer.append(MethodParameterList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodVariableList!=null)
            buffer.append(MethodVariableList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodStatementList!=null)
            buffer.append(MethodStatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
