// generated with ast extension for cup
// version 0.8
// 27/11/2022 18:16:49


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodTypeName MethodTypeName;
    private MethodParameterList MethodParameterList;
    private MethodVariableList MethodVariableList;
    private MethodStatementList MethodStatementList;

    public MethodDecl (MethodTypeName MethodTypeName, MethodParameterList MethodParameterList, MethodVariableList MethodVariableList, MethodStatementList MethodStatementList) {
        this.MethodTypeName=MethodTypeName;
        if(MethodTypeName!=null) MethodTypeName.setParent(this);
        this.MethodParameterList=MethodParameterList;
        if(MethodParameterList!=null) MethodParameterList.setParent(this);
        this.MethodVariableList=MethodVariableList;
        if(MethodVariableList!=null) MethodVariableList.setParent(this);
        this.MethodStatementList=MethodStatementList;
        if(MethodStatementList!=null) MethodStatementList.setParent(this);
    }

    public MethodTypeName getMethodTypeName() {
        return MethodTypeName;
    }

    public void setMethodTypeName(MethodTypeName MethodTypeName) {
        this.MethodTypeName=MethodTypeName;
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
        if(MethodTypeName!=null) MethodTypeName.accept(visitor);
        if(MethodParameterList!=null) MethodParameterList.accept(visitor);
        if(MethodVariableList!=null) MethodVariableList.accept(visitor);
        if(MethodStatementList!=null) MethodStatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodTypeName!=null) MethodTypeName.traverseTopDown(visitor);
        if(MethodParameterList!=null) MethodParameterList.traverseTopDown(visitor);
        if(MethodVariableList!=null) MethodVariableList.traverseTopDown(visitor);
        if(MethodStatementList!=null) MethodStatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodTypeName!=null) MethodTypeName.traverseBottomUp(visitor);
        if(MethodParameterList!=null) MethodParameterList.traverseBottomUp(visitor);
        if(MethodVariableList!=null) MethodVariableList.traverseBottomUp(visitor);
        if(MethodStatementList!=null) MethodStatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodTypeName!=null)
            buffer.append(MethodTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
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
