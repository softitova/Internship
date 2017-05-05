package org.stepik.titova;

/**
 * The class {@code ParseException} is a form of
 * {@code Throwable}.
 * <p>
 * Need to handle exceptions during parsing.
 * </p>
 */
public class ParseException extends Exception {
    private String msg;

    /**
     * Default constructor.
     */
    ParseException() {
        super();
    }

    /**
     * Creates an instance of {@link #ParseException} with mistake message.
     *
     * @param msg {@link String} view of exception message.
     */
    ParseException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}