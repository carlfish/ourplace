package org.pastiche.wiki;

public interface PageStore
{
    boolean containsName(String pageName);

    public java.util.SortedSet findPagesContaining(String searchTerm, java.util.Comparator comparator);

    public java.util.SortedSet findPagesNotContaining(String searchTerm, java.util.Comparator comparator);

    public java.util.SortedSet getAllPages(java.util.Comparator comparator);

    public int getPageCount();

    public String getRandomPageName();

    Page retrieve(String pageName) throws BadPageNameException;

    public java.util.SortedSet searchForPages(String searchTerm, java.util.Comparator comparator);

    void store(Page page) throws PageAlreadyModifiedException, StorageException;
}
