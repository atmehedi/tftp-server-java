package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 */
public class E4IllegalTFTPOperation extends TFTPError
{
    private static byte[] errcode = new byte[]{0, 4};
    public E4IllegalTFTPOperation()
    {
        super(errcode, "The operation code is not allowed according to TFTP");
    }
}
