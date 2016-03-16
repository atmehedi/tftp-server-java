package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 */
public class E5InvalidTransferID extends TFTPError
{
    private static byte[] errcode = new byte[]{0, 5};
    public E5InvalidTransferID()
    {
        super(errcode, "Invalid transfer id");
    }
}
