// generated with ast extension for cup
// version 0.8
// 15/0/2023 16:5:54


package rs.ac.bg.etf.pp1.ast;

public class MultipleAssignment extends DesignatorStatement {

    private DesignatorList DesignatorList;
    private MultipleDesignator MultipleDesignator;

    public MultipleAssignment (DesignatorList DesignatorList, MultipleDesignator MultipleDesignator) {
        this.DesignatorList=DesignatorList;
        if(DesignatorList!=null) DesignatorList.setParent(this);
        this.MultipleDesignator=MultipleDesignator;
        if(MultipleDesignator!=null) MultipleDesignator.setParent(this);
    }

    public DesignatorList getDesignatorList() {
        return DesignatorList;
    }

    public void setDesignatorList(DesignatorList DesignatorList) {
        this.DesignatorList=DesignatorList;
    }

    public MultipleDesignator getMultipleDesignator() {
        return MultipleDesignator;
    }

    public void setMultipleDesignator(MultipleDesignator MultipleDesignator) {
        this.MultipleDesignator=MultipleDesignator;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.accept(visitor);
        if(MultipleDesignator!=null) MultipleDesignator.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorList!=null) DesignatorList.traverseTopDown(visitor);
        if(MultipleDesignator!=null) MultipleDesignator.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorList!=null) DesignatorList.traverseBottomUp(visitor);
        if(MultipleDesignator!=null) MultipleDesignator.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleAssignment(\n");

        if(DesignatorList!=null)
            buffer.append(DesignatorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MultipleDesignator!=null)
            buffer.append(MultipleDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleAssignment]");
        return buffer.toString();
    }
}
