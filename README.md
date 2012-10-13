Java_Crawler
============

Java Web Crawler

The intention of this Web  Crawler is to collect all the links it can starting from a given URL, 
at the same time has to respect different rules such as :

  * Stay in and crawl only local URL’s
  * Don’t crawl the same URL twice within the same session
  * Been able to Stop, Pause and Resume at any time, and many more.
 
The web crawler will crawl the given website pages of any type such as HTML,XML using the web interface 
along the client side. 

Once the crawler is pressed the start button the crawler crawls to the given site it access all the pages 
of the above type and gets all the visited links and finds out the external and invalid links that are restricted 
for this crawler and finally store the visited links in the history table, including URL, page title, 
date of last modification and date of last time we crawl that page. 

All these tables are created in a MySql Database.

To be able to start the Crawler it is required to submit the URL and credentials of a valid MySQL DB.
A Web interface was also required and we have accomplished this by using HTM and  JQuery.

Requirements:

Mysql Database with the name of the next tables:


links :  url - title - lastpagemodified - lastcrawl

history :  url - title - lastpagemodified - lastcrawl

invalid :  url - title - lastpagemodified - lastcrawl

externallinks :  url - title - lastpagemodified - lastcrawl 