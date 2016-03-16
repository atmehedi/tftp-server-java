package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 */
public class E3DiskFull extends TFTPError
{
    private static byte[] errcode = new byte[]{0, 3};
    public E3DiskFull()
    {
        super(errcode, "Disk is full or the allocation is exceeded");
    }
}
