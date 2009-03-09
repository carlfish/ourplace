package org.pastiche.wiki.parser;

public class Text extends Element
{
    private String contents;

    private Text(String contents)
    {
        this.contents = contents;
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitText(this);
    }

    protected void addLine(String additionalContents)
    {
        this.contents = contents + "\n" + additionalContents;
    }

    public void appendTo(java.util.LinkedList elementList)
    {
        Element element = (Element) elementList.getLast();

        if (element instanceof Text)
            ((Text) element).addLine(contents);
        else
            elementList.add(this);
    }

    public String getText()
    {
        return contents;
    }

    public static Text newInstance(String initialContents)
    {
        return new Text(initialContents);
    }
}
