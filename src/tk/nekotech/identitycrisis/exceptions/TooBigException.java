package tk.nekotech.identitycrisis.exceptions;

public class TooBigException extends Exception {
    private static final long serialVersionUID = 1L;

    public TooBigException(final String message) {
        super(message);
    }
}
