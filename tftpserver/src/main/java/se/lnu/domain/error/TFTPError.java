package se.lnu.domain.error;

/**
 * Created by Jakob on 2016-03-11.
 * Super class for all error sent to client.
 */
public abstract class TFTPError
{
    private static byte[] opcode = new byte[]{0, 5};
    private byte[] errcode;
    private String errMSG;
    private byte endOfString = 0;

    public TFTPError(byte[] errC, String msg)
    {
        errcode = errC;
        errMSG = msg;
    }

    /**
     * This method returns the entire byte array needed to make a proper response
     * to a client.
     * @return a byte array containing everything needed
     */
    public byte[] getError()
    {
        byte[] errMsgInBytes = errMSG.getBytes();
        int lengthOfArr = opcode.length + errMsgInBytes.length + errMSG.length() + 1;   //+ 1 because of end of string
        byte[] out = new byte[lengthOfArr];
        out[0] = opcode[0]; out[1] = opcode[1];
        out[2] = errcode[0]; out[3] = errcode[1];
        for (int i = 4; i < out.length; i++)
        {
            out[i] = errMsgInBytes[i - 4];
        }
        out[out.length - 1] = endOfString;
        return out;
    }
}
