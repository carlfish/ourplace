package org.pastiche.wiki.parser;

public class OrderedList extends List
{
    private OrderedList(int depth)
    {
        super(depth);
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitOrderedList(this);
    }

    public void add(BasicListItem element)
    {
        getContents().add(element);
    }

    public static OrderedList newInstance(int depth, String initialContents)
    {
        OrderedList ol = new OrderedList(depth);
        ol.add(BasicListItem.newInstance(initialContents));
        return ol;
    }
}
