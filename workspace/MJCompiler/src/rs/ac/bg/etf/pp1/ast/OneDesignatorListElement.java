// generated with ast extension for cup
// version 0.8
// 12/0/2023 20:28:55


package rs.ac.bg.etf.pp1.ast;

public class OneDesignatorListElement extends DesignatorList {

    private DesignatorListElement DesignatorListElement;

    public OneDesignatorListElement (DesignatorListElement DesignatorListElement) {
        this.DesignatorListElement=DesignatorListElement;
        if(DesignatorListElement!=null) DesignatorListElement.setParent(this);
    }

    public DesignatorListElement getDesignatorListElement() {
        return DesignatorListElement;
    }

    public void setDesignatorListElement(DesignatorListElement DesignatorListElement) {
        this.DesignatorListElement=DesignatorListElement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorListElement!=null) DesignatorListElement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorListElement!=null) DesignatorListElement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorListElement!=null) DesignatorListElement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneDesignatorListElement(\n");

        if(DesignatorListElement!=null)
            buffer.append(DesignatorListElement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneDesignatorListElement]");
        return buffer.toString();
    }
}
