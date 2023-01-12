// generated with ast extension for cup
// version 0.8
// 12/0/2023 20:28:55


package rs.ac.bg.etf.pp1.ast;

public class ForEachLoop extends ForEach {

    private String ident;

    public ForEachLoop (String ident) {
        this.ident=ident;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident=ident;
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
        buffer.append("ForEachLoop(\n");

        buffer.append(" "+tab+ident);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForEachLoop]");
        return buffer.toString();
    }
}
