package se.lnu.domain.exeptions;


import se.lnu.domain.error.E1FileNotFound;


public class E1FileNotFoundException extends Exception implements TFTPExceptionInterface{
    E1FileNotFound e = new E1FileNotFound();

    public E1FileNotFoundException() {
        super();
    }

    @Override
    public String getMessage() {
        return this.e.getErrMSG();
    }

    public byte[] getErrorAsPacket(){
        return e.getError();
    }
}
