package ATM;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class RunATM {  // console interface for ATM
    public static void main(String[] args) throws IOException, ATMException {
        ATM atm = new ATM();
        BufferedReader inReader = new BufferedReader(new FileReader("input.txt"));
        PrintWriter outWriter = new PrintWriter(new File("output.txt"));
        String s = inReader.readLine();

        while (s != null) {
            StringTokenizer st = new StringTokenizer(s);
            String command = st.nextToken();
            if (command.equals("put")) {
                boolean result = atm.put(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), st.nextToken());
                if (!result) outWriter.write("false\n");
            } else {
                int sum = Integer.parseInt(st.nextToken());
                System.out.println(sum);
                ArrayList<MoneyPack> result = atm.get(sum, st.nextToken());
            }
            s = inReader.readLine();
        }
        outWriter.close();
    }
}
