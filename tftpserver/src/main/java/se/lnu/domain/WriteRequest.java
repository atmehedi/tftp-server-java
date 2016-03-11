package se.lnu.domain;

/**
 * Created by Jakob on 2016-03-11.
 */
public class WriteRequest extends OctetRequest
{
    public final static int WRITE_OPCODE = 2;
    public WriteRequest(String fileName)
    {
        super(WRITE_OPCODE, fileName);
    }
}
