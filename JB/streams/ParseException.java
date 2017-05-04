package streams;

/**
 * Created by Sophia Titova on 01.05.17.
 */
public class ParseException extends Exception {
    String msg;

    ParseException() {
    }

    ParseException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
