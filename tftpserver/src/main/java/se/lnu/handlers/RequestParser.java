package se.lnu.handlers;

import org.apache.log4j.Logger;
import se.lnu.domain.OctetRequest;
import se.lnu.domain.ReadRequest;
import se.lnu.domain.WriteRequest;
import se.lnu.domain.exeptions.E0NotDefinedException;
import se.lnu.domain.exeptions.E1FileNotFoundException;
import se.lnu.domain.exeptions.E4IllegalTFTPOperationException;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by nils on 3/4/16.
 */
public class RequestParser {

    final static Logger LOG = Logger.getLogger(RequestParser.class.getSimpleName());

    public OctetRequest getRequest(DatagramPacket datagramPacket) throws Exception
    {
        byte[] packetBuffer = datagramPacket.getData();
        byte[] opcodeArr = {packetBuffer[0], packetBuffer[1]};
        int opcode = packetBuffer[0] + packetBuffer[1];
        System.out.println("Received following opcode: " + opcode);
        String fileName = getFilenameAndVerifyMode(2, datagramPacket.getData());
        if (opcode == 1)
        {
            File requestedFile = new File(OctetRequest.READDIR + fileName);
            if (!requestedFile.exists()){
                // If file not found, throw exception to client
                throw new E1FileNotFoundException();
            }
            System.out.println("Does the file exist? " + requestedFile.exists());
            System.out.println("Returning new read request");
            return new ReadRequest(fileName);
        }
        else if (opcode == 2)
        {
            return new WriteRequest(fileName);
        }

        else {
            LOG.debug("Incompatible opcode received from client, received opcode == " + opcode);
            throw new E4IllegalTFTPOperationException();
        }
    }

    private static String getFilenameAndVerifyMode(int start, byte[] buffer) throws Exception {
        //Retrieve filename
        int i = start;
        char temp = (char) buffer[i];
        String filename = "";
        while (temp != (char) 0)        //0 == end of string
        {
            filename += temp;
            i++;
            temp = (char) buffer[i];
        }

        //Retrieve mode
        String mode = "";
        i++;
        temp = (char) buffer[i];
        while (temp != (char) 0){
            mode += temp;
            i++;
            temp = (char) buffer[i];
        }

        if (!mode.equalsIgnoreCase("octet"))
        {
            System.err.println("Mode requested was not octet and is not supported by server," +
                    " request mode received == " + mode);
            throw new E4IllegalTFTPOperationException();
        }

        return filename;
    }

}
