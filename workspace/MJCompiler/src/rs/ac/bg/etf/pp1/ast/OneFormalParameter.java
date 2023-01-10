// generated with ast extension for cup
// version 0.8
// 9/0/2023 23:9:27


package rs.ac.bg.etf.pp1.ast;

public class OneFormalParameter extends FormPars {

    private OneParameter OneParameter;

    public OneFormalParameter (OneParameter OneParameter) {
        this.OneParameter=OneParameter;
        if(OneParameter!=null) OneParameter.setParent(this);
    }

    public OneParameter getOneParameter() {
        return OneParameter;
    }

    public void setOneParameter(OneParameter OneParameter) {
        this.OneParameter=OneParameter;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OneParameter!=null) OneParameter.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OneParameter!=null) OneParameter.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OneParameter!=null) OneParameter.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneFormalParameter(\n");

        if(OneParameter!=null)
            buffer.append(OneParameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneFormalParameter]");
        return buffer.toString();
    }
}
