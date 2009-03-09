package org.pastiche.wiki.parser;

import org.pastiche.wiki.Page;

public class BasicHtmlConverter extends ElementVisitor
{
    private StringBuffer output = new StringBuffer();
    private int listDepth = 0;
    private ParserContext context = new ParserContext();

    public BasicHtmlConverter(org.pastiche.wiki.PageStore store)
    {
        this.context.setPageStore(store);
    }

    public String getContents()
    {
        return output.toString();
    }

    private void indentToListDepth()
    {
        for (int i = 0; i < listDepth; i++)
        {
            output.append('\t');
        }
    }

    public void visitBasicListItem(BasicListItem element)
    {
        indentToListDepth();
        output.append("<li>");
        element.visitContents(this);
        if (output.charAt(output.length() - 1) == '\n')
            indentToListDepth();
        output.append("</li>\n");
    }

    public void visitBlankLine(BlankLine element)
    {
        output.append('\n');
    }

    public void visitBlockQuote(BlockQuote element)
    {
        output.append("<blockquote>");
        element.visitContents(this);
        output.append("</blockquote>\n");
    }

    public void visitCentered(Centered element)
    {
        output.append("<div align=\"center\">");
        element.visitContents(this);
        output.append("</div>\n");
    }

    public void visitDefinitionList(DefinitionList element)
    {
        indentToListDepth();
        output.append("<dl>\n");
        ++listDepth;
        element.visitContents(this);
        --listDepth;
        indentToListDepth();
        output.append("</dl>\n");
    }

    public void visitDefinitionListDefinition(DefinitionListDefinition element)
    {
        indentToListDepth();
        output.append("<dd>");
        element.visitContents(this);
        if (output.charAt(output.length() - 1) == '\n')
            indentToListDepth();
        output.append("</dd>\n");
    }

    public void visitDefinitionListTerm(DefinitionListTerm element)
    {
        indentToListDepth();
        output.append("<dt>");
        element.visitContents(this);
        if (output.charAt(output.length() - 1) == '\n')
            indentToListDepth();
        output.append("</dt>\n");
    }

    public void visitDocument(Document element)
    {
        element.visitContents(this);
    }

    public void visitHorizontalRule(HorizontalRule element)
    {
        output.append("<hr />\n");
    }

    public void visitIndex(Index element)
    {
        java.util.SortedSet indexedPages = element.getIndexedPages(context.getPageStore());
        output.append("<p style=\"font-size: smaller\">(Index: " + indexedPages.size() + " matches from " + context.getPageStore().getPageCount() + " pages searched)</p>\n");

        output.append("<ol>\n");
        char firstLetter = 'A';

        java.util.Iterator i = indexedPages.iterator();

        while (i.hasNext())
        {
            Page page = (Page) i.next();

            if (page.getName().charAt(0) != firstLetter)
            {
                firstLetter = page.getName().charAt(0);
                output.append("<li style=\"margin-top: 1ex\">");
            }
            else
            {
                output.append("<li>");
            }

            output.append("<a href=\"" + page.getName() + "\" title=\"" + page.getSummaryWithEscapedQuotes()
                    + "\">" + page.getName() + "</a>" + " . . . . . . " + (new TextElementParser(page.getSummary(), context).parse()) + "</li>\n");
        }

        output.append("</ol>\n");
    }

    public void visitOrderedList(OrderedList element)
    {
        if (output.charAt(output.length() - 1) != '\n')
            output.append('\n');

        indentToListDepth();
        output.append("<ol>\n");
        ++listDepth;
        element.visitContents(this);
        --listDepth;
        indentToListDepth();
        output.append("</ol>\n");
    }

    public void visitParagraph(Paragraph element)
    {
        output.append("<p>");
        element.visitContents(this);
        output.append("</p>\n");
    }

    public void visitPreformatted(Preformatted element)
    {
        output.append("<pre>");
        context.setPreformatted(true);
        element.visitContents(this);
        context.setPreformatted(false);
        output.append("</pre>\n");
    }

    public void visitSearchBox(SearchBox element)
    {
        output.append("<form method=\"get\" action=\"search\"><input type=\"text\" size=\"40\" name=\"search\" /></form>");
    }

    public void visitText(Text element)
    {
        output.append(new TextElementParser(element.getText(), context).parse());
    }

    public void visitUnorderedList(UnorderedList element)
    {
        indentToListDepth();
        output.append("<ul>\n");
        ++listDepth;
        element.visitContents(this);
        --listDepth;
        indentToListDepth();
        output.append("</ul>\n");
    }
}
