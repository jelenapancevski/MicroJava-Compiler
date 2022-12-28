// generated with ast extension for cup
// version 0.8
// 28/11/2022 3:56:18


package rs.ac.bg.etf.pp1.ast;

public class MethodVariables extends MethodVariableList {

    private MethodVariableList MethodVariableList;
    private VarDecl VarDecl;

    public MethodVariables (MethodVariableList MethodVariableList, VarDecl VarDecl) {
        this.MethodVariableList=MethodVariableList;
        if(MethodVariableList!=null) MethodVariableList.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public MethodVariableList getMethodVariableList() {
        return MethodVariableList;
    }

    public void setMethodVariableList(MethodVariableList MethodVariableList) {
        this.MethodVariableList=MethodVariableList;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodVariableList!=null) MethodVariableList.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodVariableList!=null) MethodVariableList.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodVariableList!=null) MethodVariableList.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodVariables(\n");

        if(MethodVariableList!=null)
            buffer.append(MethodVariableList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodVariables]");
        return buffer.toString();
    }
}
