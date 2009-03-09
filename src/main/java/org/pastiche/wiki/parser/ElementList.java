package org.pastiche.wiki.parser;

import java.util.LinkedList;

public class ElementList
{
    private LinkedList elements = new LinkedList();

    public void add(Element element)
    {
        if (elements.size() == 0)
        {
            elements.add(element);
        }
        else
        {
            element.appendTo(elements);
        }
    }

    public Element getLastElement()
    {
        return (Element) elements.getLast();
    }

    public java.util.Iterator iterator()
    {
        return elements.iterator();
    }
}
