package se.lnu.domain.exeptions;


import se.lnu.domain.error.E3DiskFull;


public class E3DiskFullException extends Exception implements TFTPExceptionInterface{

    E3DiskFull e = new E3DiskFull();

    public E3DiskFullException() {
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
