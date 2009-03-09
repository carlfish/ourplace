package org.pastiche.wiki.parser;

public class Index extends Element
{
    private String searchTerm;
    private boolean exclude;

    private Index()
    {
        this.searchTerm = "";
        this.exclude = false;
    }

    private Index(String searchTerm, boolean exclude)
    {
        this.searchTerm = searchTerm;
        this.exclude = exclude;
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitIndex(this);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        elementList.add(this);
    }

    public java.util.SortedSet getIndexedPages(
            org.pastiche.wiki.PageStore pageStore)
    {
        if (searchTerm == null || searchTerm.equals(""))
            return pageStore.getAllPages(new org.pastiche.wiki.AscendingAlphaPageComparator());

        if (exclude)
            return pageStore.findPagesNotContaining(
                    searchTerm,
                    new org.pastiche.wiki.AscendingAlphaPageComparator());

        return pageStore.findPagesContaining(
                searchTerm,
                new org.pastiche.wiki.AscendingAlphaPageComparator());
    }

    public static Index newInstance(String line)
    {
        line = line.trim();
        int idx = line.indexOf(':');
        if (idx == -1)
        {
            return new Index();
        }
        else
        {
            String searchTerm = line.substring(idx + 1, line.length() - 1);
            if (searchTerm.startsWith("!"))
            {
                return new Index(searchTerm.substring(1), true);
            }
            else
            {
                return new Index(searchTerm, false);
            }
        }
    }

    public static Index newInstance(String searchTerm, boolean exclude)
    {
        return new Index(searchTerm, exclude);
    }
}
