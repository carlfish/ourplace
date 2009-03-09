package org.pastiche.wiki.parser;

public class BlankLine extends Element
{
    private BlankLine()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitBlankLine(this);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        Element element = (Element) elementList.getLast();

        if (element instanceof Preformatted)
            ((Preformatted) element).add(this);
        else if (element instanceof List)
            ((List) element).add(this);
        else if (element instanceof BlankLine)
            ; // blank lines merge together
        else if (element instanceof Text)
            ((Text) element).addLine("");
        else
            elementList.add(this);

    }

    public static BlankLine newInstance()
    {
        return new BlankLine();
    }
}
