// generated with ast extension for cup
// version 0.8
// 15/0/2023 16:5:54


package rs.ac.bg.etf.pp1.ast;

public class Designators extends DesignatorList {

    private DesignatorList DesignatorList;
    private DesignatorListElement DesignatorListElement;

    public Designators (DesignatorList DesignatorList, DesignatorListElement DesignatorListElement) {
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
        this.DesignatorListElement=DesignatorListElement;
        if(DesignatorListElement!=null) DesignatorListElement.setParent(this);
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
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
        if(DesignatorList!=null) DesignatorList.accept(visitor);
        if(DesignatorListElement!=null) DesignatorListElement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
        if(DesignatorListElement!=null) DesignatorListElement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        if(DesignatorListElement!=null) DesignatorListElement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designators(\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorListElement!=null)
            buffer.append(DesignatorListElement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designators]");
        return buffer.toString();
    }
}
