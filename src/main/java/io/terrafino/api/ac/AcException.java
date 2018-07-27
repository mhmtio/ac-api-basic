package io.terrafino.api.ac;

public class AcException extends Exception {

    public AcException(String msg) {
        super(msg);
    }

    public AcException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
