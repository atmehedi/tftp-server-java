package se.lnu.domain;


import se.lnu.handlers.UnsignedHelper;

/**
 * Created by Jakob on 2016-03-05.
 */
public class TFTPDataPacket
{
    private int blockNr;
    private byte[] packet;

    public TFTPDataPacket(byte[] data, int blockNrIn)
    {
        if (data.length > 512)
        {
            throw new IllegalArgumentException("Maximum length of data packet is 512 bytes, the number" +
                    " bytes received in constructor == " + data.length);
        }
        if (blockNrIn < 1 || blockNrIn > 65535)
        {
            throw new IllegalArgumentException("Invalid block number, has to be 1-65535");
        }
        blockNr = blockNrIn;
        packet = new byte[data.length + 4];
        packet[0] = 0; packet[1] = 3;   //opcode
        byte[] blockNrToUnsBytes = UnsignedHelper.intTo2UnsignedBytes(blockNrIn);
        packet[2] = blockNrToUnsBytes[0];
        packet[3] = blockNrToUnsBytes[1];
        for (int i = 4; i < packet.length; i++)
        {
            packet[i] = data[i-4];
        }
    }

    public String toString()
    {
        String out = "";
        out += "Opcode == " + packet[0] + "" + packet[1];
        out += ", block nr == " + blockNr;
        out += ", getContent() == " + getContent();
        return  out;
    }

    /**
     * This method returns the content of the data converted to a string,
     * hence it is not useful for raw data in other forms than text.
     * @return a String directly converted from the data bytes
     */
    public String getContent()
    {
        StringBuilder content = new StringBuilder();
        for (int i = 4; i < packet.length; i++)
        {
            content.append((char)packet[i]);
        }
        return content.toString();
    }

    /**
     * Getter for the entire packet in bytes, including opcode and
     * block nr in unsigned bytes.
     * @return
     */
    public byte[] getPacket() {
        return packet;
    }

    /**
     * Returns the signed int value of the block nr.
     * @return
     */
    public int getBlockNr() {
        return blockNr;
    }

    /**
     * The data byte array contains the entire data packet, including the opcode
     * and block number. This method extracts only the actual data.
     * @return a byte array with only the data of the packet
     */
    public byte[] getDataOnly()
    {
        byte[] out = new byte[packet.length -4];
        for (int i = 4; i < packet.length; i++)
        {
            out[i-4] = packet[i];
        }
        return out;
    }
}
