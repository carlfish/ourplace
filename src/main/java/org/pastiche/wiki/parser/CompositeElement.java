package org.pastiche.wiki.parser;

import java.util.LinkedList;

public abstract class CompositeElement extends Element
{
    private ElementList contents = new ElementList();

    protected ElementList getContents()
    {
        return contents;
    }

    public void visitContents(ElementVisitor visitor)
    {
        java.util.Iterator i = getContents().iterator();
        while (i.hasNext())
        {
            ((Element) i.next()).accept(visitor);
        }
    }
}
