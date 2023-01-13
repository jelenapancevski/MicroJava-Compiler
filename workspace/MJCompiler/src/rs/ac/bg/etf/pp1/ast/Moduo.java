// generated with ast extension for cup
// version 0.8
// 13/0/2023 5:23:51


package rs.ac.bg.etf.pp1.ast;

public class Moduo extends Mulop {

    public Moduo () {
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
        buffer.append("Moduo(\n");

        buffer.append(tab);
        buffer.append(") [Moduo]");
        return buffer.toString();
    }
}
