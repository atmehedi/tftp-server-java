package se.lnu.handlers;

import se.lnu.domain.error.E1FileNotFound;
import se.lnu.domain.error.E6FileAlreadyExist;
import se.lnu.domain.error.TFTPError;
import se.lnu.domain.exeptions.E1FileNotFoundException;
import se.lnu.domain.exeptions.E6FileAlreadyExistException;

import java.io.File;

/**
 * Created by Jakob on 2016-03-07.
 * A class to do some basic validation of the parsed request objects.
 */
public class RequestValidator
{

    /**
     * Method for validating a read request. Returns an error if there's an
     * error, otherwise null.
     * @param fileName
     * @return
     */
    public TFTPError validateRequestedReadFile(String fileName) throws Exception
    {
        File f = new File(fileName);
        if (!f.exists()){throw new E1FileNotFoundException();}
        return null;
    }


    /**
     * Method for validating the write request.
     * @param fileName
     * @return error if something wrong, otherwise null
     */
    public TFTPError validateRequestedWriteFile(String fileName) throws Exception
    {
        File f = new File(fileName);
        if (f.exists()) { throw new E6FileAlreadyExistException(); }
        return null;
    }
}
