// generated with ast extension for cup
// version 0.8
// 4/0/2023 1:46:18


package rs.ac.bg.etf.pp1.ast;

public class BoolConstant extends Constant {

    private Boolean boolConstant;

    public BoolConstant (Boolean boolConstant) {
        this.boolConstant=boolConstant;
    }

    public Boolean getBoolConstant() {
        return boolConstant;
    }

    public void setBoolConstant(Boolean boolConstant) {
        this.boolConstant=boolConstant;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BoolConstant(\n");

        buffer.append(" "+tab+boolConstant);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BoolConstant]");
        return buffer.toString();
    }
}
