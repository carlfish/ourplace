package org.pastiche.wiki.parser;

public class WikiParser
{
    private org.pastiche.wiki.PageStore store = null;
    private String content = null;

    public WikiParser(String content, org.pastiche.wiki.PageStore store)
    {
        this.content = content;
        this.store = store;
    }

    private int getIndexOfFirstNonTab(String line)
    {
        for (int i = 0; i < line.length(); i++)
        {
            if (line.charAt(i) == '\t')
                continue;

            return i;
        }

        throw new RuntimeException("Line does not contain a non-tab");
    }

    private String getNextLine()
    {
        if (content.length() == 0)
            return null;

        int index;

        if ((index = content.indexOf('\n')) == -1)
        {
            String line = content;
            content = "";
            return line;
        }

        int nextLineStart = index + 1;
        int lineEnd = index;

        if ((index > 0) && (content.charAt(index - 1) == '\r'))
        {
            lineEnd = index - 1;
        }

        String line = content.substring(0, lineEnd);
        content = content.substring(nextLineStart);
        return line;
    }

    private boolean isBlankLine(String line)
    {
        for (int i = 0; i < line.length(); i++)
        {
            if (!Character.isWhitespace(line.charAt(i)))
                return false;
        }

        return true;
    }

    private boolean isBlockQuote(String line)
    {
        line = line.trim();
        return line.startsWith("\" ") && line.endsWith(" \"");
    }

    private boolean isCentered(String line)
    {
        line = line.trim();
        return line.startsWith("> ") && line.endsWith(" <");
    }

    private boolean isDefinitionList(String line)
    {
        return line.startsWith("(") && line.indexOf(')') > 1;
    }

    private boolean isHorizontalRule(String line)
    {
        line = line.trim();
        if (!line.startsWith("----"))
            return false;

        for (int i = 4; i < line.length(); i++)
        {
            if (line.charAt(i) == '-')
            {
                continue;
            }
            return false;
        }

        return true;
    }

    private boolean isIndex(String line)
    {
        line = line.trim();
        if (!(line.startsWith("[") && line.endsWith("]")))
            return false;

        line = line.substring(1, line.length() - 1);
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(line, ":");
        return (tokens.nextToken().equalsIgnoreCase("index") &&
                tokens.countTokens() < 3);
    }

    public String parse()
    {
        Document document = Document.newInstance();

        String line;
        while ((line = getNextLine()) != null)
        {
            if (line.length() == 0)
                document.add(BlankLine.newInstance());
            else if (Character.isWhitespace(line.charAt(0)))
                processWhitespaceLine(document, line);
            else if (isHorizontalRule(line))
                document.add(HorizontalRule.newInstance());
            else if (isCentered(line))
                document.add(Centered.newInstance(line));
            else if (isBlockQuote(line))
                document.add(BlockQuote.newInstance(line));
            else if (isIndex(line))
                document.add(Index.newInstance(line));
            else if (line.trim().equalsIgnoreCase("[search]"))
                document.add(SearchBox.newInstance());
            else
                document.add(Paragraph.newInstance(line));
        }

        BasicHtmlConverter converter = new BasicHtmlConverter(store);
        converter.visitDocument(document);
        return converter.getContents();
    }

    private void processWhitespaceLine(Document document, String line)
    {
        if (isBlankLine(line))
        {
            document.add(BlankLine.newInstance());
            return;
        }

        if (line.startsWith("\t"))
        {
            int depth = getIndexOfFirstNonTab(line);

            if ((depth + 1 < line.length()))
            {
                if (line.charAt(depth) == '*'
                        && Character.isWhitespace(line.charAt(depth + 1)))
                {
                    document.add(UnorderedList.newInstance(depth, line));
                    return;
                }
                else if (
                        Character.isDigit(line.charAt(depth))
                                && Character.isWhitespace(line.charAt(depth + 1)))
                {
                    document.add(OrderedList.newInstance(depth, line));
                    return;
                }
                else if (isDefinitionList(line.substring(depth)))
                {
                    document.add(DefinitionList.newInstance(depth, line));
                    return;
                }
            }
        }

        document.add(Preformatted.newInstance(line));
        return;
    }
}
