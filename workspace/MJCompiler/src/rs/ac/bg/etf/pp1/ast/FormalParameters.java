// generated with ast extension for cup
// version 0.8
// 12/0/2023 20:28:55


package rs.ac.bg.etf.pp1.ast;

public class FormalParameters extends FormPars {

    private FormPars FormPars;
    private OneParam OneParam;

    public FormalParameters (FormPars FormPars, OneParam OneParam) {
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
        this.OneParam=OneParam;
        if(OneParam!=null) OneParam.setParent(this);
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
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
        if(FormPars!=null) FormPars.accept(visitor);
        if(OneParam!=null) OneParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
        if(OneParam!=null) OneParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        if(OneParam!=null) OneParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParameters(\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OneParam!=null)
            buffer.append(OneParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParameters]");
        return buffer.toString();
    }
}
