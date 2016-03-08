package se.lnu.handlers;

/**
 * Created by Jakob on 2016-03-07.
 */
public class UnsignedHelper
{
    public static byte[] intTo2UnsignedBytes(int val)
    {
        int upperBound = (int) Math.round(Math.pow(2,16) - 1);
        System.out.println(upperBound);
        int lowerBound = 0;
        if (val < lowerBound || val > upperBound)
        {
            throw new IllegalArgumentException("Argument has to be 0 <= val <= (2^16-1)");
        }
        int no256s = val / 256;
        System.out.println(no256s + " st 256");
        byte[] out = new byte[2];
        out[0] = (byte) no256s;
        out[1] = (byte) (val % 256);
        System.out.println("out[0] == " + out[0]);
        System.out.println("out[1] == " + out[1]);
        return out;
    }

    public static int twoBytesToInt(byte[] inbytes)
    {
        int first = Byte.toUnsignedInt(inbytes[0]);
        int second = Byte.toUnsignedInt(inbytes[1]);
        int result = first * 256 + second;
        System.out.println(result);
        return result;
    }
}
