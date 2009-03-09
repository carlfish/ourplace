package org.pastiche.wiki.parser;

public abstract class ElementVisitor
{
    public abstract void visitBasicListItem(BasicListItem element);

    public abstract void visitBlankLine(BlankLine element);

    public abstract void visitBlockQuote(BlockQuote element);

    public abstract void visitCentered(Centered element);

    public abstract void visitDefinitionList(DefinitionList element);

    public abstract void visitDefinitionListDefinition(DefinitionListDefinition element);

    public abstract void visitDefinitionListTerm(DefinitionListTerm element);

    public abstract void visitDocument(Document element);

    public abstract void visitHorizontalRule(HorizontalRule element);

    public abstract void visitIndex(Index element);

    public abstract void visitOrderedList(OrderedList element);

    public abstract void visitParagraph(Paragraph element);

    public abstract void visitPreformatted(Preformatted element);

    public abstract void visitSearchBox(SearchBox element);

    public abstract void visitText(Text element);

    public abstract void visitUnorderedList(UnorderedList element);
}
