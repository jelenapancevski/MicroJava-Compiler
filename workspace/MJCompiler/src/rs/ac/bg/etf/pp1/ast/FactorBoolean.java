// generated with ast extension for cup
// version 0.8
// 26/11/2022 16:55:57


package rs.ac.bg.etf.pp1.ast;

public class FactorBoolean extends Factor {

    private Boolean B1;

    public FactorBoolean (Boolean B1) {
        this.B1=B1;
    }

    public Boolean getB1() {
        return B1;
    }

    public void setB1(Boolean B1) {
        this.B1=B1;
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
        buffer.append("FactorBoolean(\n");

        buffer.append(" "+tab+B1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorBoolean]");
        return buffer.toString();
    }
}
