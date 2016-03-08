package se.lnu.handlers;

import org.apache.log4j.Logger;
import se.lnu.domain.OctetRequest;
import se.lnu.domain.ReadRequest;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by nils on 3/4/16.
 */
public class RequestParser {

    final static Logger LOG = Logger.getLogger(RequestParser.class.getSimpleName());

    public static OctetRequest getRequest(DatagramPacket datagramPacket) throws IOException
    {
        byte[] packetBuffer = datagramPacket.getData();
        byte[] opcodeArr = {packetBuffer[0], packetBuffer[1]};
        int opcode = packetBuffer[0] + packetBuffer[1];
        System.out.println("Received following opcode: " + opcode);
        String fileName = getFilenameAndVerifyMode(2, datagramPacket.getData());
        OctetRequest out;
        if (opcode == 1)
        {
            File requestedFile = new File(OctetRequest.READDIR + fileName);
            System.out.println("Does the file exist? " + requestedFile.exists());
            System.out.println("Returning new read request");
            return new ReadRequest(fileName);
        }
        else if (opcode == 2)
        {
            System.out.println("\n\nUNIMPLEMENTED - got write request\n\n");
        }
        else {throw new IOException("Incompatible opcode received from client, received opcode == " + opcode);}

        return null;
    }

    private static String getFilenameAndVerifyMode(int start, byte[] buffer) throws IOException
    {
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
            throw new IOException("Mode requested was not octet and is not supported by server," +
                    " request mode received == " + mode);
        }

        return filename;
    }

}
