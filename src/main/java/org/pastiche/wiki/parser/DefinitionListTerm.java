package org.pastiche.wiki.parser;

public class DefinitionListTerm extends CompositeElement
{
    private DefinitionListTerm()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitDefinitionListTerm(this);
    }

    public void add(Text element)
    {
        getContents().add(element);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        elementList.add(this);
    }

    public static DefinitionListTerm newInstance(String initialContent)
    {
        DefinitionListTerm dt = new DefinitionListTerm();
        dt.add(Text.newInstance(initialContent));
        return dt;
    }
}
