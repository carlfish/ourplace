package org.pastiche.wiki.parser;

public abstract class ListItem extends CompositeElement
{
    public void add(List element)
    {
        getContents().add(element);
    }

    public void add(Text element)
    {
        getContents().add(element);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        elementList.add(this);
    }
}
