package se.lnu.domain;

/**
 * Created by nils on 3/4/16.
 */
public class ReadRequest extends OctetRequest
{
    public final static int READ_OPCODE = 2;
    public ReadRequest(String fileName)
    {
        super(READ_OPCODE, fileName);
    }
}
