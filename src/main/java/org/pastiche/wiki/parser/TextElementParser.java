package org.pastiche.wiki.parser;

import org.pastiche.wiki.Page;
import org.pastiche.wiki.BadPageNameException;

public class TextElementParser
{
    private String content = null;
    private java.util.Stack tagStack = new java.util.Stack();
    private int position = 0;
    private StringBuffer response = null;

    private char getPreviousChar()
    {
        if (position == 0)
            return '\n';

        return content.charAt(position - 1);
    }

    private void handleApostrophes()
    {
        int numberOfApostrophes = getNumberOfApostrophes(position);

        switch (numberOfApostrophes)
        {
            case 6:
                toggleTag(SUPPRESS_LINKS_TAG);
                break;
            case 5:
                handleFiveApostrophes();
                break;
            case 4:
                toggleTag("em");
                toggleTag("em");
                break;
            case 3:
                toggleTag("strong");
                break;
            case 2:
                toggleTag("em");
                break;
            case 1:
                response.append("'");
                break;
            default:
        }

        position += (numberOfApostrophes - 1);
    }

    private void handleNewLine()
    {
        if (context.isPreformatted())
        {
            response.append('\n');
        }
        else
        {
            response.append("<br />\n");
        }
    }

    private void handleNewWord()
    {
        int i = position;

        for (; i < content.length(); i++)
        {
            if (!Character.isLetterOrDigit(content.charAt(i)))
            {
                break;
            }
        }

        String word = content.substring(position, i);

        if (Page.isValidPageName(word) && !tagStack.contains(SUPPRESS_LINKS_TAG))
        {
            if (context.getPageStore().containsName(word))
            {
                try
                {
                    response.append(
                            "<a href=\""
                                    + word
                                    + "\" title=\""
                                    + context.getPageStore().retrieve(word).getSummaryWithEscapedQuotes()
                                    + "\">"
                                    + word
                                    + "</a>");
                }
                catch (BadPageNameException bpne)
                {
                    throw new RuntimeException("IMPOSSIBLE ERROR: good page name turned into bad page name.");
                }
            }
            else
            {
                response.append(word + "<a href=\"edit?page=" + word + "\">?</a>");
            }
            position = i - 1;
        }
        else
        {
            response.append(content.charAt(position));
        }
    }


    private void handlePragma()
    {
        String pragma = getPragmaWordAt(position + 1);

        if (pragma == null)
        {
            response.append(content.charAt(position));
            return;
        }

        String pragmaContent = getPragmaContentAt(position + pragma.length() + 2);

        if ((pragmaContent == null) || pragmaContent.equals(""))
        {
            if (pragma.equalsIgnoreCase("PAGECOUNT"))
            {
                response.append(context.getPageStore().getPageCount());
                position += 2 + pragma.length();
            }
            response.append(content.charAt(position));
            return;
        }
        else
        {
            if (pragma.equalsIgnoreCase("URL"))
            {
                response.append("<a href=\"" + pragmaContent + "\">" + pragmaContent + "</a>");
            }
            else if (pragma.equalsIgnoreCase("IMG"))
            {
                response.append("<img src=\"" + pragmaContent + "\" />");
            }
            else if (pragma.equalsIgnoreCase("IMDB"))
            {
                response.append("<a href=\"http://us.imdb.com/Title?" + pragmaContent + "\">(Internet Movie Database)</a>");
            }
            else if (pragma.equalsIgnoreCase("ISBN"))
            {
                response.append("<a href=\"http://www.amazon.com/exec/obidos/ISBN=" + extractDigits(pragmaContent) + "\">ISBN: " + pragmaContent + "</a>");
            }
            else if (pragma.equalsIgnoreCase("LJ"))
            {
                response.append("<a href=\"http://www.livejournal.com/userinfo.bml?user=" + pragmaContent + "\"><img width=\"17\" src=\"http://img.livejournal.com/userinfo.gif\" height=\"17\" align=\"absmiddle\" border=\"0\"></a><a href=\"http://www.livejournal.com/users/" + pragmaContent + "/\">" + pragmaContent + "</a>");
            }
            else
            {
                response.append(content.charAt(position));
                return;
            }
            position += 1 + pragma.length() + 1 + pragmaContent.length();
        }
    }


    private ParserContext context = null;
    public static final String SUPPRESS_LINKS_TAG = "__NOLINK__";

    public TextElementParser(String content, ParserContext context)
    {
        this.content = content;
        this.context = context;
    }

    private String cleanSummary(String content)
    {
        if (content == null)
            return "";

        int position = 0;
        StringBuffer response = new StringBuffer();
        int i = position;

        for (; i < content.length(); i++)
        {
            char c = content.charAt(i);
            if (c == '<')
            {
                response.append("&lt;");
            }
            else if (c == '>')
            {
                response.append("&gt;");
            }
            else if (c == '&')
            {
                response.append("&amp;");
            }
            else if (c == '"')
            {
                response.append("&quot;");
            }
            else
            {
                response.append(c);
            }
        }
        return response.toString();
    }

    private void closeTag(String tagName)
    {
        String oldTag;

        while ((oldTag = (String) tagStack.pop()) != null)
        {
            if (!oldTag.equals(SUPPRESS_LINKS_TAG))
            {
                response.append("</" + getFirstWord(oldTag) + ">");
            }

            if (oldTag.equals(tagName))
            {
                return;
            }
        }

        throw new RuntimeException("Tried to close a tag that wasn't open");
    }

    private String extractDigits(String stringContainingNumbers)
    {
        StringBuffer digits = new StringBuffer(stringContainingNumbers.length());
        for (int i = 0; i < stringContainingNumbers.length(); i++)
        {
            if (Character.isDigit(stringContainingNumbers.charAt(i)))
                digits.append(stringContainingNumbers.charAt(i));
        }
        return digits.toString();
    }

    private String getFirstWord(String s)
    {
        return (s.indexOf(' ') == -1) ? s : s.substring(0, s.indexOf(' '));
    }

    private int getNumberOfApostrophes(int start)
    {
        int numberOfApostrophes = 1;

        for (int i = start + 1; i < content.length() && numberOfApostrophes <= 6; i++)
        {
            if (!(content.charAt(i) == '\''))
            {
                break;
            }

            numberOfApostrophes++;
        }

        return numberOfApostrophes;
    }

    private String getPragmaContentAt(int start)
    {
        if (content.charAt(start - 1) != ':')
            return null;

        for (int i = start; i < content.length(); i++)
        {
            if (content.charAt(i) == ']')
            {
                return content.substring(start, i);
            }

            if (content.charAt(i) == '"' || content.charAt(i) == '\n')
            {
                return null;
            }
        }

        return null;
    }

    private String getPragmaWordAt(int start)
    {
        for (int i = (start); i < content.length(); i++)
        {
            if (content.charAt(i) == ':' || content.charAt(i) == ']')
            {
                return content.substring(start, i);
            }

            if (!Character.isLetter(content.charAt(i)))
            {
                return null;
            }
        }

        return null;
    }

    private void handleCharacter(char c)
    {
        if (c == '<')
        {
            response.append("&lt;");
        }
        else if (c == '>')
        {
            response.append("&gt;");
        }
        else if (c == '&')
        {
            response.append("&amp;");
        }
        else if (c == '\r')
        {
        }
        else if (c == '\n')
        {
            handleNewLine();
        }
        else if (c == '\'')
        {
            handleApostrophes();
        }
        else if (c == '[' && !tagStack.contains(SUPPRESS_LINKS_TAG))
        {
            handlePragma();
        }
        else if (
                Character.isLetter(c) && !Character.isLetterOrDigit(getPreviousChar()))
        {
            handleNewWord();
        }
        else
        {
            response.append(c);
        }
    }

    private void handleFiveApostrophes()
    {
        int lastEmLocation = tagStack.lastIndexOf("em");
        int lastStrongLocation = tagStack.lastIndexOf("strong");

        if (lastEmLocation == -1 && lastStrongLocation == -1)
        {
            if (nextApostrophesAreStrong(position + 5))
            {
                openTag("em");
                openTag("strong");
            }
            else
            {
                openTag("strong");
                openTag("em");
            }
        }
        else if (lastEmLocation == -1)
        {
            closeTag("strong");
            openTag("em");
        }
        else if (lastStrongLocation == -1)
        {
            closeTag("em");
            openTag("strong");
        }
        else if (lastEmLocation > lastStrongLocation)
        {
            closeTag("em");
            closeTag("strong");
        }
        else
        {
            closeTag("strong");
            closeTag("em");
        }
    }

    private boolean nextApostrophesAreStrong(int start)
    {
        for (int i = start; i < content.length(); i++)
        {
            if (content.charAt(i) == '\'' && (i + 2) < content.length())
            {
                int numberOfApostrophes = getNumberOfApostrophes(i);
                if ((numberOfApostrophes == 3) || (numberOfApostrophes == 5))
                {
                    return true;
                }
                else if (numberOfApostrophes == 2)
                {
                    return false;
                }
            }
            if ((content.charAt(i) == '\n') && (content.charAt(i - 1) == '\n'))
            {
                return false;
            }
        }

        return false;
    }

    private void openTag(String tagName)
    {
        if (!tagName.equals(SUPPRESS_LINKS_TAG))
        {
            response.append("<" + tagName + ">");
        }

        tagStack.push(tagName);
    }

    public String parse()
    {
        if (content == null)
            return "";

        response = new StringBuffer((int) (content.length() * 1.2));

        for (position = 0; position < content.length(); position++)
        {
            handleCharacter(content.charAt(position));
        }

        while (tagStack.size() != 0)
        {
            closeTag((String) tagStack.peek());
        }

        return response.toString();
    }

    private String parseSummary(String content)
    {
        if (content == null)
            return "";

        int position = 0;
        StringBuffer response = new StringBuffer();
        int i = position;

        for (; i < content.length(); i++)
        {
            char c = content.charAt(i);
            if (c == '<')
            {
                response.append("&lt;");
            }
            else if (c == '>')
            {
                response.append("&gt;");
            }
            else if (c == '&')
            {
                response.append("&amp;");
            }
            else if (c == '"')
            {
                response.append("&quot;");
            }
            else if (Character.isLetter(c) && ((i == 0) || !Character.isLetterOrDigit(content.charAt(i - 1))))
            {
                int j = i;

                for (; j < content.length(); j++)
                {
                    if (!Character.isLetter(content.charAt(j)))
                    {
                        break;
                    }
                }

                String word = content.substring(i, j);

                if (Page.isValidPageName(word) && !tagStack.contains(SUPPRESS_LINKS_TAG))
                {
                    if (context.getPageStore().containsName(word))
                    {
                        try
                        {
                            response.append("<a href=\"" + word + "\" title=\"" + cleanSummary(context.getPageStore().retrieve(word).getSummary())
                                    + "\">" + word + "</a>");
                        }
                        catch (BadPageNameException bpne)
                        {
                            throw new RuntimeException("IMPOSSIBLE ERROR: Good page name turned into bad page name.");
                        }
                    }
                    else
                    {
                        response.append(word + "<a href=\"edit?page=" + word + "\">?</a>");
                    }
                    i = j - 1;
                }
                else
                {
                    response.append(c);
                }
            }
            else
            {
                response.append(c);
            }
        }

        return response.toString();
    }

    private void toggleTag(String tagName)
    {
        if (tagStack.contains(tagName))
        {
            closeTag(tagName);
        }
        else
        {
            openTag(tagName);
        }
    }
}