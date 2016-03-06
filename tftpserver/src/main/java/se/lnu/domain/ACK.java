package se.lnu.domain;

/**
 * Created by Jakob on 2016-03-05.
 */
public class ACK
{
    private byte[] opcode = {0, 4};
    private byte[] blockNr;

    public ACK(byte b, byte b1)
    {
        blockNr = new byte[]{b, b1};
        System.out.println("Packet nr == " + b + "," + b1);
    }

    /**
     * Returns what block nr that was received with the ack
     * @return
     */
    public int getAckNr()
    {
        return blockNr[0] * 256 + blockNr[1];
    }
}
