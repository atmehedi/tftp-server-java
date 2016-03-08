package se.lnu.egnatester;

/**
 * Created by Jakob on 2016-03-06.
 */
public class CastBytes
{
    public static void main(String[] args)
    {
        byte b = -1;
        int i = Byte.toUnsignedInt(b);
        System.out.println(i);
    }
}
