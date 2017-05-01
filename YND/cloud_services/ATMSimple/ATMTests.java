package ATM;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static junit.framework.TestCase.*;

public class ATMTests { // tests for ATM

    private ATM atm = new ATM();
    private static final int NOMINALS_AMOUNT = 8;
    private static final int values[] = new int[]{1, 5, 10, 50, 100, 500, 1000, 5000};
    private static final String[] currencies = new String[]{"USD", "RUB", "EUR"};

    @Test
    public void testPutCorrectMoney() {
        for (int value : values) {
            for (String currency : currencies) {
                for (int addMoney = 0; addMoney < 10; addMoney++) {
                    assertTrue(atm.put(addMoney, value, currency));
                }
            }
        }
    }

    @Test
    public void testPutNegativeMoneyCount() {
        assertFalse(atm.put(-10, 5, "RUB"));
    }

    @Test
    public void testPutWrongCurrency() {
        assertFalse(atm.put(10, 5, "BITCOIN"));
    }

    @Test
    public void testPutWrongValue() {
        Random rd = new Random();
        int value = rd.nextInt(Integer.MAX_VALUE);
        for (int i = 0; i < 8; i++) {
            while (value == values[i]) {
                value = rd.nextInt(Integer.MAX_VALUE);
            }
        }
        assertFalse(atm.put(10, value, "RUB"));
    }

    @Test
    public void testPutLargeValue() throws ATMException {
        atm.put(Integer.MAX_VALUE, 1000, "USD");
        atm.put(Integer.MAX_VALUE, 1000, "USD");
        assertEquals((long) Integer.MAX_VALUE + Integer.MAX_VALUE, atm.getCointsAmount(1000, "USD"));
    }

    @Test
    public void testGetZeroAmount() throws Exception {
        atm.put(10, 1000, "RUB");
        ArrayList<MoneyPack> pack = atm.get(0, "RUB");
        assertEquals(0, pack.size());
    }

    @Test
    public void testGetWrongCurrency() throws ATMException {
        atm.put(10, 1000, "RUB");
        atm.put(10, 1000, "EUR");
        atm.put(10, 1000, "USD");
        WrongCurrencyException e = null;
        try {
            atm.get(1, "BITCOIN");
        } catch (WrongCurrencyException ex) {
            e = ex;
        }
        assertNotNull(e);
    }

    @Test
    public void testGetTooSmallAmount() throws ATMException {
        atm.put(10, 1000, "RUB");
        InsufficientCashAmount e = null;
        try {
            atm.get(1, "RUB");
        } catch (InsufficientCashAmount ex) {
            e = ex;
        }
        assertNotNull(e);
    }

    @Test
    public void testGetTooBigAmount() throws ATMException {
        atm.put(10, 1000, "RUB");
        InsufficientCashAmount e = null;
        try {
            atm.get(100000, "RUB");
        } catch (InsufficientCashAmount ex) {
            e = ex;
        }
        assertNotNull(e);
    }

    @Test
    public void testGetEmptyCurrency() throws ATMException {
        atm.put(10, 1000, "RUB");
        InsufficientCashAmount e = null;
        try {
            atm.get(10000, "USD");
        } catch (InsufficientCashAmount ex) {
            e = ex;
        }
        assertNotNull(e);
    }

    private static final int TEST_GET_RANDOM_ITERATIONS_NUMBER = 1000;
    private static final int TEST_GET_RANDOM_ADD_COINS_LIMIT = 100;
    private static final int TEST_GET_RANDOM_GET_TRIES_COUNT = 10;
    private static final int TEST_GET_RANDOM_GET_SUM_LIMIT = 10000;

    @Test
    public void testGetRandom() throws ATMException, FileNotFoundException { // it tests ATM for some number of "get" and "put" queries for random amount of denomination and necessary amount of money for each currency

        long success = 0;
        long fail = 0;
        for (int testNumber = 0; testNumber < TEST_GET_RANDOM_ITERATIONS_NUMBER; testNumber++) {
            System.out.println("Try " + testNumber + "/" + TEST_GET_RANDOM_ITERATIONS_NUMBER);

            Random rd = new Random();
            ATM atm = new ATM();
            for (String currency : currencies) {  // "put" queries
                for (int value : values) {
                    int addMoney = rd.nextInt(TEST_GET_RANDOM_ADD_COINS_LIMIT);
                    atm.put(addMoney, value, currency);
                }
            }
            for (String currency : currencies) {  // "get" queries
                for (int requestTryNumber = 0; requestTryNumber <= TEST_GET_RANDOM_GET_TRIES_COUNT; requestTryNumber++) {
                    boolean getOperationWasFailed = false;
                    ArrayList<MoneyPack> coinsSet = null;
                    int moneyToGet = rd.nextInt(TEST_GET_RANDOM_GET_SUM_LIMIT);
                    long[] oldCoinsAmount = new long[NOMINALS_AMOUNT];
                    for (int i = 0; i < NOMINALS_AMOUNT; i++) {
                        oldCoinsAmount[i] = atm.getCointsAmount(values[i], currency);
                    }
                    try {
                        coinsSet = atm.get(moneyToGet, currency);
                        success++;
                    } catch (InsufficientCashAmount ex) {
                        getOperationWasFailed = true;
                        fail++;
                    }
                    int gotSummaryMoney = 0;
                    if (!getOperationWasFailed) {
                        long[] newCoinsAmount = new long[NOMINALS_AMOUNT];
                        for (int i = 0; i < NOMINALS_AMOUNT; i++) { //checking given amount summary
                            newCoinsAmount[i] = atm.getCointsAmount(values[i], currency);
                        }
                        for (MoneyPack pack : coinsSet) {
                            gotSummaryMoney += pack.getDenomination() * pack.getCount();
                            int ind = -1;
                            for (int ii = 0; ii < NOMINALS_AMOUNT; ii++) {
                                if (values[ii] == pack.getDenomination()) {
                                    ind = ii;
                                }
                            }
                            assertTrue(oldCoinsAmount[ind] - newCoinsAmount[ind] == pack.getCount());
                        }
                        assertEquals(gotSummaryMoney, moneyToGet);
                    } else {  // check that we really can't get this summary
                        long totalHave = atm.getTotalMoneyCount(currency);
                        if (totalHave > moneyToGet) {
                            boolean dp[] = new boolean[moneyToGet + 1];
                            Arrays.fill(dp, false);
                            dp[0] = true;
                            for (int denominationIndex = 0; denominationIndex < NOMINALS_AMOUNT; denominationIndex++) {
                                for (int coinsCount = 0; coinsCount < oldCoinsAmount[denominationIndex]; coinsCount++) {
                                    for (int j = moneyToGet; j >= values[denominationIndex]; j--) {
                                        dp[j] |= dp[j - values[denominationIndex]];
                                    }
                                }
                            }
                            assertFalse(dp[moneyToGet]);
                        }
                    }
                }
            }
        }
        System.out.println("Success/total rate: " + (success * 100d) / (success + fail) + "%");
    }
}
