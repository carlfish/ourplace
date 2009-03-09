package org.pastiche.wiki;

import java.util.Date;
import java.io.File;
import java.util.SortedSet;
import java.text.SimpleDateFormat;

/**
 * This page store will not scale to a huge number of pages, since
 * everything is being held in memory at once, and the store() operation
 * is O(N) against the number of entries in the store.
 * <p/>
 * That said, if you've got lots of memory, or a small site, it's more
 * than enough, especially since you get complete page-retrieval for free.
 * <p/>
 * Note: I should be returning CLONES of the pages from the store, rather
 * than references to the pages themselves, so that pages modified while
 * outside the store don't affect the store itself. I don't do this, and
 * for it I should be spanked. So be careful modifying stuff while you're
 * using it. :)
 */
public class SimplePageStore implements PageStore, java.io.Serializable
{
    private final static long serialVersionUID = -8284628139744428581L;

    private static final SimpleDateFormat backupDateFormat =
            new SimpleDateFormat("yyyyMMdd");
    private java.util.Map storage = new java.util.HashMap();
    private transient File storeFile = null;

    protected SimplePageStore(java.io.File storeFile)
    {
        this.storeFile = storeFile;
    }

    public void backup(File backupDirectory) throws StorageException
    {
        writeDb(
                new File(
                        backupDirectory,
                        storeFile.getName() + "-" + backupDateFormat.format(new Date())));
    }

    private void checkPageNotAlreadyModified(Page page) throws PageAlreadyModifiedException
    {
        Page existingPage = null;
        try
        {
            existingPage = retrieve(page.getName());
        }
        catch (BadPageNameException bpne)
        {
            throw new RuntimeException("IMPOSSIBLE EVENT: A Page was constructed with a bad Page Name.");
        }

        if (existingPage != null)
        {
            if (existingPage == page)
                return;

            if (page.getVersion() <= existingPage.getVersion())
                throw new PageAlreadyModifiedException();
        }

    }

    public boolean containsName(String pageName)
    {
        return storage.containsKey(pageName);
    }

    public java.util.SortedSet findPagesContaining(java.lang.String searchTerm, java.util.Comparator comparator)
    {
        SortedSet results = new java.util.TreeSet(comparator);

        java.util.Iterator i = storage.values().iterator();

        while (i.hasNext())
        {
            Page page = (Page) i.next();

            if ((matches(page.getName(), searchTerm) ||
                    matches(page.getSummary(), searchTerm) ||
                    matches(page.getContent(), searchTerm)))
                results.add(page);
        }
        return results;
    }

    public synchronized java.util.SortedSet findPagesNotContaining(java.lang.String searchTerm, java.util.Comparator comparator)
    {
        SortedSet results = new java.util.TreeSet(comparator);

        java.util.Iterator i = storage.values().iterator();

        while (i.hasNext())
        {
            Page page = (Page) i.next();

            if (!(matches(page.getName(), searchTerm) ||
                    matches(page.getSummary(), searchTerm) ||
                    matches(page.getContent(), searchTerm)))
                results.add(page);
        }
        return results;
    }

    public java.util.SortedSet getAllPages(java.util.Comparator comparator)
    {
        SortedSet allPages = new java.util.TreeSet(comparator);
        allPages.addAll(storage.values());
        return allPages;
    }

    public synchronized int getPageCount()
    {
        return storage.size();
    }

    public String getRandomPageName()
    {
        java.util.List list = new java.util.ArrayList(storage.keySet());
        return (String) list.get(new java.util.Random().nextInt(list.size()));
    }

    private String getResultContext(String s, String searchTerm)
    {
        int start = s.toLowerCase().indexOf(searchTerm.toLowerCase());
        int end = start + searchTerm.length();


        while (start > 0 && Character.isLetterOrDigit(s.charAt(start - 1)))
            start--;

        while (end < s.length() && Character.isLetterOrDigit(s.charAt(end)))
            end++;

        return s.substring(start, end);
    }

    public static SimplePageStore getStore(java.io.File storeDirectory, String storeName) throws java.io.IOException
    {
        if (!storeDirectory.exists())
            throw new java.io.FileNotFoundException("Storage directory does not exist: " + storeDirectory.getAbsolutePath());

        if (!storeDirectory.isDirectory())
            throw new java.io.FileNotFoundException("Specified directory is not a directory: " + storeDirectory.getAbsolutePath());

        java.io.File storeFile = new java.io.File(storeDirectory, storeName);

        if (storeFile.exists())
        {
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(storeFile));
            try
            {
                SimplePageStore store = (SimplePageStore) in.readObject();
                store.setStoreFile(storeFile);
                return store;
            }
            catch (ClassNotFoundException cnfe)
            {
                throw new java.io.IOException("Problem de-serializing old object store:" + cnfe.getMessage());
            }
        }

        return new SimplePageStore(storeFile);
    }

    private boolean matches(String s, String searchTerm)
    {
        if (s == null)
            return false;

        return s.toLowerCase().indexOf(searchTerm.toLowerCase()) != -1;
    }

    public synchronized Page retrieve(String pageName) throws BadPageNameException
    {
        if (!Page.isValidPageName(pageName))
            throw new BadPageNameException(pageName);

        Page page = (Page) storage.get(pageName);

        if (page != null)
            page.setStore(this);

        return page;
    }

    public synchronized java.util.SortedSet searchForPages(java.lang.String searchTerm, java.util.Comparator comparator)
    {
        SortedSet results = new java.util.TreeSet(comparator);

        java.util.Iterator i = storage.values().iterator();

        while (i.hasNext())
        {
            Page page = (Page) i.next();

            if (matches(page.getName(), searchTerm))
            {
                results.add(new SearchResult(page, page.getName()));
            }
            else if (matches(page.getSummary(), searchTerm))
            {
                results.add(new SearchResult(page, page.getSummary()));
            }
            else if (matches(page.getContent(), searchTerm))
            {
                results.add(new SearchResult(page, getResultContext(page.getContent(), searchTerm)));
            }
        }
        return results;
    }

    private void setStoreFile(java.io.File storeFile)
    {
        this.storeFile = storeFile;
    }

    public synchronized void store(Page page) throws PageAlreadyModifiedException, StorageException
    {
        checkPageNotAlreadyModified(page);
        page.setStore(this);

        if (page.getVersion() == Integer.MAX_VALUE)
            page.setVersion(1);

        storage.put(page.getName(), page);

        writeDb(storeFile);
    }

    private void writeDb(File file) throws StorageException
    {
        try
        {
            java.io.ObjectOutputStream out =
                    new java.io.ObjectOutputStream(new java.io.FileOutputStream(file));
            out.writeObject(this);
        }
        catch (java.io.IOException ioe)
        {
            throw new StorageException("Could not write out changes: " + ioe.getMessage());
        }
    }
}
