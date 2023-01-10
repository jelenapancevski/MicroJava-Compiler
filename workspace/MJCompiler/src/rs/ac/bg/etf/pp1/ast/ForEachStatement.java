// generated with ast extension for cup
// version 0.8
// 10/0/2023 18:31:26


package rs.ac.bg.etf.pp1.ast;

public class ForEachStatement extends Statement {

    private Designator Designator;
    private ForEach ForEach;
    private String ident;
    private ForStatements ForStatements;

    public ForEachStatement (Designator Designator, ForEach ForEach, String ident, ForStatements ForStatements) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.ForEach=ForEach;
        if(ForEach!=null) ForEach.setParent(this);
        this.ident=ident;
        this.ForStatements=ForStatements;
        if(ForStatements!=null) ForStatements.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public ForEach getForEach() {
        return ForEach;
    }

    public void setForEach(ForEach ForEach) {
        this.ForEach=ForEach;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident=ident;
    }

    public ForStatements getForStatements() {
        return ForStatements;
    }

    public void setForStatements(ForStatements ForStatements) {
        this.ForStatements=ForStatements;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(ForEach!=null) ForEach.accept(visitor);
        if(ForStatements!=null) ForStatements.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(ForEach!=null) ForEach.traverseTopDown(visitor);
        if(ForStatements!=null) ForStatements.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(ForEach!=null) ForEach.traverseBottomUp(visitor);
        if(ForStatements!=null) ForStatements.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForEachStatement(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForEach!=null)
            buffer.append(ForEach.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+ident);
        buffer.append("\n");

        if(ForStatements!=null)
            buffer.append(ForStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForEachStatement]");
        return buffer.toString();
    }
}
