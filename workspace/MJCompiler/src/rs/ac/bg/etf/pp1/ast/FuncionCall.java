// generated with ast extension for cup
// version 0.8
// 15/0/2023 16:5:54


package rs.ac.bg.etf.pp1.ast;

public class FuncionCall extends DesignatorStatement {

    private FunctionName FunctionName;
    private ActParams ActParams;

    public FuncionCall (FunctionName FunctionName, ActParams ActParams) {
        this.FunctionName=FunctionName;
        if(FunctionName!=null) FunctionName.setParent(this);
        this.ActParams=ActParams;
        if(ActParams!=null) ActParams.setParent(this);
    }

    public FunctionName getFunctionName() {
        return FunctionName;
    }

    public void setFunctionName(FunctionName FunctionName) {
        this.FunctionName=FunctionName;
    }

    public ActParams getActParams() {
        return ActParams;
    }

    public void setActParams(ActParams ActParams) {
        this.ActParams=ActParams;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FunctionName!=null) FunctionName.accept(visitor);
        if(ActParams!=null) ActParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FunctionName!=null) FunctionName.traverseTopDown(visitor);
        if(ActParams!=null) ActParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FunctionName!=null) FunctionName.traverseBottomUp(visitor);
        if(ActParams!=null) ActParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FuncionCall(\n");

        if(FunctionName!=null)
            buffer.append(FunctionName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParams!=null)
            buffer.append(ActParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FuncionCall]");
        return buffer.toString();
    }
}
