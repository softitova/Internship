package ATM;

public class WrongCurrencyException extends ATMException {
    public WrongCurrencyException(String s) {
        super(s);
    }
}
