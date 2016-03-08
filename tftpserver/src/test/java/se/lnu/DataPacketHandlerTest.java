package se.lnu;

import org.junit.Test;
import se.lnu.domain.TFTPDataPacket;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * Created by Jakob on 2016-03-08.
 */
public class DataPacketHandlerTest
{
    @Test
    public void getReadPackets_largeInput_returns65535packets() throws IOException
    {
        File text = new File("MacBeth.txt");
        //System.out.println(text.exists());
        Path p = Paths.get(text.getAbsolutePath());
        ArrayList<TFTPDataPacket> packets = getReadPackets(p);
        System.out.println(packets.size());
        for (int i = packets.size()-10; i < packets.size(); i++)
        {
            System.out.println(packets.get(i));
        }
    }

    public ArrayList<TFTPDataPacket> getReadPackets(Path toFile) throws IOException
    {
        byte[] dataInBytes = Files.readAllBytes(toFile);
        final int ARR_SIZE = 1;
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
