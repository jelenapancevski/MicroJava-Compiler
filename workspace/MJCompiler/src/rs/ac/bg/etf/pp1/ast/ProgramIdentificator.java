// generated with ast extension for cup
// version 0.8
// 15/0/2023 16:5:54


package rs.ac.bg.etf.pp1.ast;

public class ProgramIdentificator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String progIdent;

    public ProgramIdentificator (String progIdent) {
        this.progIdent=progIdent;
    }

    public String getProgIdent() {
        return progIdent;
    }

    public void setProgIdent(String progIdent) {
        this.progIdent=progIdent;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
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
        buffer.append("ProgramIdentificator(\n");

        buffer.append(" "+tab+progIdent);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramIdentificator]");
        return buffer.toString();
    }
}
