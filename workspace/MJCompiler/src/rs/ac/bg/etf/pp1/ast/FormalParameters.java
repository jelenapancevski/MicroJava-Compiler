// generated with ast extension for cup
// version 0.8
// 27/11/2022 18:16:49


package rs.ac.bg.etf.pp1.ast;

public class FormalParameters extends FormPars {

    private FormPars FormPars;
    private OneParameter OneParameter;

    public FormalParameters (FormPars FormPars, OneParameter OneParameter) {
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
        this.OneParameter=OneParameter;
        if(OneParameter!=null) OneParameter.setParent(this);
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
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
        if(FormPars!=null) FormPars.accept(visitor);
        if(OneParameter!=null) OneParameter.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
        if(OneParameter!=null) OneParameter.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        if(OneParameter!=null) OneParameter.traverseBottomUp(visitor);
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

        if(OneParameter!=null)
            buffer.append(OneParameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParameters]");
        return buffer.toString();
    }
}
