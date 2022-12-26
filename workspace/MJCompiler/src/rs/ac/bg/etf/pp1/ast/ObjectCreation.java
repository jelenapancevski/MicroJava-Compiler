// generated with ast extension for cup
// version 0.8
// 26/11/2022 3:30:20


package rs.ac.bg.etf.pp1.ast;

public class ObjectCreation extends Factor {

    private Type Type;
    private ActParams ActParams;

    public ObjectCreation (Type Type, ActParams ActParams) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ActParams=ActParams;
        if(ActParams!=null) ActParams.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
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
        if(Type!=null) Type.accept(visitor);
        if(ActParams!=null) ActParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ActParams!=null) ActParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ActParams!=null) ActParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ObjectCreation(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParams!=null)
            buffer.append(ActParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ObjectCreation]");
        return buffer.toString();
    }
}
