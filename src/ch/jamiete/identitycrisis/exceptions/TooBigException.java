package ch.jamiete.identitycrisis.exceptions;

public class TooBigException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TooBigException(final String message) {
        super(message);
    }
}
