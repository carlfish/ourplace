package org.pastiche.wiki.parser;

public class BlockQuote extends CompositeElement
{
    private BlockQuote()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitBlockQuote(this);
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

        if (element instanceof BlockQuote)
        {
            java.util.Iterator i = getContents().iterator();
            while (i.hasNext())
            {
                ((BlockQuote) element).add((Element) i.next());
            }
        }
        else
        {
            elementList.add(this);
        }
    }

    public static BlockQuote newInstance(String initialContents)
    {
        BlockQuote blockQuote = new BlockQuote();
        initialContents = initialContents.trim();
        initialContents = initialContents.substring(1, initialContents.length() - 1).trim();
        blockQuote.add(Text.newInstance(initialContents));
        return blockQuote;
    }
}
