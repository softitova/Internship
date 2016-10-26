

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import static java.lang.System.exit;
import static java.lang.System.out;


public class ATM {

    private static long amountOfDenomination[];
    private static final int denomination[] = new int[]{1, 3, 5, 10, 25, 50, 100, 500, 1000, 5000};
    private static final int AMOUNT_OF_DENOMINATION = 10;
    private long totalSummary;

    private int getIndexOfDenomination(int nominal) {
        for (int i = 0; i < AMOUNT_OF_DENOMINATION; i++) {
            if (denomination[i] == nominal) {
                return i;
            }
        }
        return -1;
    }

    private void copy(int prev[], int next[]) {
        for (int i = 0; i < Math.max(prev.length, next.length); i++) {
            next[i] = prev[i];
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ATM four = new ATM();
        amountOfDenomination = new long[AMOUNT_OF_DENOMINATION];
        String newCommand = in.readLine();
        StringTokenizer st;
        while (newCommand != null) {
            st = new StringTokenizer(newCommand);
            String command = st.nextToken();
            switch (command) {
                case "put":
                    four.put(Integer.parseInt(st.nextToken()), Long.parseLong(st.nextToken()));
                    four.state();
                    break;
                case "get":
                    four.get(Integer.parseInt(st.nextToken()));
                    break;
                case "dump":
                    four.dump();
                    break;
                case "state":
                    four.state();
                    break;
                case "quit":
                    four.quit();
                    break;
                default:
                    System.out.print("ERROR: Unexpected comand");
            }
            newCommand = in.readLine();
        }
    }


    public void put(int denomination, long count) {
        totalSummary += denomination * count;
        int index = getIndexOfDenomination(denomination);
        if (index == -1) {
            out.println("ERROR: unexpected denomination");
        } else {
            amountOfDenomination[index] += count;
        }
    }

    public void get(int amount) {
        int amountToGive = amount;
        int amountOfDenomPrev[] = new int[amount + 1];
        int amountOfDenomNext[] = new int[amount + 1];
        int INF = Integer.MAX_VALUE - 1;
        Arrays.fill(amountOfDenomPrev, INF);
        Arrays.fill(amountOfDenomNext, INF);
        amountOfDenomPrev[0] = 0;
        amountOfDenomNext[0] = 0;
        for (int denominationIndex = 0; denominationIndex < AMOUNT_OF_DENOMINATION; denominationIndex++) {
            for (int coinsCount = 0; coinsCount < amountOfDenomination[denominationIndex]; coinsCount++) {
                copy(amountOfDenomPrev, amountOfDenomNext);
                for (int j = denomination[denominationIndex]; j <= amount; j++) {
                    amountOfDenomNext[j] = Math.min(amountOfDenomNext[j],
                            amountOfDenomPrev[j - denomination[denominationIndex]] + 1);
                }
                copy(amountOfDenomNext, amountOfDenomPrev);
            }
        }
        while (amountOfDenomPrev[amount] == INF) {
            amount--;
        }
        int ans[] = new int[AMOUNT_OF_DENOMINATION];
        int possibleAmount = amount;
        while (amount > 0) {
            for (int i = 0; i < AMOUNT_OF_DENOMINATION; ++i) {
                if (amountOfDenomPrev[amount - denomination[i]] == amountOfDenomPrev[amount] - 1) {
                    ans[i]++;
                    amount -= denomination[i];
                    amountOfDenomination[i]--;
                    break;
                }
            }
        }
        for (int i = 0; i < AMOUNT_OF_DENOMINATION; i++) {
            if (ans[i] != 0) {
                out.print(denomination[i] + "=" + ans[i] + ", ");
            }
        }
        totalSummary -= possibleAmount;
        out.println("всего " + possibleAmount);
        if (possibleAmount < amountToGive) {
            out.println("без " + (amountToGive - possibleAmount));
        }
    }


    public void dump() {
        for (int i = 9; i > 0; i--) {
            System.out.print(denomination[i] + "=" + amountOfDenomination[i] + ", ");
        }
        System.out.println(denomination[0] + "=" + amountOfDenomination[0]);
    }

    public void state() {
        System.out.println("всего" + " " + totalSummary);

    }

    public void quit() {
        exit(0);
    }
}
