package org.pastiche.wiki.parser;

public class Document extends CompositeElement
{
    private Document()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitDocument(this);
    }

    public void add(BlankLine element)
    {
        getContents().add(element);
    }

    public void add(BlockQuote element)
    {
        getContents().add(element);
    }

    public void add(Centered element)
    {
        getContents().add(element);
    }

    public void add(HorizontalRule element)
    {
        getContents().add(element);
    }

    public void add(Index element)
    {
        getContents().add(element);
    }

    public void add(List element)
    {
        getContents().add(element);
    }

    public void add(Paragraph element)
    {
        getContents().add(element);
    }

    public void add(Preformatted element)
    {
        getContents().add(element);
    }

    public void add(SearchBox element)
    {
        getContents().add(element);
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        elementList.add(this);
    }

    public static Document newInstance()
    {
        return new Document();
    }
}
