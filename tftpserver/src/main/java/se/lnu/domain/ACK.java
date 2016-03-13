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
        //System.out.println("Packet nr == " + b + "," + b1);
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

    public String toString()
    {
        return "ACK, nr: " + blockNrInt;
    }

    public byte[] returnAckAsBytes(){

        byte[] out = new byte[4];
        out[0] = opcode[0];
        out[1] = opcode[1];
        out[2] = blockNr[0];
        out[3] = blockNr[1];
        return out;
    }
}
