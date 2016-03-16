package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 */
public class E1FileNotFound extends TFTPError
{
    private static byte[] errcode = new byte[]{0, 1};
    public E1FileNotFound()
    {
        super(errcode, "File not found.");
    }
}
