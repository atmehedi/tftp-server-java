package se.lnu;

import org.apache.log4j.Logger;
import se.lnu.domain.Request;
import se.lnu.handlers.RequestParser;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws Exception
    {
        final Logger LOG = Logger.getLogger(Main.class.getSimpleName());
        LOG.debug("Init server");

        int byteBufferSize = 516;
        int port = 4970;

        byte[] byteBuffer = new byte[byteBufferSize];

        DatagramSocket datagramSocket = new DatagramSocket(null);

        SocketAddress socketAddress = new InetSocketAddress("0.0.0.0", port);

        datagramSocket.bind(socketAddress);


        DatagramPacket recvDatagramPacket = new DatagramPacket(byteBuffer, byteBuffer.length);

        System.out.println("Waiting for packet");

        RequestParser rp = new RequestParser();


        datagramSocket.receive(recvDatagramPacket);


        Request fromClient = rp.getRequest(recvDatagramPacket);


        System.out.println("Recv packet");

        LOG.debug("Recv from client");
        LOG.debug("Data: " + new String(recvDatagramPacket.getData()));
        LOG.debug("Length: " + recvDatagramPacket.getLength());
        LOG.debug("Address: " + recvDatagramPacket.getAddress());
        LOG.debug("Port: " + recvDatagramPacket.getPort());

    }
}
