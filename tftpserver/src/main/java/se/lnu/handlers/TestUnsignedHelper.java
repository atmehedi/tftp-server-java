package se.lnu.handlers;

/**
 * Created by Jakob on 2016-03-07.
 * REMOVE BEFORE HAND IN
 */
public class TestUnsignedHelper
{
    public static void main(String[] args)
    {
        byte[] inbytes = UnsignedHelper.intTo2UnsignedBytes(32767);
        int backToInt = UnsignedHelper.twoBytesToInt(inbytes);
    }
}
