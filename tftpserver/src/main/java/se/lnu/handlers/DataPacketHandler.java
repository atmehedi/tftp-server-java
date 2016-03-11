package se.lnu.handlers;

import se.lnu.domain.ACK;
import se.lnu.domain.TFTPDataPacket;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by Jakob on 2016-03-07.
 * This class takes care of most of the nasty buisness of dealing with data
 * packets. Has methods for both receiving and sending data.
 */
public class DataPacketHandler
{
    /* Unless the prepareToReceive method is called, the following array list
    * will not be instantiated.*/
    private ArrayList<TFTPDataPacket> receivedPackets;
    /* Variable of the array size of the data part of the packet. Possible to
    * modify during development before compiling to make smaller data parts.*/
    public final static int ARR_SIZE = 512;

    /**
     * Main method to get the right data packets to send to a client. Takes a
     * path to a file as an argument, reads it
     * @param toFile
     * @return
     * @throws IOException
     */
    public ArrayList<TFTPDataPacket> getReadPackets(Path toFile) throws IOException
    {
        byte[] dataInBytes = Files.readAllBytes(toFile);
        /* Since the block numbers are limited by two unsigned bytes, the maximum
        * length of the dataInBytes array is 65535 bytes*/
        if (dataInBytes.length > 65535)
        {
            throw new IllegalArgumentException("The file is too big to transfer over the" +
                    " tftp protocol");
        }
        return logicOfSeparatingPackets(dataInBytes);
    }

    /**
     * This method make sure to divide all data into different data packets with
     * the correct block nrs, starting at 1 and with a maximum value of 65535.
     * It also make sure that the last packets data section is 0 to 511 bytes.
     * @param dataInBytes the data to be transfered in bytes
     * @return an sorted array list with all data packets in order.
     */
    public ArrayList<TFTPDataPacket> logicOfSeparatingPackets(byte[] dataInBytes)
    {
        /* The amount of packets needed, the plus one makes sure that there
        * will always be an extra packet where the data is 0 to 511 bytes.*/
        int noPackets = dataInBytes.length / ARR_SIZE + 1;
        ArrayList<TFTPDataPacket> out = new ArrayList<TFTPDataPacket>();
        /* Making all "full" packets, that is all packets that are 512 bytes*/
        for (int i = 0; i < noPackets - 1; i++)     //Last packet is smaller
        {
            byte[] data = new byte[ARR_SIZE];
            for (int k = 0; k < data.length; k++)
            {
                data[k] = dataInBytes[i*ARR_SIZE + k];
            }
            /* i + 1 = the block number.*/
            TFTPDataPacket toAdd = new TFTPDataPacket(data, (i + 1));
            out.add(toAdd);
        }
        /* Adding the last packet with 0 to 511 bytes of content*/
        int startIOfLastBytes = ARR_SIZE * (noPackets-1);
        int bytesLeft = dataInBytes.length - startIOfLastBytes;
        byte[] lastPack = new byte[bytesLeft];
        for (int i = startIOfLastBytes; i < dataInBytes.length; i++)
        {
            lastPack[i % ARR_SIZE] = dataInBytes[i];
        }
        TFTPDataPacket last = new TFTPDataPacket(lastPack, noPackets);
        out.add(last);
        return out;
    }

    /**
     * Set up the receivedPackets array list so it can start receiving packets.
     */
    public void prepareToReceive()
    {
        receivedPackets = new ArrayList<TFTPDataPacket>();
    }

    /**
     * This method will handle more or less everything that has to do with incoming
     * data packets from the client. It takes the entire packet from the client as
     * an argument and parse it to an TFTPDataPacket object and stores all incoming
     * packets from the client in an array list. It will always return an ack of the
     * last packet that arrived in order, the only way it will not is if it is not
     * an data packet passed as an argument or
     * @param fromClient the entire data packet received from the client.
     * @return ACK from last received packed in order if nothing that was to faulty
     * was passed as argument
     * @throws IllegalStateException and IllegalArgumentException in case there was
     * too little data (less than 4 bytes) or the wrong opcode.
     */
    public ACK parseAndReceiveTFTPDataPacket(byte[] fromClient)
    {
        /* Parse the array to an data packet*/
        TFTPDataPacket packet = parsePacket(fromClient);
        //System.out.println(packet);
        if (packet == null)     //The block nr of the data packet was zero.
        {
            if (receivedPackets.size() > 0) //Return last packet received in order
            {
                TFTPDataPacket lastReceived = receivedPackets.get(receivedPackets.size()-1);
                byte[] blockNrLastPack = UnsignedHelper.intTo2UnsignedBytes(lastReceived.getBlockNr());
                return new ACK(blockNrLastPack[0], blockNrLastPack[1]);
            }
            else        //No current packets, can't return ack
            {
                return null;
            }
        }
        else        //An actual data packet
        {
            if (receivedPackets.size() > 0)
            {
                TFTPDataPacket lastReceived = receivedPackets.get(receivedPackets.size() - 1);
                int blockNrLastRecieved = lastReceived.getBlockNr();
                if (blockNrLastRecieved + 1 == packet.getBlockNr())     //Make sure packets are successive
                {
                    receivedPackets.add(packet);
                }       //Else return ack of last packet received in order
                return getLastPacketAndReturnAck();

            }
            /* If the array is empty, we should receive the first packet.*/
            else
            {
                if (packet.getBlockNr() == 1) {
                    receivedPackets.add(packet);
                    return getLastPacketAndReturnAck();
                }
                else    //Else return null (There is no packet we can acknowledge)
                {
                    return null;
                }

            }
        }
    }

    /**
     * This is a straight forward method for getting the last packet received in order
     * and returning an ack for it.
     * @return an ack of the last received packet in order.
     */
    private ACK getLastPacketAndReturnAck()
    {
        TFTPDataPacket lastReceived = receivedPackets.get(receivedPackets.size() - 1);
        int blockNrLastRecieved = lastReceived.getBlockNr();
        byte[] bNrLastRecBytes = UnsignedHelper.intTo2UnsignedBytes(blockNrLastRecieved);
        return new ACK(bNrLastRecBytes[0], bNrLastRecBytes[1]);
    }

    /**
     * Simple parse method for a data packet.
     * @param fromClient
     * @return
     */
    private TFTPDataPacket parsePacket(byte[] fromClient)
    {
        if (fromClient.length < 4)      //Can't be a proper data packet
        {
            throw new IllegalArgumentException("The packet received from the client has to little content" +
                    " to be an data packet.");
        }
        int opcode = fromClient[0] + fromClient[1];
        if (opcode != 3)
        {
            throw new IllegalStateException("The opcode from the client isn't a data packet, the received " +
                    "opcode was: " + opcode);
        }
        int blockNr = UnsignedHelper.twoBytesToInt(new byte[]{fromClient[2], fromClient[3]});
        if (blockNr == 0)     //Data packets start from 1
        {
            //Shouldn't throw exception here, might be that the client sent packet 2 before 1.
            return null;
        }
        int startIndex = 4;
        /* Read the data from the packet*/
        byte[] data = new byte[fromClient.length - startIndex];
        for (int i = startIndex; i < fromClient.length; i++)
        {
            data[i-4] = fromClient[i];
        }
        return new TFTPDataPacket(data, blockNr);
    }

    /**
     * A method for seeing if the last packet has been received. TFTP specifies the
     * last data packet to have content of 0 to 511 bytes data, this method will check if
     * the last packet fulfills this condition.
     * @return true if last packet received, otherwise false.
     */
    public boolean lastPacketReceived()
    {
        if (receivedPackets.size() > 0)
        {
            TFTPDataPacket lastRec = receivedPackets.get(receivedPackets.size()-1);
            return lastRec.getPacket().length < 516;        //If last packet isn't filled, it was the last
        }
        return false;
    }

    /**
     * This method writes the content of the file to the specified path and the content of the data.
     * @param fileNameWithPath the absolute path with file name to the file that is to be written
     * @throws IOException
     */
    public void writePacketsToFile(String fileNameWithPath) throws IOException
    {
        if (lastPacketReceived())
        {
            //Write it to file
            /*TODO: When you are done with the main flow, make sure to test this method John*/
            byte[] rawData = getRawDataFromPackets();
            writeToFile(fileNameWithPath, rawData);
        }
        else
        {
            throw new IllegalStateException("The last packet haven't been received yet, cannot write to file");
        }
    }

    private void writeToFile(String fileNameWithPath, byte[] rawData) throws IOException
    {
        FileOutputStream outputStream = new FileOutputStream(fileNameWithPath);
        outputStream.write(rawData);
        outputStream.close();
    }

    /**
     * Help method that retrieves only the data section of all packets in order and
     * assembly them in to an single array.
     * @return an byte array of all received data packets.
     */
    private byte[] getRawDataFromPackets()
    {
        ArrayList<byte[]> dataOnlyArrs = new ArrayList<byte[]>();
        int arrLength = 0;
        for (TFTPDataPacket packet: receivedPackets)
        {
            dataOnlyArrs.add(packet.getDataOnly());
            arrLength += packet.getDataOnly().length;
            //System.out.println(arrLength);
        }
        byte[] out = new byte[arrLength];
        for (int i = 0; i < dataOnlyArrs.size(); i++)
        {
            byte[] temp = dataOnlyArrs.get(i);
            //System.out.println(new String(temp) + "   " + temp.length);
            for (int k = 0; k < temp.length; k++)
            {
                int outIndex = ARR_SIZE * i + k;
                //System.out.println(outIndex);
                out[outIndex] = temp[k];
            }
        }
        return out;
    }



}
