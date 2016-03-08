package se.lnu.handlers;

import java.io.File;

/**
 * Created by Jakob on 2016-03-07.
 */
public class RequestValidator
{

    public boolean validateRequestedReadFile(String fileName)
    {
        File f = new File(fileName);
        return f.exists();
    }

    /* These methods can later be changed to return error messages. If they return null,
    * there's not conflict and the main method will know it.*/
    public boolean validateRequestedWriteFile(String fileName)
    {

        File f = new File(fileName);
        return !f.exists();     //Not possible to write over existing file
    }
}
