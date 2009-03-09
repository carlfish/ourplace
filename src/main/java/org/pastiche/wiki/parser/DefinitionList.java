package org.pastiche.wiki.parser;

public class DefinitionList extends List
{
    private DefinitionList(int depth)
    {
        super(depth);
    }

    public void accept(ElementVisitor visitor)
    {
        visitor.visitDefinitionList(this);
    }

    public void add(DefinitionListTerm term, DefinitionListDefinition definition)
    {
        getContents().add(term);
        getContents().add(definition);
    }

    public static DefinitionList newInstance(int depth, String initialContents)
    {
        DefinitionList dl = new DefinitionList(depth);
        dl.add(
                DefinitionListTerm.newInstance(
                        initialContents.substring(depth + 1, initialContents.indexOf(')'))),
                DefinitionListDefinition.newInstance(
                        initialContents.substring(initialContents.indexOf(')') + 1)));
        return dl;
    }
}
