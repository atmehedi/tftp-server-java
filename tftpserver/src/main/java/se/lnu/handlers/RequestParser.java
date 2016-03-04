package se.lnu.handlers;

import org.apache.log4j.Logger;
import se.lnu.domain.Request;

import java.net.DatagramPacket;

/**
 * Created by nils on 3/4/16.
 */
public class RequestParser {

    final static Logger LOG = Logger.getLogger(RequestParser.class.getSimpleName());

    public static Request getRequest(DatagramPacket datagramPacket){

        byte[] packetBuffer = datagramPacket.getData();

        byte[] opcode = {packetBuffer[0], packetBuffer[1]};

        packetBuffer[1] = 0;

        String bufToStr = new String(packetBuffer);

        //String[] input = bufToStr.split(""); TODO: Split at char null

        LOG.debug("Read optcode from message" + opcode[0] + "" + opcode[1]);

        String[] fromBuffer = getReadableString(2, datagramPacket.getData());

        return null;
    }



    private static String[] getReadableString(int start, byte[] buffer){
        int i = start;
        char temp = (char) buffer[i];
        String filename = "";
        String mode = "";

        String[] returnArr = new String[2];
        while (temp != (char) 0){
            filename += temp;
            i++;
            temp = (char) buffer[i];
        }
        LOG.debug("Extracted filename: " + filename);

        returnArr[0] = filename;


        i++;
        temp = (char) buffer[i];
        while (temp != (char) 0){
            mode += temp;
            i++;
            temp = (char) buffer[i];
        }

        returnArr[1] = mode;

        LOG.debug("Extracted mode: " + mode);

        return returnArr;

    }
}
