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
        byte[] errMsgInBytes = this.errMSG.getBytes();
        int lengthOfArr = 4 + errMsgInBytes.length + 1;  //+4 because of 4 first
                                                         //+1 because of end of string
        byte[] out = new byte[lengthOfArr];
        out[0] = opcode[0]; out[1] = opcode[1];
        out[2] = errcode[0]; out[3] = errcode[1];

        for (int i = 0; i < errMsgInBytes.length; i++)
        {
            // Add the message on i + 4 in the out array.
            out[i + 4] = errMsgInBytes[i];
        }

        return out;
    }

    public String getErrMSG() {
        return this.errMSG;
    }
}
