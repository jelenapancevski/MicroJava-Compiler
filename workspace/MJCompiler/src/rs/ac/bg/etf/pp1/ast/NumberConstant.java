// generated with ast extension for cup
// version 0.8
// 15/0/2023 16:5:54


package rs.ac.bg.etf.pp1.ast;

public class NumberConstant extends Constant {

    private Integer numConstant;

    public NumberConstant (Integer numConstant) {
        this.numConstant=numConstant;
    }

    public Integer getNumConstant() {
        return numConstant;
    }

    public void setNumConstant(Integer numConstant) {
        this.numConstant=numConstant;
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
        buffer.append("NumberConstant(\n");

        buffer.append(" "+tab+numConstant);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NumberConstant]");
        return buffer.toString();
    }
}
