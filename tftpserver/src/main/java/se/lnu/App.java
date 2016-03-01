package se.lnu;

import org.apache.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        final Logger LOG = Logger.getLogger(App.class.getSimpleName());
        LOG.debug("Init server");
        LOG.error("Server error");
        System.out.println( "Hello World!" );

    }
}
