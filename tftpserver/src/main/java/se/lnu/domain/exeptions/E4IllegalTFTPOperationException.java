package se.lnu.domain.exeptions;


import se.lnu.domain.error.E4IllegalTFTPOperation;


public class E4IllegalTFTPOperationException extends Exception implements TFTPExceptionInterface {

    E4IllegalTFTPOperation e = new E4IllegalTFTPOperation();

    public E4IllegalTFTPOperationException() {
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
