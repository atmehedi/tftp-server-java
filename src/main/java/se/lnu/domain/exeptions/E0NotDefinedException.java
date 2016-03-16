package se.lnu.domain.exeptions;


import se.lnu.domain.error.E0NotDefined;
import se.lnu.domain.error.TFTPError;


/**
 * Created by nils on 3/12/16.
 */
public class E0NotDefinedException extends Exception implements TFTPExceptionInterface{

    E0NotDefined e0NotDefined;

    public E0NotDefinedException(String message) {
        super(message);
        e0NotDefined = new E0NotDefined(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public byte[] getErrorAsPacket(){
        return this.e0NotDefined.getError();
    }
}
