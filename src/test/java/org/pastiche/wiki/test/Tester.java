package org.pastiche.wiki.test;

import java.io.*;

import org.pastiche.wiki.*;
import org.pastiche.wiki.parser.WikiParser;

/**
 * Insert the type's description here.
 * Creation date: (3/02/2001 1:31:16 PM)
 *
 * @author: Charles Miller
 */
public class Tester
{
    /**
     * Tester constructor comment.
     */
    public Tester()
    {
        super();
    }

    /**
     * Insert the method's description here.
     * Creation date: (3/02/2001 1:31:26 PM)
     *
     * @param args java.lang.String[]
     */
    public static void main(String[] args)
    {
        try
        {
            File foo = new File("bugger.txt");
            StringBuffer stuff = new StringBuffer(8024000);
            BufferedReader reader = new BufferedReader(new FileReader(foo));
            String line;
            while ((line = reader.readLine()) != null)
            {
                stuff.append(line + "\r\n");
            }

            System.out.println(new WikiParser(stuff.toString(), new TestPageStore()).parse());

            //long start = new java.util.Date().getTime();
            //for (int i = 0; i < 100000; i++) {
            //String s = new WikiParser(stuff.toString(), new TestPageStore()).parse();
            //}
            //long seconds = (new java.util.Date().getTime() - start) / 1000;
            //System.out.println("100,000 iterations = " + seconds + " seconds, or " + (100000.0 / (float)seconds) + " iterations per second.");
        }
        catch (java.io.IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
