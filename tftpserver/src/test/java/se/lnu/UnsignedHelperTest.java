package se.lnu;


import org.junit.Test;
import se.lnu.handlers.UnsignedHelper;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Jakob on 2016-03-07.
 */
public class UnsignedHelperTest
{


    @Test
    public void intTo2UnsignedBytes_65535_returnsNeg1Neg1()
    {
        int value = (int) Math.round(Math.pow(2, 16) - 1);
        byte[] unsigned =UnsignedHelper.intTo2UnsignedBytes(value);
        byte expectedVal = -1;      //1111 1111
        byte actual1 = unsigned[0];
        byte actual2 = unsigned[1];
        assertEquals("Most significant bits fail", expectedVal, actual1);
        assertEquals("Least significant bits fail", expectedVal, actual2);
    }

    @Test
    public void intTo2UnsignedBytes_255_returns0Neg1()
    {
        int value = (int) Math.round(Math.pow(2, 8) - 1);
        byte[] unsigned =UnsignedHelper.intTo2UnsignedBytes(value);
        byte expectedVal1 = 0;
        byte expectedVal2 = -1;      //1111 1111
        byte actual1 = unsigned[0];
        byte actual2 = unsigned[1];
        assertEquals("Most significant bits fail", expectedVal1, actual1);
        assertEquals("Least significant bits fail", expectedVal2, actual2);
    }

    @Test
    public void twoBytesToInt_neg1neg1_returns65535()
    {
        byte[] val = new byte[]{-1,-1};
        int signedIntVal = UnsignedHelper.twoBytesToInt(val);
        int expected = 65535;
        assertEquals("TFTPError converting from unsigned bytes to signed integer", expected, signedIntVal);
    }
}
