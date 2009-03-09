package org.pastiche.wiki.parser;

public abstract class List extends CompositeElement
{
    private int depth = 0;

    protected List(int depth)
    {
        this.depth = depth;
    }

    public void add(BlankLine element)
    {
        getContents().add(element);
    }

    private void add(Element element)
    {
        getContents().add(element);
    }

    public void add(List element)
    {
        getContents().add(element);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        Element element = (Element) elementList.getLast();

        if (element instanceof List)
        {
            if (((List) element).getDepth() < getDepth())
            {
                ((List) element).add(this);
            }
            else if (element.getClass() == getClass()
                    && ((List) element).getDepth() == getDepth())
            {

                java.util.Iterator i = getContents().iterator();
                while (i.hasNext())
                {
                    ((List) element).add((Element) i.next());
                }
            }
            else
            {
                elementList.add(this);
            }
        }
        else if (element instanceof ListItem)
        {
            ((ListItem) element).add(this);
        }
        else
        {
            elementList.add(this);
        }
    }

    protected static ListItem createListItem(String contents)
    {
        for (int depth = 0; depth < contents.length(); depth++)
        {
            if (contents.charAt(depth) != '\t')
            {
                return BasicListItem.newInstance(contents.substring(depth + 2));
            }
        }

        throw new RuntimeException("Attempted to create malformed list line (no content)");
    }

    public void decrementDepth()
    {
        --depth;
    }

    public int getDepth()
    {
        return depth;
    }

    private int getDepth(String contents)
    {
        for (int depth = 0; depth < contents.length(); depth++)
        {
            if (contents.charAt(depth) != '\t')
            {
                return depth;
            }
        }
        throw new RuntimeException("Blank line interpreted as list item");
    }
}
