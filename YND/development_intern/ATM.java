import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.Integer.*;

public class ATM {
    
    private static final int denominationArr[] = new int[]{5000, 1000, 500, 100, 50, 25, 10, 5, 3, 1};
    
    private final static HashMap<Integer, Integer> denominationMap = new HashMap<>();
    
    private final HashMap<Integer, Integer> getResult;
    
    private long totalSummary;
    
    private ATM() {
        totalSummary = 0;
        getResult = new HashMap<>();
        
        setDenomination();
    }
    
    private void setDenomination() {
        Arrays.stream(denominationArr).forEach(v -> denominationMap.put(v, 0));
    }
    
    public static void main(String[] args) throws IOException {
        new ATM().run();
    }
    
    private void run() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String newCommand;
        StringTokenizer st;
        
        while ((newCommand = in.readLine()) != null) {
            st = new StringTokenizer(newCommand);
            String command = st.nextToken();
            switch (command) {
                case "put":
                    put(parseInt(st.nextToken()), parseInt(st.nextToken()));
                    state();
                    break;
                case "get":
                    get(parseInt(st.nextToken()));
                    break;
                case "dump":
                    dump();
                    break;
                case "state":
                    state();
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.out.print("ERROR: Unexpected command");
            }
        }
    }
    
    private void put(int denomination, int amount) {
        if (!denominationMap.containsKey(denomination)) {
            System.out.println("ERROR: Unexpected denomination");
        } else {
            totalSummary += amount * denomination;
            //            denominationMap.replace(denomination, denominationMap.get(denomination) + amount);
            denominationMap.compute(denomination, (integer, integer2) -> integer2 + amount);
        }
    }
    
    private void state() {
        System.out.println(totalSummary);
    }
    
    private void get(int amount) {
        
        int lastNominal[] = new int[amount + 1];
        lastNominal[0] = -1;
        
        for (Integer curDenomination : denominationArr) {
            boolean flag = denominationMap.containsKey(curDenomination);
            int denominationAmount = denominationMap.get(curDenomination);
            for (int i = 0; flag && i < denominationAmount; i++) {
                flag = false;
                for (int j = amount; j >= curDenomination; j--) {
                    if (lastNominal[j - curDenomination] != 0 && lastNominal[j] == 0) {
                        flag = true;
                        lastNominal[j] = curDenomination;
                    }
                }
            }
        }
        
        getResult.clear();
        int curNominal;
        int curAmount = 0;
        for (int i = amount; i >= 0; i--) {
            curAmount = i;
            while (lastNominal[i] > 0 && i >= 0) {
                curNominal = lastNominal[i];
                getResult.put(curNominal, getResult.getOrDefault(curNominal, 0) + 1);
                i -= lastNominal[i];
            }
            if (curAmount != i) break;
        }
        getResult.entrySet().stream()
        .sorted((o1, o2) -> compare(o1.getKey(), o2.getKey()))
        .forEach(x -> System.out.print(x.getKey() + "=" + x.getValue() + ","));
        System.out.println(" всего " + curAmount);
        if (curAmount != amount) {
            System.out.println("без " + (amount - curAmount));
        }
        
    }
    
    private void dump() {
        denominationMap.entrySet().stream()
        .sorted((o1, o2) -> compare(o1.getKey(), o2.getKey()))
        .filter(x_ -> !x_.getKey().equals(denominationArr[0]))
        .forEach(x -> System.out.print(x.getKey() + "=" + x.getValue() + ","));
        System.out.println(denominationArr[0]
                           + "=" + denominationMap.get(denominationArr[0]));
    }
    
    private void quit() {
        System.exit(0);
    }
}
