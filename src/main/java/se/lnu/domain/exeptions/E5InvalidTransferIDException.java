package se.lnu.domain.exeptions;


import se.lnu.domain.error.E5InvalidTransferID;


public class E5InvalidTransferIDException extends Exception implements TFTPExceptionInterface{

    E5InvalidTransferID e = new E5InvalidTransferID();

    public E5InvalidTransferIDException() {
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
