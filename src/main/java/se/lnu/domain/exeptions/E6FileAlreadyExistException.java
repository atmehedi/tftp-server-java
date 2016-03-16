package se.lnu.domain.exeptions;


import se.lnu.domain.error.E6FileAlreadyExist;


public class E6FileAlreadyExistException extends Exception implements TFTPExceptionInterface{

    E6FileAlreadyExist e = new E6FileAlreadyExist();

    public E6FileAlreadyExistException() {
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
