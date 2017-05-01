package ATM;

import java.util.ArrayList;
import java.util.HashMap;

public class ATM { // logic module of ATM

    private static final HashMap<String, Integer> currencyIndices = new HashMap<>();
    private static final HashMap<Integer, Integer> denominationIndices = new HashMap<>();
    private static final int DENOMINATIONS_AMOUNT = 8;
    private static final int denominations[] = new int[]{1, 5, 10, 50, 100, 500, 1000, 5000};

    static {
        for (int i = 0; i < DENOMINATIONS_AMOUNT; i++) {
            denominationIndices.put(denominations[i], i);
        }
        currencyIndices.put("RUB", 0);
        currencyIndices.put("USD", 1);
        currencyIndices.put("EUR", 2);
    }

    private long coinsAmounts[][] = new long[3][8];

    public ATM() {
    }

    private boolean checkForCorrectCurrency(String currency) {
        return currencyIndices.containsKey(currency);
    }

    private boolean checkForCorrectDenomination(int denomination) {
        return denominationIndices.containsKey(denomination);
    }

    private void checkForCorrectCurrecyAndDenomination(String currency, int denomination) throws ATMException {
        if (!checkForCorrectCurrency(currency)) throw new WrongCurrencyException("Wrong currency: "+currency);
        if (!checkForCorrectDenomination(denomination)) throw new WrongDenominationException("Wrong denomination: "+denomination);
    }

    public long getTotalMoneyCount(String currency) throws WrongCurrencyException {
        if (!checkForCorrectCurrency(currency)) throw new WrongCurrencyException("Wrong currency: "+currency);
        int currencyIndex=currencyIndices.get(currency);
        long sum=0;
        for (int i=0; i<DENOMINATIONS_AMOUNT; i++) {
            sum+=coinsAmounts[currencyIndex][i]*denominations[i];
        }
        return sum;
    }

    public long getCointsAmount(int denomination, String currency) throws ATMException {
        checkForCorrectCurrecyAndDenomination(currency, denomination);
        return coinsAmounts[currencyIndices.get(currency)][denominationIndices.get(denomination)];
    }

    public boolean put(int number, int denomination, String currency) {
        if (!checkForCorrectCurrency(currency)) return false;
        if (!checkForCorrectDenomination(denomination)) return false;
        if (number<0) return false;
        coinsAmounts[currencyIndices.get(currency)][denominationIndices.get(denomination)] += number;
        return true;
    }

    public ArrayList<MoneyPack> get(int value, String currency) throws ATMException {
        if (!checkForCorrectCurrency(currency)) throw new WrongCurrencyException("Wrong currency: "+currency);
        int currencyIndex = currencyIndices.get(currency);
        ArrayList<MoneyPack> result = new ArrayList<>();
        for (int nominalIndex = DENOMINATIONS_AMOUNT -1; nominalIndex >= 0; nominalIndex--) {
            if (coinsAmounts[currencyIndex][nominalIndex] != 0) {
                long div = value / denominations[nominalIndex];
                if (div > 0 || (div == 0 && value % denominations[nominalIndex] == 0)) {
                    long lastCount = coinsAmounts[currencyIndex][nominalIndex] - div;
                    long count = div;
                    if (lastCount < 0) {
                        count = coinsAmounts[currencyIndex][nominalIndex];
                        coinsAmounts[currencyIndex][nominalIndex] = 0;
                    } else {
                        coinsAmounts[currencyIndex][nominalIndex] = lastCount;
                    }
                    value -= count * denominations[nominalIndex];
                    if (count != 0) {
                        result.add(new MoneyPack(denominations[nominalIndex], count));
                    }
                }
            }
            if (value == 0) {
                break;
            }
        }
        if (value != 0) {
            throw new InsufficientCashAmount("Error: can't give this sum");
        }
        return result;
    }
}