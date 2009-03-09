package org.pastiche.wiki.parser;

public class UnorderedList extends List
{
    private UnorderedList(int depth)
    {
        super(depth);
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitUnorderedList(this);
    }

    public void add(BasicListItem element)
    {
        getContents().add(element);
    }

    public static UnorderedList newInstance(int depth, String initialContents)
    {
        UnorderedList ul = new UnorderedList(depth);
        ul.add(BasicListItem.newInstance(initialContents));
        return ul;
    }
}
