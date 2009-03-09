package org.pastiche.wiki.parser;

public class Preformatted extends CompositeElement
{
    private Preformatted()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitPreformatted(this);
    }

    public void add(BlankLine element)
    {
        getContents().add(element);
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

        if (element instanceof Preformatted)
        {
            java.util.Iterator i = getContents().iterator();
            while (i.hasNext())
            {
                ((Preformatted) element).add((Element) i.next());
            }
        }
        else
        {
            elementList.add(this);
        }
    }

    public static Preformatted newInstance(String initialContents)
    {
        Preformatted pre = new Preformatted();
        pre.add(Text.newInstance(initialContents));
        return pre;
    }
}
