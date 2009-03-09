package org.pastiche.wiki.parser;

public abstract class Element
{
    public abstract void accept(ElementVisitor visitor);

    public abstract void appendTo(java.util.LinkedList elementList);
}
