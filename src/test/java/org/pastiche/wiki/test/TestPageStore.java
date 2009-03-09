package org.pastiche.wiki.test;

/**
 * A PageStore that does not persist, so we can use it in tests.
 */
public class TestPageStore implements org.pastiche.wiki.PageStore
{
    private java.util.Map storage = new java.util.HashMap();

    public TestPageStore()
    {
    }

    public boolean containsName(String pageName)
    {
        return storage.containsKey(pageName);
    }

    /**
     * findPagesContaining method comment.
     */
    public java.util.SortedSet findPagesContaining(java.lang.String searchTerm, java.util.Comparator comparator)
    {
        return null;
    }

    /**
     * findPagesNotContaining method comment.
     */
    public java.util.SortedSet findPagesNotContaining(java.lang.String searchTerm, java.util.Comparator comparator)
    {
        return null;
    }

    /**
     * getAllPages method comment.
     */
    public java.util.SortedSet getAllPages(java.util.Comparator comparator)
    {
        return null;
    }

    public synchronized int getPageCount()
    {
        return storage.size();
    }

    /**
     * getRandomPageName method comment.
     */
    public java.lang.String getRandomPageName()
    {
        return null;
    }

    public synchronized org.pastiche.wiki.Page retrieve(String pageName)
    {
        return (org.pastiche.wiki.Page) storage.get(pageName);
    }

    /**
     * searchForPages method comment.
     */
    public java.util.SortedSet searchForPages(java.lang.String searchTerm, java.util.Comparator comparator)
    {
        return null;
    }

    public synchronized void store(org.pastiche.wiki.Page page) throws org.pastiche.wiki.StorageException
    {
        if ((retrieve(page.getName()) != null) &&
                (page.getVersion() <= retrieve(page.getName()).getVersion()))

            throw new org.pastiche.wiki.PageAlreadyModifiedException();

        storage.put(page.getName(), page);
    }
}
