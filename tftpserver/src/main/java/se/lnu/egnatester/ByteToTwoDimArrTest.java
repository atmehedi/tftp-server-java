package se.lnu.egnatester;

/**
 * Created by Jakob on 2016-03-06.
 * Simple test for trying out methods and stuff for the data packets. NOT
 * to be included when handed in to the teacher.
 */
public class ByteToTwoDimArrTest
{
    public static void main(String[] args)
    {
        String text = "";
        byte[] dataInBytes = text.getBytes();
        final int ARR_SIZE = 8;
        int noArrs = dataInBytes.length / ARR_SIZE + 1;
        System.out.println("Arrsize = " + noArrs);
        byte[][] dataArr = new byte[noArrs][];

        for (int i = 0; i < dataArr.length - 1; i++)
        {
            dataArr[i] = new byte[ARR_SIZE];
            for (int k = 0; k < ARR_SIZE; k++)
            {
                //System.out.println("dataInBytes[" + i + "]" + "[" + k + "] == dataInBytes[" + ((ARR_SIZE * i) + k) + "]");
                dataArr[i][k] = dataInBytes[(ARR_SIZE * i) + k];
            }
        }

        /*Oavsett vilket så måste ett sista paket skapas för även om det
        * är ett jämnt antal ska ett sista paket som innehåller 0-511 paket skickas*/

        int lastArray = dataArr.length - 1;
        System.out.println("Last array index == " + lastArray);
        int startIOfLastBytes = ARR_SIZE * lastArray;
        System.out.println("Index of start byte to read from == " + startIOfLastBytes);
        int bytesLeft = dataInBytes.length - startIOfLastBytes;
        System.out.println("Bytes left == " + bytesLeft);
        dataArr[dataArr.length - 1] = new byte[bytesLeft];
        for (int i = startIOfLastBytes; i < dataInBytes.length; i++)
        {
            dataArr[lastArray][i % ARR_SIZE] = dataInBytes[i];
        }

        byte[] backToBytes = new byte[dataInBytes.length];
        for (int i = 0; i < dataArr.length;i++)
        {
            System.out.println("dataArr[" + i + "] == " + new String(dataArr[i]));
        }
    }
}
