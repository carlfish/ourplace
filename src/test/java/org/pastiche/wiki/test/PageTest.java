package org.pastiche.wiki.test;

import org.pastiche.wiki.Page;
import junit.framework.TestCase;
import junit.swingui.TestRunner;

public class PageTest extends TestCase
{
    public PageTest(String arg1)
    {
        super(arg1);
    }

    public static void main(String[] args)
    {
        TestRunner.run(PageTest.class);
    }

    public static junit.framework.TestSuite suite()
    {
        return new junit.framework.TestSuite(PageTest.class);
    }

    public void testWikiNameValidity()
    {
        assertTrue("Valid FrontPage", Page.isValidPageName("FrontPage"));
        assertTrue("Invalid Frontpage", !Page.isValidPageName("Frontpage"));
        assertTrue("Invalid frontPage", !Page.isValidPageName("frontPage"));
        assertTrue("Valid ThisIsLonger", Page.isValidPageName("ThisIsLonger"));
        assertTrue("Valid ThisIsAPage", Page.isValidPageName("ThisIsAPage"));
        assertTrue("Invalid Contains`NonAlpha", !Page.isValidPageName("Contains`NonAlpha"));
        assertTrue("Invalid Contains1aNumber", !Page.isValidPageName("Contains1aNumber"));
        assertTrue("Invalid empty", !Page.isValidPageName(""));
        assertTrue("Invalid oneChar", !Page.isValidPageName("A"));
        assertTrue("Invalid twoChar", !Page.isValidPageName("AA"));
        assertTrue("Invalid twoCharLower", !Page.isValidPageName("Aa"));
        assertTrue("Invalid EndsInA", !Page.isValidPageName("EndsInA"));
    }
}
