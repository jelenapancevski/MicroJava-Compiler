// generated with ast extension for cup
// version 0.8
// 15/0/2023 16:5:54


package rs.ac.bg.etf.pp1.ast;

public class OneFormalParameter extends FormPars {

    private OneParam OneParam;

    public OneFormalParameter (OneParam OneParam) {
        this.OneParam=OneParam;
        if(OneParam!=null) OneParam.setParent(this);
    }

    public OneParam getOneParam() {
        return OneParam;
    }

    public void setOneParam(OneParam OneParam) {
        this.OneParam=OneParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OneParam!=null) OneParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OneParam!=null) OneParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OneParam!=null) OneParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OneFormalParameter(\n");

        if(OneParam!=null)
            buffer.append(OneParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OneFormalParameter]");
        return buffer.toString();
    }
}
