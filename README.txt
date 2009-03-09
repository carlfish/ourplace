=== OurPlace ===

OurPlace is a wiki server I wrote in 2001 to run a site for me and my friends. Its functionality is based largely
on Ward Cunningham's WikiWikiWeb, or at least the WikiBase code he published back then.

It was written in Java 1.2 (later 1.3?) against the Servlet 2.1 (later 2.2) spec.

The code is very basic. All the controller logic happens in a single if/then/else statement in the main servlet,
it keeps the entire site in memory, the persistence mechanism is plain old Java serialization, and I hesitate
to guess just how many concurrency bugs are lurking in it. Still, I wrote ot in two Sundays (one for the base wiki,
another to rewrite the wiki parser so I could more easily add new markup) and it managed to keep the handful of
contributors to my website happy for a number of years.

This code has little more than sentimental value for its author, I'm just putting it online because I keep almost
losing it on my own machines.

=== License ===

Copyright (c) 2001-2009 Charles Miller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.