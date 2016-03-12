package se.lnu.domain.exeptions;


import se.lnu.domain.error.E7NoSuchUser;


public class E7NoSuchUserException extends Exception implements TFTPExceptionInterface{

    E7NoSuchUser e = new E7NoSuchUser();

    public E7NoSuchUserException() {
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
