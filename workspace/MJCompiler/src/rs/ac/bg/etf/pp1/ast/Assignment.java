// generated with ast extension for cup
// version 0.8
// 11/0/2023 6:6:41


package rs.ac.bg.etf.pp1.ast;

public class Assignment extends DesignatorStatement {

    private Designator Designator;
    private AssignmentExpr AssignmentExpr;

    public Assignment (Designator Designator, AssignmentExpr AssignmentExpr) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.AssignmentExpr=AssignmentExpr;
        if(AssignmentExpr!=null) AssignmentExpr.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public AssignmentExpr getAssignmentExpr() {
        return AssignmentExpr;
    }

    public void setAssignmentExpr(AssignmentExpr AssignmentExpr) {
        this.AssignmentExpr=AssignmentExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(AssignmentExpr!=null) AssignmentExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(AssignmentExpr!=null) AssignmentExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(AssignmentExpr!=null) AssignmentExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Assignment(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignmentExpr!=null)
            buffer.append(AssignmentExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Assignment]");
        return buffer.toString();
    }
}
