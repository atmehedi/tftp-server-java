package se.lnu;

import org.apache.log4j.Logger;
import se.lnu.domain.ACK;
import se.lnu.domain.OctetRequest;
import se.lnu.domain.TFTPDataPacket;
import se.lnu.handlers.ACKParser;
import se.lnu.handlers.DataPacketHandler;
import se.lnu.handlers.RequestParser;
import se.lnu.handlers.RequestValidator;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws Exception
    {
        final Logger LOG = Logger.getLogger(Main.class.getSimpleName());
//        LOG.debug("Init server");
        /* Setting up the base for the main class*/
        int byteBufferSize = 516;
        int port = 4970;
        byte[] byteBuffer = new byte[byteBufferSize];
        DatagramSocket datagramSocket = new DatagramSocket(null);
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
        datagramSocket.bind(socketAddress);
        DatagramPacket recvDatagramPacket = new DatagramPacket(byteBuffer, byteBuffer.length);
        //System.out.println("Waiting for packet");
        RequestParser rp = new RequestParser();
        datagramSocket.receive(recvDatagramPacket);
        System.out.println(recvDatagramPacket.getPort());
        int remotePort = recvDatagramPacket.getPort();
        SocketAddress remoteBindPoint = new InetSocketAddress("127.0.0.1", remotePort);
        OctetRequest fromClient = rp.getRequest(recvDatagramPacket);
        System.out.println("Gotten a read request with opcode: " + fromClient.getOpcode()
                + "\nisReadRequest == " + fromClient.isReadRequest() + ", is writeRequest == " + fromClient.isWriteRequest()
                + "\nThe done file name == " + fromClient.getFileName());

        if (fromClient.isReadRequest())
        {
            System.out.println("It's a read request");
            //New interface for John:
            DataPacketHandler dp = new DataPacketHandler();
            RequestValidator rv = new RequestValidator();
            boolean validFile = rv.validateRequestedReadFile(fromClient.getFileName());
            if (validFile)
            {
                ArrayList<TFTPDataPacket> packets = dp.getReadPackets(Paths.get(fromClient.getFileName()));


                // Iterate over the to toClient.getPacket until we get to getLastPacketNr
//                for (int i = 1; i <= toClient.getLastPacketNr(); i++)
//                {
//
//                    byte[] msg = toClient.getPacket(i);
//                    DatagramPacket sendPack = new DatagramPacket(msg, msg.length, remoteBindPoint);
//                    System.out.println("Send package nr: " + i);
//                    datagramSocket.send(sendPack);
////                System.out.println("Waiting for ack");
//                    // Wait for ack from client
//                    datagramSocket.receive(recvDatagramPacket);
//
//                    ACKParser ap = new ACKParser();
//                    ACK ack = ap.getAck(recvDatagramPacket);
//                    System.out.println();
//                }
            }

            else
            {
                //Handle with errors
            }

        }
        else        //It's a write request
        {
            RequestValidator rv = new RequestValidator();
            boolean validFile = rv.validateRequestedWriteFile(fromClient.getFileName());        //Change to return errors
            if (validFile)
            {
                DataPacketHandler dp = new DataPacketHandler();
                
            }
            else
            {
                //Simply send the errors
            }
        }

    }
}
