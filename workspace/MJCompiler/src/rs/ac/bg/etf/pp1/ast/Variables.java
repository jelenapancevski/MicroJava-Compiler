// generated with ast extension for cup
// version 0.8
// 9/0/2023 23:9:27


package rs.ac.bg.etf.pp1.ast;

public class Variables extends VariablesList {

    private VariablesList VariablesList;
    private Variable Variable;

    public Variables (VariablesList VariablesList, Variable Variable) {
        this.VariablesList=VariablesList;
        if(VariablesList!=null) VariablesList.setParent(this);
        this.Variable=Variable;
        if(Variable!=null) Variable.setParent(this);
    }

    public VariablesList getVariablesList() {
        return VariablesList;
    }

    public void setVariablesList(VariablesList VariablesList) {
        this.VariablesList=VariablesList;
    }

    public Variable getVariable() {
        return Variable;
    }

    public void setVariable(Variable Variable) {
        this.Variable=Variable;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VariablesList!=null) VariablesList.accept(visitor);
        if(Variable!=null) Variable.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VariablesList!=null) VariablesList.traverseTopDown(visitor);
        if(Variable!=null) Variable.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VariablesList!=null) VariablesList.traverseBottomUp(visitor);
        if(Variable!=null) Variable.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Variables(\n");

        if(VariablesList!=null)
            buffer.append(VariablesList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Variable!=null)
            buffer.append(Variable.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Variables]");
        return buffer.toString();
    }
}
