package no.uit.zhangway;

public class ClientException extends RuntimeException {
    public ClientException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ClientException(String message) {
        super(message);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ClientException(Throwable cause) {
        super(cause);
    }
}
