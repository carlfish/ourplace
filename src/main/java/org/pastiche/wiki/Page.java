package org.pastiche.wiki;

public class Page implements java.io.Serializable
{
    private final static long serialVersionUID = -76547734498611647L;

    private String name = null;
    private String summary = "";
    private String content = null;
    private java.util.Date lastModified = new java.util.Date();
    private int version = 0;

    private transient PageStore store = null;

    public Page(String name) throws BadPageNameException
    {
        if (!isValidPageName(name))
            throw new BadPageNameException(name);

        this.name = name;
        this.content = "Describe " + name + " here.";
    }

    public Page(PageStore store, String name) throws BadPageNameException
    {
        if (!isValidPageName(name))
            throw new BadPageNameException(name);

        this.store = store;
        this.name = name;
        this.content = "Describe " + name + " here.";
    }

    public java.lang.String getContent()
    {
        return content;
    }

    public java.lang.String getContentAsHtml()
    {
        return new org.pastiche.wiki.parser.WikiParser(getContent(), getStore()).parse();
    }

    /**
     * Insert the method's description here.
     * Creation date: (20/05/2001 6:08:37 PM)
     *
     * @return java.util.Date
     */
    public java.util.Date getLastModified()
    {
        return lastModified;
    }

    public java.lang.String getName()
    {
        return name;
    }

    public java.lang.String getNameWithSpaces()
    {
        StringBuffer buf = new StringBuffer(getName().length() + 5);

        buf.append(getName().charAt(0));

        for (int i = 1; i < getName().length(); i++)
        {
            if (Character.isUpperCase(getName().charAt(i)))
            {
                buf.append(' ');
            }

            buf.append(getName().charAt(i));
        }


        return buf.toString();
    }

    public PageStore getStore()
    {
        return store;
    }

    public java.lang.String getSummary()
    {
        return summary;
    }

    public java.lang.String getSummaryWithEscapedQuotes()
    {
        int i;
        String summary = getSummary();

        if (summary == null)
        {
            return "";
        }

        while ((i = summary.indexOf('"')) != -1)
        {
            StringBuffer buf = new StringBuffer(summary);
            buf.replace(i, i + 1, "&quot;");
            summary = buf.toString();
        }

        return summary;
    }

    public int getVersion()
    {
        return version;
    }

    public static boolean isValidPageName(String name)
    {
        if (name == null || name.length() <= 3)
            return false;

        // By negating the characters we want, we conveniently take
        // care of situations where the characters are non-alpha, too.
        if (!Character.isUpperCase(name.charAt(0)) ||
                !Character.isLowerCase(name.charAt(1)) ||
                !Character.isLowerCase(name.charAt(name.length() - 1)))
        {

            return false;
        }


        boolean containsUpper = false;
        for (int i = 2; i < name.length(); i++)
        {
            if (!Character.isLetter(name.charAt(i)))
            {
                return false;
            }

            if (Character.isUpperCase(name.charAt(i)))
            {
                containsUpper = true;
            }
        }

        return containsUpper;
    }

    public void setContent(java.lang.String newContent)
    {
        content = newContent;
        version++;
        lastModified = new java.util.Date();
    }

    /**
     * Insert the method's description here.
     * Creation date: (20/05/2001 6:08:37 PM)
     *
     * @param newLastModified java.util.Date
     */
    public void setLastModified(java.util.Date newLastModified)
    {
        lastModified = newLastModified;
    }

    /**
     * Insert the method's description here.
     * Creation date: (17/03/2001 3:42:33 PM)
     *
     * @param newStore org.pastiche.wiki.PageStore
     */
    public void setStore(PageStore newStore)
    {
        store = newStore;
    }

    public void setSummary(java.lang.String newSummary)
    {
        summary = newSummary;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }
}
