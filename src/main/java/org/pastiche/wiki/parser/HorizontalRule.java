package org.pastiche.wiki.parser;

public class HorizontalRule extends Element
{
    public void accept(ElementVisitor visitor)
    {
        visitor.visitHorizontalRule(this);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        if ((Element) elementList.getLast() instanceof HorizontalRule)
        {
            return;
        }
        else
        {
            elementList.add(this);
        }
    }

    public static HorizontalRule newInstance()
    {
        return new HorizontalRule();
    }
}
