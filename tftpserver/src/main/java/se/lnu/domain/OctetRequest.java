package se.lnu.domain;


import java.io.File;

/**
 * Created by nils on 3/4/16.
 * Super class of requests. Don't contain any mode since the request parser doesn't
 * approve any other mode than octet.
 */
public abstract class OctetRequest
{
    public static final String READDIR = "/Users/nils/Desktop/";
    public static final String WRITEDIR = "C:\\Users\\Steve\\Documents\\TFTPFolder\\sendReqs\\";
    private int opcode;
    private String fileName;

    public OctetRequest(int opc, String fName)
    {
        opcode = opc;
        if (opc == 1) {fileName = READDIR + fName;}
        else {fileName = WRITEDIR + fName;}
        File f = new File(fileName);
        System.out.println("Existerar filen? " + f.exists());
    }

    public int getOpcode()
    {
        return opcode;
    }

    public String getFileName()
    {
        return fileName;
    }

    public boolean isReadRequest()
    {
        return opcode == 1;
    }

    public boolean isWriteRequest()
    {
        return opcode == 2;
    }

    public String getREADDIR() {
        return READDIR;
    }

    public String getWRITEDIR() {
        return WRITEDIR;
    }
}
