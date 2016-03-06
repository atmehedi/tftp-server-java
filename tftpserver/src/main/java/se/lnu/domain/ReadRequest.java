package se.lnu.domain;

/**
 * Created by nils on 3/4/16.
 */
public class ReadRequest extends OctetRequest
{
    public ReadRequest(String fileName)
    {
        super(1, fileName);
    }
}
