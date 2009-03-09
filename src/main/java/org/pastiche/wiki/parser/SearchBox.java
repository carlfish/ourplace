package org.pastiche.wiki.parser;

public class SearchBox extends Element
{
    private SearchBox()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitSearchBox(this);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        elementList.add(this);
    }

    public static SearchBox newInstance()
    {
        return new SearchBox();
    }
}
