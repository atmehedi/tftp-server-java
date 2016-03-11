package se.lnu.domain;

import se.lnu.handlers.UnsignedHelper;

/**
 * Created by Jakob on 2016-03-05.
 */
public class ACK
{
    private byte[] opcode = {0, 4};
    private byte[] blockNr;
    private int blockNrInt;


    public ACK(byte b, byte b1)
    {
        blockNr = new byte[]{b, b1};
        System.out.println("Packet nr == " + b + "," + b1);
        blockNrInt = UnsignedHelper.twoBytesToInt(blockNr);
    }

    /**
     * Returns what block nr that was received with the ack
     * @return
     */
    public int getAckNr()
    {
        return blockNrInt;
    }
}
