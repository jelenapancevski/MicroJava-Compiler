// generated with ast extension for cup
// version 0.8
// 26/11/2022 14:39:44


package rs.ac.bg.etf.pp1.ast;

public class Variables extends VariablesList {

    private VariablesList VariablesList;
    private String I2;
    private ArrayBrackets ArrayBrackets;

    public Variables (VariablesList VariablesList, String I2, ArrayBrackets ArrayBrackets) {
        this.VariablesList=VariablesList;
        if(VariablesList!=null) VariablesList.setParent(this);
        this.I2=I2;
        this.ArrayBrackets=ArrayBrackets;
        if(ArrayBrackets!=null) ArrayBrackets.setParent(this);
    }

    public VariablesList getVariablesList() {
        return VariablesList;
    }

    public void setVariablesList(VariablesList VariablesList) {
        this.VariablesList=VariablesList;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public ArrayBrackets getArrayBrackets() {
        return ArrayBrackets;
    }

    public void setArrayBrackets(ArrayBrackets ArrayBrackets) {
        this.ArrayBrackets=ArrayBrackets;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VariablesList!=null) VariablesList.accept(visitor);
        if(ArrayBrackets!=null) ArrayBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VariablesList!=null) VariablesList.traverseTopDown(visitor);
        if(ArrayBrackets!=null) ArrayBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VariablesList!=null) VariablesList.traverseBottomUp(visitor);
        if(ArrayBrackets!=null) ArrayBrackets.traverseBottomUp(visitor);
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

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(ArrayBrackets!=null)
            buffer.append(ArrayBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Variables]");
        return buffer.toString();
    }
}
