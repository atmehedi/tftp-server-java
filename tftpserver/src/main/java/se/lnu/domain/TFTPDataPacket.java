package se.lnu.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Jakob on 2016-03-05.
 */
public class TFTPDataPacket
{
    private byte[] opcode = {0, 3};
    private byte[][] data;

    public TFTPDataPacket(Path toFile) throws IOException
    {
        data = setupDataArr(toFile);
    }

    public byte[] getPacket(int packetNo)
    {
        if (packetNo <= 0)
        {
            throw new IllegalArgumentException("Packet number can't be 0 or negative");
        }
        else if (packetNo > data.length)
        {
            throw new IllegalArgumentException("The last packet is the packet with number " + data.length);
        }
        byte[] currentPacket = data[packetNo-1];
        byte[] packet = new byte[currentPacket.length + 4];
        packet[0] = 0; packet[1] = 3;   //Indicating it's data packet for the transfer
        packet[2] = 0; packet[3] = (byte) packetNo;
        for (int i = 4; i < packet.length; i++)
        {
            packet[i] = currentPacket[i-4];
        }
        //System.out.println("Optcode = " + packet[0] +"" + packet[1]);
        //System.out.println("Returning packet no " + packetNo);
        return packet;
    }

    /**
     * Help method that sets up all byte arrays for the data to transfer
     * @param toFile the file to be served
     * @return a two dimensonial array where the last packet won't be full
     * according to the tftp protocol.
     * @throws IOException
     */
    private byte[][] setupDataArr(Path toFile) throws IOException
    {
        byte[] dataInBytes = Files.readAllBytes(toFile);
        final int ARR_SIZE = 512;
        int noArrs = dataInBytes.length / ARR_SIZE + 1;
        //System.out.println("Arrsize = " + noArrs);
        byte[][] dataArr = new byte[noArrs][];
        for (int i = 0; i < dataArr.length - 1; i++)
        {
            dataArr[i] = new byte[ARR_SIZE];
            for (int k = 0; k < ARR_SIZE; k++)
            {
                //System.out.println("dataInBytes[" + i + "]" + "[" + k + "] == dataInBytes[" + ((ARR_SIZE * i) + k) + "]");
                dataArr[i][k] = dataInBytes[(ARR_SIZE * i) + k];
            }
        }
        /*Oavsett vilket så måste ett sista paket skapas för även om det
        * är ett jämnt antal ska ett sista paket som innehåller 0-511 paket skickas*/
        int lastArray = dataArr.length - 1;
        //System.out.println("Last array index == " + lastArray);
        int startIOfLastBytes = ARR_SIZE * lastArray;
        //System.out.println("Index of start byte to read from == " + startIOfLastBytes);
        int bytesLeft = dataInBytes.length - startIOfLastBytes;
        //System.out.println("Bytes left == " + bytesLeft);
        dataArr[dataArr.length - 1] = new byte[bytesLeft];
        for (int i = startIOfLastBytes; i < dataInBytes.length; i++)
        {
            dataArr[lastArray][i % ARR_SIZE] = dataInBytes[i];
        }
        return dataArr;
    }

    public int getLastPacketNr()
    {
        return data.length;
    }

}
