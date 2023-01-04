// generated with ast extension for cup
// version 0.8
// 4/0/2023 1:46:18


package rs.ac.bg.etf.pp1.ast;

public class ActParsList extends ActPars {

    private ActPars ActPars;
    private Parameter Parameter;

    public ActParsList (ActPars ActPars, Parameter Parameter) {
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
        this.Parameter=Parameter;
        if(Parameter!=null) Parameter.setParent(this);
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public Parameter getParameter() {
        return Parameter;
    }

    public void setParameter(Parameter Parameter) {
        this.Parameter=Parameter;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActPars!=null) ActPars.accept(visitor);
        if(Parameter!=null) Parameter.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
        if(Parameter!=null) Parameter.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        if(Parameter!=null) Parameter.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsList(\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Parameter!=null)
            buffer.append(Parameter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsList]");
        return buffer.toString();
    }
}
