package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 */
public class E2AccessViolation extends TFTPError
{
    private static byte[] errcode = new byte[]{0, 2};
    public E2AccessViolation()
    {
        super(errcode, "Access violation.");
    }
}
