package org.pastiche.wiki.parser;

public class Centered extends CompositeElement
{
    private Centered()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitCentered(this);
    }

    private void add(Element e)
    {
        getContents().add(e);
    }

    public void add(Text element)
    {
        getContents().add(element);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        Element element = (Element) elementList.getLast();

        if (element instanceof Centered)
        {
            java.util.Iterator i = getContents().iterator();
            while (i.hasNext())
            {
                ((Centered) element).add((Element) i.next());
            }
        }
        else
        {
            elementList.add(this);
        }
    }

    public static Centered newInstance(String initialContents)
    {
        Centered centered = new Centered();
        initialContents = initialContents.trim();
        initialContents = initialContents.substring(1, initialContents.length() - 1).trim();
        centered.add(Text.newInstance(initialContents));
        return centered;
    }
}
