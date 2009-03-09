package org.pastiche.wiki.parser;

public class Paragraph extends CompositeElement
{
    private Paragraph()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitParagraph(this);
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

        if (element instanceof Paragraph)
        {
            java.util.Iterator i = getContents().iterator();
            while (i.hasNext())
            {
                ((Paragraph) element).add((Element) i.next());
            }
        }
        else
        {
            elementList.add(this);
        }
    }

    public static Paragraph newInstance(String initialContents)
    {
        Paragraph paragraph = new Paragraph();
        paragraph.add(Text.newInstance(initialContents));
        return paragraph;
    }
}
