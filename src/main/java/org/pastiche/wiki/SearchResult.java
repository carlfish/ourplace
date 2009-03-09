package org.pastiche.wiki;

public class SearchResult
{
    private Page page;
    private String context;

    public SearchResult(Page page, String context)
    {
        this.page = page;
        this.context = context;
    }

    public String getContext()
    {
        return context;
    }

    public Page getPage()
    {
        return page;
    }
}
