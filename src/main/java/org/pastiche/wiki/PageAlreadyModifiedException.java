package org.pastiche.wiki;

public class PageAlreadyModifiedException extends StorageException
{
    public PageAlreadyModifiedException()
    {
        super();
    }

    public PageAlreadyModifiedException(String s)
    {
        super(s);
    }
}
