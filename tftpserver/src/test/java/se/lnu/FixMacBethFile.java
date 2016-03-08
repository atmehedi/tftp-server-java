package se.lnu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Jakob on 2016-03-08.
 */
public class FixMacBethFile
{
    public static void main(String[] args) throws FileNotFoundException
    {
        File file = new File("MacBeth.txt");
        System.out.println(file.exists());
        Scanner fileScan = new Scanner(file);
        String input = "";
        while (fileScan.hasNextLine())
        {
            input +=fileScan.nextLine() + "\n";
        }
        input = input.substring(0, input.length() - 1);// remove last new line
        fileScan.close();
        System.out.println(input.length());
        String textWanted = input.substring(0, 65535);
        System.out.println(textWanted.length());
        PrintWriter out = new PrintWriter(file);
        out.println(textWanted);
        out.close();
    }
}
