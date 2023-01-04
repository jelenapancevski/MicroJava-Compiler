// generated with ast extension for cup
// version 0.8
// 4/0/2023 1:46:18


package rs.ac.bg.etf.pp1.ast;

public class ErrorVariableSemiColon extends VarDecl {

    public ErrorVariableSemiColon () {
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
        buffer.append("ErrorVariableSemiColon(\n");

        buffer.append(tab);
        buffer.append(") [ErrorVariableSemiColon]");
        return buffer.toString();
    }
}
