// generated with ast extension for cup
// version 0.8
// 7/0/2023 0:53:57


package rs.ac.bg.etf.pp1.ast;

public class NoDesignatorElement extends DesignatorListElement {

    public NoDesignatorElement () {
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
        buffer.append("NoDesignatorElement(\n");

        buffer.append(tab);
        buffer.append(") [NoDesignatorElement]");
        return buffer.toString();
    }
}
