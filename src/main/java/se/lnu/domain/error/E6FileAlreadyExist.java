package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 */
public class E6FileAlreadyExist extends TFTPError
{
    private static byte[] errcode = new byte[]{0, 6};
    public E6FileAlreadyExist()
    {
        super(errcode, "File already exist");
    }
}