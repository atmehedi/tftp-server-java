package se.lnu.domain.exeptions;


import se.lnu.domain.error.E2AccessViolation;


public class E2AccessViolationException extends Exception implements TFTPExceptionInterface{

    E2AccessViolation e = new E2AccessViolation();

    public E2AccessViolationException() {
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
