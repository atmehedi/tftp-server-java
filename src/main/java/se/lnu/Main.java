package se.lnu;

import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import se.lnu.domain.ACK;
import se.lnu.domain.OctetRequest;
import se.lnu.domain.TFTPDataPacket;
import se.lnu.domain.error.TFTPError;
import se.lnu.domain.exeptions.*;
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

        DatagramSocket datagramSocket = new DatagramSocket(null);
        SocketAddress socketAddress = new InetSocketAddress("0.0.0.0", port);
        datagramSocket.bind(socketAddress);
        datagramSocket.setReuseAddress(true);

        while (true){
            DatagramPacket recvDatagramPacket = new DatagramPacket(byteBuffer, byteBuffer.length);
            System.out.println("Waiting for packet");
            // Recv packet from client
            datagramSocket.receive(recvDatagramPacket);
            // Create new server thread instance
            // Pass the datagram packet the we got from client as argument
            TFTPServerThread tftpServer = new TFTPServerThread(recvDatagramPacket);
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
    SocketAddress remoteBindPointBackup;
    CommandLine cmd;

    public TFTPServerThread(DatagramPacket datagramPacket){
        this.recvDatagramPacket = datagramPacket;
        this.remoteBindPoint = datagramPacket.getSocketAddress();
        this.remoteBindPointBackup = datagramPacket.getSocketAddress();
    }

    @Override
    public void run(){

        OctetRequest fromClient;
        RequestParser requestParser = new RequestParser();
        SocketAddress socketAddress;
        DatagramSocket datagramSocket = null;

        try {

            datagramSocket = new DatagramSocket(null);
            fromClient = requestParser.getRequest(recvDatagramPacket);

            // Assign a port that is not occupied
            assignPort:
            for (;;)
            {
                try{
                    socketAddress = new InetSocketAddress("0.0.0.0", RandomInt());
                    datagramSocket.bind(socketAddress);
                    break assignPort;
                }
                catch (BindException e){}
            }

            if (fromClient.isReadRequest())
            {
                LOG.debug("Serving read request, host: " + remoteBindPoint.toString());

                RequestValidator requestValidator = new RequestValidator();

                // If request from client is valid
                requestValidator.validateRequestedReadFile(fromClient.getFileName());


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

                            datagramSocket.send(sendPack); // Send package
                            // Wait for ack from client
                            datagramSocket.receive(recvDatagramPacket);

                            ACKParser ackParser = new ACKParser();
                            ACK ack = ackParser.getAck(recvDatagramPacket);

                            if (ack.getAckNr() == packet.getBlockNr()){
                                System.out.println("Recv package nr: "+ack.getAckNr());
                                break sendPackageAndCheckAck;
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

            else if (fromClient.isWriteRequest())
            {
                LOG.debug("Serving write request, host: " + remoteBindPoint.toString());

                RequestValidator requestValidator = new RequestValidator();
                requestValidator.validateRequestedWriteFile(fromClient.getFileName()); // Validate
                DataPacketHandler dataPacketHandler = new DataPacketHandler();

                dataPacketHandler.prepareToReceive();

                byte[] firstAckToClient = new byte[4];
                firstAckToClient[0] = 0;
                firstAckToClient[1] = 4;
                firstAckToClient[2] = 0;
                firstAckToClient[3] = 0;

                DatagramPacket firstAckToClienPacket = new DatagramPacket(firstAckToClient, firstAckToClient.length, remoteBindPoint);

                datagramSocket.send(firstAckToClienPacket);

                int timeouts = 1;
                int max_timeouts = 5;
                datagramSocket.setSoTimeout(2000);  // Set timeout to 2 sec

                reciveAndSendAckLook: for (;;){

                    try {
                        datagramSocket.receive(recvDatagramPacket);

                        // If last packet, copy it to a new array.
                        byte[] revcData = null;
                        if (recvDatagramPacket.getLength() < 516) {
                            revcData = Arrays.copyOf(recvDatagramPacket.getData(), recvDatagramPacket.getLength());
                        } else {
                            revcData = recvDatagramPacket.getData();
                        }

                        // Get Ack
                        ACK ackToClient = dataPacketHandler.parseAndReceiveTFTPDataPacket(revcData);

                        // Send Ack back to client
                        DatagramPacket ackToClienPacket = new DatagramPacket(ackToClient.returnAckAsBytes(), ackToClient.returnAckAsBytes().length, remoteBindPoint);
                        datagramSocket.send(ackToClienPacket);

                        if (dataPacketHandler.lastPacketReceived()) {
                            dataPacketHandler.writePacketsToFile(fromClient.getFileName());
                            break reciveAndSendAckLook;
                        }
                    }
                    catch (SocketTimeoutException e){
                        LOG.debug("Timeout occured: "+ timeouts);
                        if (timeouts > max_timeouts){
                            LOG.debug("Max timeouts reached. Stop sending.");
                            break reciveAndSendAckLook;

                        }
                        timeouts++;
                    }
                }
            }
        }

        catch (Exception e){

            TFTPExceptionInterface tftpExceptionInterface = null;

            if (e instanceof E0NotDefinedException){
                tftpExceptionInterface = new E0NotDefinedException(e.getMessage());
            }

            else if (e instanceof E1FileNotFoundException){
                tftpExceptionInterface = new E1FileNotFoundException();
            }

            else if (e instanceof E2AccessViolationException){
                tftpExceptionInterface = new E2AccessViolationException();
            }

            else if (e instanceof E3DiskFullException){
                tftpExceptionInterface = new E3DiskFullException();
            }

            else if (e instanceof E4IllegalTFTPOperationException){
                tftpExceptionInterface = new E4IllegalTFTPOperationException();
            }

            else if (e instanceof E5InvalidTransferIDException){
                tftpExceptionInterface = new E5InvalidTransferIDException();
            }

            else if (e instanceof E6FileAlreadyExistException){
                tftpExceptionInterface = new E6FileAlreadyExistException();
            }

            else if (e instanceof E7NoSuchUserException){
                tftpExceptionInterface = new E7NoSuchUserException();
            }

            e.printStackTrace();

            this.sendErrorToClient(tftpExceptionInterface);
        }
    }

    private void sendErrorToClient(TFTPExceptionInterface tftpExceptionInterface){
        try{

            DatagramSocket datagramSocket = new DatagramSocket(null);

            DatagramPacket sendErrorPacket = new DatagramPacket(tftpExceptionInterface.getErrorAsPacket(),
                    tftpExceptionInterface.getErrorAsPacket().length,
                    remoteBindPointBackup);
            datagramSocket.send(sendErrorPacket);

        }
        catch (Exception e){
            System.err.println("Strange, we should never end up here!");
            e.printStackTrace();
            System.err.println("\nShuting down");
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

