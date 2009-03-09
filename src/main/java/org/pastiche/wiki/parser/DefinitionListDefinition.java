package org.pastiche.wiki.parser;

public class DefinitionListDefinition extends ListItem
{
    public void accept(ElementVisitor visitor)
    {
        visitor.visitDefinitionListDefinition(this);
    }

    public static DefinitionListDefinition newInstance(String initialContent)
    {
        DefinitionListDefinition dd = new DefinitionListDefinition();
        dd.add(Text.newInstance(initialContent.trim()));
        return dd;
    }
}
