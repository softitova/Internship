package converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class BytesToHexConverter {

    private static final String ERROR_MESSAGE = "Error: unexpected symbol";

    public static void main(String[] args) throws IOException, InterruptedException {
        convert(System.in, System.out, System.err);
    }

    public static void convert(InputStream in, PrintStream out, PrintStream err) throws IOException {
        long currentByte = in.read();
        while ((currentByte != -1)) {
            int checkByte = 0b11011111;
            int countOfBytes = 1;
            boolean flag = false;
            if ((currentByte & 0b01111111) == currentByte) {
                flag = true;
            } else {
                for (int i = 0; i < 8; i++) {
                    countOfBytes++;
                    if ((currentByte & checkByte) == currentByte) {
                        flag = true;
                        break;
                    }
                    checkByte = (checkByte >> 1) | 0b10000000;
                }
            }
            if (flag == false || countOfBytes > 4) {
                err.println(ERROR_MESSAGE);
                return;
            }
            out.print(makeAnswer(currentByte));
            StringBuilder stBuilder= new StringBuilder(4);
            for (int i = 0; i < countOfBytes - 1; i++) {
                currentByte = in.read();
                if ((currentByte >> 6) != 0b10) {
                    err.println(ERROR_MESSAGE);
                    return;
                } else {
                    stBuilder.append(makeAnswer(currentByte));
                }
            }
            currentByte = in.read();
            out.println(stBuilder.toString());
        }
    }

    private static char intTo16Char(long i) {
        if (i < 10) return (char) ('0' + i);
        return (char) ('A' + i - 10);
    }

    private static String makeAnswer(long answerToConvert) {
        ArrayList<Character> result = new ArrayList<>();
        while (answerToConvert != 0) {
            result.add(intTo16Char(answerToConvert % 16));
            answerToConvert = answerToConvert / 16;
        }
        StringBuilder stBuilder = new StringBuilder(result.size());
        for (int i = result.size() - 1; i >= 0; i--) {
            stBuilder.append(result.get(i));
        }
        return stBuilder.toString();
    }
}
