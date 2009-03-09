package org.pastiche.wiki;

import java.io.File;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;

public class WikiControllerServlet extends HttpServlet
{
    private PageStore pageStore = null;
    private java.io.File storeDirectory = null;
    private String storeName = null;
    private java.util.Date lastBackupDate = null;
    private java.text.SimpleDateFormat recentChangeDateFormat = new java.text.SimpleDateFormat("EEEE dd MMMM, yyyy");
    private java.text.SimpleDateFormat recentChangeTimeFormat = new java.text.SimpleDateFormat("HH:mm:ss zz");
    private java.text.SimpleDateFormat msffCookieFormat = new java.text.SimpleDateFormat("ddMM");

    private void checkForDbBackup() throws StorageException
    {
        if (pageStore instanceof SimplePageStore
                && (getInitParameter("backupDir") != null)
                && ((lastBackupDate == null)
                || (new Date().getTime() - lastBackupDate.getTime()) > 1000 * 60 * 60 * 24))
        {
            ((SimplePageStore) pageStore).backup(
                    new java.io.File(getInitParameter("backupDir")));
            lastBackupDate = new Date();
        }
    }

    private String convertSpacesToTabs(String content)
    {
        int start;
        while ((start = content.indexOf("\n    ")) != -1)
        {
            start++;
            int c = 0;

            while ((start + c) < content.length() &&
                    content.charAt(start + c) == ' ')
                c++;

            StringBuffer buf = new StringBuffer(content);
            buf.replace(start, start + c, makeTabsAndSpaces(c / 4, c % 4));
            content = buf.toString();
        }

        return content;
    }

    private String deleteRecentChangeLine(String input, String pageName)
    {
        int start = input.indexOf("\t* " + pageName);
        int end = start + 1;

        if (start == -1)
            return input;

        while (input.charAt(end) != '\n')
            end++;

        StringBuffer buf = new StringBuffer(input);
        buf.delete(start, end + 1);

        return buf.toString();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException
    {
        java.util.StringTokenizer tokens = null;

        try
        {
            checkForDbBackup();
        }
        catch (StorageException se)
        {
            getServletContext().log("WARNING!", se);
        }


        // WORKAROUND: Total fucking reversal of behaviour between Tomcat 3.1 and 3.2
        try
        {
            tokens = new java.util.StringTokenizer(request.getPathInfo(), "/");
        }
        catch (NullPointerException npe)
        {
            tokens = new java.util.StringTokenizer(request.getServletPath(), "/");
        }

        String requestType;

        if (isMicrosoftFreeFriday(request))
        {
            requestType = "MicrosoftFreeFriday";
            setFridayExemptionCookie(response);
        }
        else if (tokens.hasMoreTokens())
        {
            requestType = tokens.nextToken();
        }
        else
        {
            requestType = "FrontPage";
        }

        try
        {
            if (requestType.equals("edit"))
            {
                if (request.getParameter("backupFile") != null)
                {
                    request.setAttribute("wikiPage", getPageFromBackup(request.getParameter("backupFile"), request.getParameter("page")));
                }
                else
                {
                    request.setAttribute("wikiPage", getPage(request.getParameter("page")));
                }
                request.getRequestDispatcher("/editPage.jsp").forward(request, response);
            }
            else if (requestType.equals("search"))
            {
                request.setAttribute("searchResults", pageStore.searchForPages(request.getParameter("search"), new AscendingAlphaSearchResultComparator()));
                request.setAttribute("searchTerm", request.getParameter("search"));
                request.setAttribute("pageCount", new Integer(pageStore.getPageCount()));
                request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
            }
            else if (requestType.equals("view"))
            {
                if (Page.isValidPageName(request.getParameter("page")))
                {
                    redirectToWikiPage(request.getParameter("page"), request, response);
                }
                else
                {
                    request.setAttribute("searchResults", pageStore.searchForPages(request.getParameter("page"), new AscendingAlphaSearchResultComparator()));
                    request.setAttribute("searchTerm", request.getParameter("page"));
                    request.setAttribute("pageCount", new Integer(pageStore.getPageCount()));
                    request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
                }
            }
            else if (requestType.equals("RandomPage"))
            {
                redirectToWikiPage(pageStore.getRandomPageName(), request, response);
            }
            else
            {
                request.setAttribute("wikiPage", getPage(requestType));
                request.getRequestDispatcher("/showPage.jsp").forward(request, response);
            }
        }
        catch (BadPageNameException bpne)
        {
            try
            {
                redirectToWikiPage("BadPageName", request, response);
            }
            catch (BadPageNameException bpne2)
            {
                throw new RuntimeException("IMPOSSIBLE ERROR: BadPageName not recognised as a wiki word.");
            }
        }
    }

    public void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException
    {
        try
        {
            Page page = new Page(request.getParameter("name"));
            page.setVersion(Integer.parseInt(request.getParameter("version")));
            if (request.getParameter("convert") != null &&
                    request.getParameter("convert").equals("tabs"))
            {
                page.setContent(convertSpacesToTabs(request.getParameter("text")));
            }
            else
            {
                page.setContent(request.getParameter("text"));
            }
            page.setSummary(request.getParameter("summary"));
            pageStore.store(page);
            request.setAttribute("pageName", page.getName());

            try
            {
                if (request.getParameter("minorEdit") != null &&
                        request.getParameter("minorEdit").equals("yes"))
                {
                    updateRecentChanges(getPage("RecentEdits"), page);
                }
                else
                {
                    Page recentEdits = getPage("RecentEdits");
                    recentEdits.setContent(deleteRecentChangeLine(recentEdits.getContent(), page.getName()));
                    pageStore.store(recentEdits);
                    updateRecentChanges(getPage("RecentChanges"), page);
                }
            }
            catch (BadPageNameException bpne)
            {
            }

            getServletContext().getRequestDispatcher("/doneEdit.jsp").forward(request, response);
        }
        catch (BadPageNameException bpne)
        {
            request.setAttribute("pageName", request.getParameter("name"));
            getServletContext().getRequestDispatcher("/badPage.jsp").forward(request, response);
        }
        catch (PageAlreadyModifiedException pame)
        {
            request.setAttribute("pageName", request.getParameter("name"));
            getServletContext().getRequestDispatcher("/concurrentModification.jsp").forward(request, response);
        }
        catch (StorageException se)
        {
            se.printStackTrace(System.err);
            getServletContext().getRequestDispatcher("/badKarma.jsp").forward(request, response);
        }
    }

    private String getMicrosoftFreeFridayCookie(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++)
        {
            if (cookies[i].getName().equals("MSFF"))
            {
                return cookies[i].getValue();
            }
        }

        return "";
    }

    private Page getPage(String pageName) throws BadPageNameException
    {
        Page page = pageStore.retrieve(pageName);

        if (page == null)
        {
            page = new Page(pageName);
            page.setStore(pageStore);
        }

        return page;
    }

    private Page getPage(PageStore store, String pageName) throws BadPageNameException
    {
        Page page = store.retrieve(pageName);

        if (page == null)
        {
            page = new Page(pageName);
            page.setStore(pageStore);
        }

        return page;
    }

    private Page getPageFromBackup(String backupFileName, String pageName) throws BadPageNameException, java.io.IOException
    {
        Page page = null;

        if (!java.util.Arrays.asList(new File(getInitParameter("backupDir")).list()).contains(backupFileName))
        {
            page = new Page(pageStore, pageName);
        }
        else
        {
            page = SimplePageStore.getStore(new File(getInitParameter("backupDir")), backupFileName).retrieve(pageName);
        }

        if (page == null)
        {
            page = new Page(pageStore, pageName);
        }

        page.setVersion(getPage(pageName).getVersion());

        return page;
    }

    private String getRequestType(javax.servlet.http.HttpServletRequest request)
    {
        String importantPart = request.getRequestURI().substring(request.getContextPath().length());
        java.util.StringTokenizer tok = new java.util.StringTokenizer(importantPart, "/");
        return tok.nextToken();
    }

    public void init() throws javax.servlet.ServletException
    {
        try
        {
            pageStore = SimplePageStore.getStore(new java.io.File(getInitParameter("storageDir")),
                    getInitParameter("storageFile"));
        }
        catch (java.io.IOException ioe)
        {
            throw new javax.servlet.ServletException("Page Store cound not be initialised: " + ioe.getMessage());
        }
    }

    private boolean isMicrosoftFreeFriday(HttpServletRequest request)
    {
        return (getInitParameter("microsoftFreeFriday") != null)
                && (java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)
                == java.util.Calendar.FRIDAY)
                && (request.getHeader("User-Agent").indexOf("MSIE") != -1)
                && (!getMicrosoftFreeFridayCookie(request)
                .equals(msffCookieFormat.format(new java.util.Date())));
    }

    private String makeTabsAndSpaces(int tabs, int spaces)
    {
        StringBuffer buf = new StringBuffer(tabs + spaces);

        for (int i = 0; i < tabs; i++)
        {
            buf.append('\t');
        }

        for (int i = 0; i < spaces; i++)
        {
            buf.append(' ');
        }

        return buf.toString();
    }

    public void redirectToWikiPage(String pageName, HttpServletRequest request, HttpServletResponse response) throws BadPageNameException, java.io.IOException
    {
        if (!Page.isValidPageName(pageName))
            throw new BadPageNameException(pageName);

        response.sendRedirect("http://" + request.getServerName() +
                ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "") +
                request.getContextPath() + "/" + pageName);
    }

    private void setFridayExemptionCookie(HttpServletResponse response)
    {
        response.addCookie(new Cookie("MSFF", msffCookieFormat.format(new java.util.Date())));
    }

    private void updateRecentChanges(Page recentChangesPage, Page changedPage)
    {

        String pageContent = recentChangesPage.getContent();
        String changeDate = recentChangeDateFormat.format(new java.util.Date());
        String changeTime = recentChangeTimeFormat.format(new java.util.Date());
        pageContent = deleteRecentChangeLine(pageContent, changedPage.getName());

        if (pageContent.indexOf(changeDate) == -1)
            pageContent += "\n" + changeDate + "\n";

        pageContent += "\t* " + changedPage.getName() + " . . . " + changedPage.getSummary() + " . . . " + changeTime + "\n";
        recentChangesPage.setContent(pageContent);
        try
        {
            pageStore.store(recentChangesPage);
        }
        catch (StorageException se)
        {
        }
    }
}
