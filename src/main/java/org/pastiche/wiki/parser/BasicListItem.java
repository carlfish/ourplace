package org.pastiche.wiki.parser;

public class BasicListItem extends ListItem
{
    private BasicListItem()
    {
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitBasicListItem(this);
    }

    public static BasicListItem newInstance(String initialContent)
    {
        BasicListItem li = new BasicListItem();
        li.add(Text.newInstance(stripListPrefix(initialContent)));
        return li;
    }

    private static String stripListPrefix(String line)
    {
        for (int p = 0; p < line.length(); p++)
        {
            if (line.charAt(p) != '\t')
            {
                return line.substring(p + 2);
            }
        }

        throw new RuntimeException("Not a list line: " + line);
    }
}
