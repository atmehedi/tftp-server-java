package se.lnu.domain;


public class ReadRequest extends OctetRequest
{
    public final static int READ_OPCODE = 1;
    public ReadRequest(String fileName)
    {
        super(READ_OPCODE, fileName);
    }
}
