package se.lnu.egnatester;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Jakob on 2016-03-05.
 */
public class FolderForFile
{
    public static void main(String[] args) throws IOException
    {
        File f = new File("C:\\Users\\Steve\\Documents\\TFTPFolder\\readReqs\\test.txt");
        System.out.println(f.exists());
        Path p = Paths.get("C:\\Users\\Steve\\Documents\\TFTPFolder\\readReqs\\test.txt");
        byte[] fileToBytes = Files.readAllBytes(p);
        System.out.println(fileToBytes.length);

        int noArrays = fileToBytes.length / 512  + 1;
        System.out.println("Estimated byte arrays = " + noArrays);

        byte[][] content = new byte[noArrays][];
        for (int i = 0; i < content.length - 1; i++)
        {
            int startIndex = 0 + 512 * i;
            int endIndex = 511 + 512 * i;
            content[i] = new byte[512];
            for (int k = startIndex; k <= endIndex; k++)
            {
                content[i][(k - 512 * i)] = fileToBytes[k];
            }
        }

        int startIndex = (content.length - 1) * 512;
        System.out.println("startIndex == " + startIndex);

        int bytesLeft = fileToBytes.length - startIndex;
        System.out.println("bytes left to read == " + bytesLeft);

        //Capturing last bytes
        for (int i = startIndex; i < fileToBytes.length; i++)
        {
            //content[content.length-1][i]
        }

        byte[] holdForStr = new byte[(content.length - 1) * 512];
        for (int i = 0; i < content.length - 1; i++)
        {
            for (int k = 0; k < content[i].length; k++)
            {
                holdForStr[i*512 + k] = content[i][k];
            }
        }
        String backToStr = new String(holdForStr);
        System.out.println(backToStr);
    }
}
