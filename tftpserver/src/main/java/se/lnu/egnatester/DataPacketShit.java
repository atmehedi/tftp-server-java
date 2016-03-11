package se.lnu.egnatester;

import se.lnu.domain.ACK;
import se.lnu.domain.TFTPDataPacket;
import se.lnu.handlers.DataPacketHandler;

import java.util.ArrayList;

/**
 * Created by Jakob on 2016-03-11.
 * Demonstration class for JOHN to see how the logic of
 * the datapackethandler class works if it is confusing.
 */
public class DataPacketShit
{
    static ArrayList<TFTPDataPacket> packets;
    public final static int ARR_SIZE = 1;

    public static void main(String[] args)
    {
        /* The following text represents a file */
        String text = "Motherfucking shiieeet";
        DataPacketHandler dh = new DataPacketHandler();
        /* Divide content of the file to packets in the correct way.
        * If you look on the variable ARR_SIZE it's possible to change it
        * to change the size of the packets, the low value is simply because
        * it's easier to test.*/
        packets = dh.logicOfSeparatingPackets(text.getBytes());
        for (TFTPDataPacket tftpDataPacket: packets)
        {
            System.out.println(tftpDataPacket);
        }

        /* This method is needed before receiving data packets from the client,
        * this simply sets up the array list for storing the incoming data packets.*/
        dh.prepareToReceive();

        /* Method for showing how the parseAndReceiveTFTPDataPacket method works.
        * As you can see it receives a bunch of packets and returns a corresponding
        * ACK object. The method will reject packets that doesn't come in the right
        * order so you can try to set i to 1 and see what happens.*/
        for (int i = 0; i < packets.size(); i++)
        {
            ACK ack = dh.parseAndReceiveTFTPDataPacket(packets.get(i).getPacket());
            System.out.println(ack);
        }

        /* Retrieve the raw data from all packets and merge them into one big byte array.*/
        byte[] data = getRawDataFromPackets();
        /* The result of converting the packets back and printing them out.*/
        System.out.println(new String(data));

    }

    private static byte[] getRawDataFromPackets()
    {
        ArrayList<byte[]> dataOnlyArrs = new ArrayList<byte[]>();
        int arrLength = 0;
        for (TFTPDataPacket packet: packets)
        {
            dataOnlyArrs.add(packet.getDataOnly());
            arrLength += packet.getDataOnly().length;
            System.out.println(arrLength);
        }
        byte[] out = new byte[arrLength];
        for (int i = 0; i < dataOnlyArrs.size(); i++)
        {
            byte[] temp = dataOnlyArrs.get(i);
            System.out.println(new String(temp) + "   " + temp.length);
            for (int k = 0; k < temp.length; k++)
            {
                int outIndex = ARR_SIZE * i + k;
                System.out.println(outIndex);
                out[outIndex] = temp[k];
            }
        }
        return out;
    }
}
