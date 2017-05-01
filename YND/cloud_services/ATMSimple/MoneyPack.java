package ATM;

public class MoneyPack {

    private int denomination;
    private long count;

    public MoneyPack(int denomination, long count) {
        this.denomination = denomination;
        this.count = count;
    }

    public int getDenomination() {
        return denomination;
    }

    public long getCount() {
        return count;
    }
}
