package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 * This is the only error with not specific message attached to it. Instead
 * it's the error message with error code 0 which isn't defined and returns
 * an arbitrary error message.
 */
public class E0NotDefined extends TFTPError
{
    private static byte[] errcode = new byte[]{0, 0};

    /**
     * Main constructor for the undefined error
     * @param errorMSG the message to be sent to the client
     */
    public E0NotDefined(String errorMSG)
    {
        super(errcode, errorMSG);
    }
}
