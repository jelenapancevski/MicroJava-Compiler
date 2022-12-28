// generated with ast extension for cup
// version 0.8
// 28/11/2022 3:56:18


package rs.ac.bg.etf.pp1.ast;

public class ErrorVariableComma extends VarDecl {

    private VariablesList VariablesList;

    public ErrorVariableComma (VariablesList VariablesList) {
        this.VariablesList=VariablesList;
        if(VariablesList!=null) VariablesList.setParent(this);
    }

    public VariablesList getVariablesList() {
        return VariablesList;
    }

    public void setVariablesList(VariablesList VariablesList) {
        this.VariablesList=VariablesList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VariablesList!=null) VariablesList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VariablesList!=null) VariablesList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VariablesList!=null) VariablesList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ErrorVariableComma(\n");

        if(VariablesList!=null)
            buffer.append(VariablesList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ErrorVariableComma]");
        return buffer.toString();
    }
}
