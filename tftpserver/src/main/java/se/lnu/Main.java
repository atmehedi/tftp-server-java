package se.lnu;

import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import se.lnu.domain.ACK;
import se.lnu.domain.OctetRequest;
import se.lnu.domain.TFTPDataPacket;
import se.lnu.handlers.ACKParser;
import se.lnu.handlers.DataPacketHandler;
import se.lnu.handlers.RequestParser;
import se.lnu.handlers.RequestValidator;


/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) throws Exception
    {

        final Logger LOG = Logger.getLogger(Main.class.getSimpleName());

        int byteBufferSize = 516;
        int port = 4970;
        byte[] byteBuffer = new byte[byteBufferSize];

        Options options = new Options();
        // commandline arguments
        options.addOption("h", "help", false, "show help.");
        options.addOption("d", "directory", true, "Read/Write directory");
        CommandLine cmd = null;

        try{
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        }
        catch (UnrecognizedOptionException e){
            System.err.println("Unrecognized option");
            help(options);
        }
        catch (MissingArgumentException e){
            System.err.println("Missign arguments options");
            help(options);
        }


        DatagramSocket datagramSocket = new DatagramSocket(null);
        SocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
        datagramSocket.bind(socketAddress);
        datagramSocket.setReuseAddress(true);

        while (true){
            DatagramPacket recvDatagramPacket = new DatagramPacket(byteBuffer, byteBuffer.length);
            System.out.println("Waiting for packet");
            // Recv packet from client
            datagramSocket.receive(recvDatagramPacket);
            // Create new server thread instance
            // Pass the datagram packet the we got from client as argument
            TFTPServerThread tftpServer = new TFTPServerThread(recvDatagramPacket, cmd);
            // Start the new server instance
            tftpServer.start();
            // Go back to waiting for client
        }
    }

    private static void help(Options options){
        // good looking helper
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("TFTP Server", options);
        System.exit(1);
    }

}


class TFTPServerThread extends Thread {

    final Logger LOG = Logger.getLogger(this.getClass().getSimpleName());

    DatagramPacket recvDatagramPacket;
    SocketAddress remoteBindPoint;
    CommandLine cmd;

    public TFTPServerThread(DatagramPacket datagramPacket, CommandLine cmd){
        this.recvDatagramPacket = datagramPacket;
        this.remoteBindPoint = datagramPacket.getSocketAddress();
        this.cmd = cmd;
    }

    @Override
    public void run(){
        try {
            OctetRequest fromClient;
            RequestParser requestParser = new RequestParser();
            fromClient = requestParser.getRequest(recvDatagramPacket);
            DatagramSocket datagramSocket = new DatagramSocket(null);
            SocketAddress socketAddress;

            // Assign a port that is not occupied
            assignPort: for (;;){
                try{
                    socketAddress = new InetSocketAddress("127.0.0.1", RandomInt());
                    datagramSocket.bind(socketAddress);
                    break assignPort;
                }
                catch (BindException e){}
            }

            System.out.println(socketAddress);




            if (fromClient.isReadRequest())
            {
                System.out.println("It's a read request");

                RequestValidator requestValidator = new RequestValidator();

                // If request from client is valid
                boolean validReq = requestValidator.validateRequestedReadFile(fromClient.getFileName());
                if (validReq){

                    Path path = Paths.get(fromClient.getREADDIR()+fromClient.getFileName());
                    System.out.println(path);
                    DataPacketHandler dataPacketHandler = new DataPacketHandler();
                    ArrayList<TFTPDataPacket> arrayList = dataPacketHandler.getReadPackets(Paths.get(fromClient.getFileName()));
                    datagramSocket.setSoTimeout(2000);  // Set timeout to 2 sec

                    iterateOverAllPackages: for (TFTPDataPacket packet: arrayList){  // Iterate over all packages

                        int timeouts = 1;
                        int max_timeouts = 5;

                        sendPackageAndCheckAck: for (;;){  // Send packages, Verify Ack block nr, Work with timeouts

                            try{
                                DatagramPacket sendPack = new DatagramPacket(packet.getPacket(), packet.getPacket().length, remoteBindPoint);
                                System.out.println("Send package nr: " + packet.getBlockNr());
                                datagramSocket.send(sendPack); // Send package
                                // Wait for ack from client
                                datagramSocket.receive(recvDatagramPacket);

                                ACKParser ackParser = new ACKParser();
                                ACK ack = ackParser.getAck(recvDatagramPacket);

                                if (ack.getAckNr() == packet.getBlockNr()){
                                    System.out.println("Recv package nr: "+ack.getAckNr());
                                    break sendPackageAndCheckAck;
                                }
                                else {
                                    System.out.println("The ACK nr we got from client dont match the one we sent.");
                                    // Should we do anything here?
                                }


                                System.out.println();
                            }
                            catch (SocketTimeoutException e){
                                System.out.println("Timeout occured: "+ timeouts);
                                if (timeouts > max_timeouts){
                                    System.out.println("Max timeouts reached. Stop sending.");
                                    break iterateOverAllPackages;

                                }
                                timeouts++;
                            }
                        }
                    }

                }
                else{
                    // Handle error
                }
            }
        }

        catch (Exception e){
            LOG.debug("Failed to parse recvDatagramPacket with RequestParser.getRequest()");
            System.exit(1);
        }
    }

    private static int RandomInt(){
        int min = 65000;
        int max = 65534;
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}

