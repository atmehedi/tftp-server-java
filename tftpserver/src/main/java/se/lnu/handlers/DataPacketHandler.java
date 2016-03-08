package se.lnu.handlers;

import se.lnu.domain.TFTPDataPacket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by Jakob on 2016-03-07.
 */
public class DataPacketHandler
{

    public ArrayList<TFTPDataPacket> getReadPackets(Path toFile) throws IOException
    {
        byte[] dataInBytes = Files.readAllBytes(toFile);
        final int ARR_SIZE = 512;
        int noPackets = dataInBytes.length / ARR_SIZE + 1;
        ArrayList<TFTPDataPacket> out = new ArrayList<TFTPDataPacket>();
        for (int i = 0; i < noPackets - 1; i++)     //Last packet is smaller
        {
            byte[] data = new byte[ARR_SIZE];
            for (int k = 0; k < data.length; k++)
            {
                data[k] = dataInBytes[i*ARR_SIZE + k];
            }
            TFTPDataPacket toAdd = new TFTPDataPacket(data, (i + 1));
            out.add(toAdd);
        }
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
}
