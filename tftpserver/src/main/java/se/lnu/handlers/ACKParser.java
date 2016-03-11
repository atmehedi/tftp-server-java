package se.lnu.handlers;

import se.lnu.domain.ACK;

import java.net.DatagramPacket;

/**
 * Created by Jakob on 2016-03-06.
 * Simple parser for an ACK.
 */
public class ACKParser
{

    public ACK getAck(DatagramPacket recvDatagramPacket) throws IllegalStateException
    {
        byte[] data = recvDatagramPacket.getData();
        System.out.println("\n" + data[0] + ", " + data[1] + ", " + data[2] + "," + data[3] + "\n");
        if (data[0] != 0 && data[1] != 4)
        {
            //TODO handle with proper error response
            throw new IllegalStateException("Not ack opcode, received code = " + data[0] + " " + data[1]);
        }
        return new ACK(data[2], data[3]);
    }
}
