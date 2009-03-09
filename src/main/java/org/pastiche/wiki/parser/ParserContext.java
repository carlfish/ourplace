package org.pastiche.wiki.parser;

public class ParserContext
{
    private org.pastiche.wiki.PageStore pageStore = null;
    private boolean preformatted = false;

    /**
     * ParserContext constructor comment.
     */
    public ParserContext()
    {
        super();
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2001 7:14:44 PM)
     *
     * @return org.pastiche.wiki.PageStore
     */
    public org.pastiche.wiki.PageStore getPageStore()
    {
        return pageStore;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2001 7:08:38 PM)
     *
     * @return boolean
     */
    public boolean isPreformatted()
    {
        return preformatted;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2001 7:14:44 PM)
     *
     * @param newPageStore org.pastiche.wiki.PageStore
     */
    public void setPageStore(org.pastiche.wiki.PageStore newPageStore)
    {
        pageStore = newPageStore;
    }

    /**
     * Insert the method's description here.
     * Creation date: (29/07/2001 7:08:38 PM)
     *
     * @param newPreformatted boolean
     */
    public void setPreformatted(boolean newPreformatted)
    {
        preformatted = newPreformatted;
    }
}
