

import java.io.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TerminalApp {

    private static final int SYMBOL_LENGTH = 3;
    private static final int up[] = new int[]{27, 91, 65};
    private static final int down[] = new int[]{27, 91, 66};
    private static final int right[] = new int[]{27, 91, 67};
    private static final int left[] = new int[]{27, 91, 68};
    private static final int ESC_CODE = 27;
    private static final int ESC_CODE_LENGTH = 1;

    private static boolean comp(char[] a, int b[]) {
        for (int i = 0; i < SYMBOL_LENGTH; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        setTerminalToCBreak();
        //TODO нормальнозавершаться и красивый код? точка входа?
        startWalk();

    }

    /**
     * this method paint symbol @ onto terminal
     * react on users actions such as shift up, down, left, right;
     * when esc was pressed program have to completed
     * there are some difficulties cause of the start point (0,0) is the same with (0,1) ; (1, 0) and (1.1) in terminal
     * so here we have to choose which point we want to make a start point
     * also after the end of this program we have to print in terminal "reset"
     * it can be another way to make exit better like using stty(intr)
     */
    private static void startWalk() {
        new Thread(() -> {
            int xStart = 1;
            int yStart = 1;
            int x = 1;
            int y = 1;
            System.out.print("\033[H\033[2J\033[?25l");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    if (System.in.available() != 0) {
                        char buffer[] = new char[5];
                        int sz = 0;
                        if ((sz = in.read(buffer, 0, 4)) != -1) {
                            if (sz == ESC_CODE_LENGTH && buffer[0] == ESC_CODE) {
                                System.out.print("\033[H\033[2J");
                                System.exit(0);
                            } else if (sz == SYMBOL_LENGTH) {
                                if (comp(buffer, up)) {
                                    y = Math.max(1, y - 1);
                                } else if (comp(buffer, down)) {
                                    y++;
                                } else if (comp(buffer, right)) {
                                    x++;
                                } else if (comp(buffer, left)) {
                                    x = Math.max(1, x - 1);
                                }

                                System.out.print("\033[H\033[2J");

                                if (x != xStart || y != yStart) {
                                    System.out.print("\033[" + y + ";" + x + "H@");
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void setTerminalToCBreak() throws IOException, InterruptedException {
        stty("-icanon min 1");
        stty("-echo");
    }

    private static String stty(final String args) throws IOException, InterruptedException {
        String cmd = "stty " + args + " < /dev/tty";
        return exec(new String[]{"sh", "-c", cmd});
    }

    private static String exec(final String[] cmd) throws IOException, InterruptedException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Process p = Runtime.getRuntime().exec(cmd);
        int c;
        InputStream in = p.getInputStream();
        while ((c = in.read()) != -1) {
            bout.write(c);
        }
        in = p.getErrorStream();
        while ((c = in.read()) != -1) {
            bout.write(c);
        }
        p.waitFor();
        return new String(bout.toByteArray());
    }
}
