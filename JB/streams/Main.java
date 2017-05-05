package org.stepik.titova;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Main {

    /**
     * Entry point into parsing and simplifying program.
     * <p>
     * Starts parsing and simplifying given on console strings.
     * Handles incorrect input by printing mistakes message on console and continues parsing next line.
     * <p>
     * Strops after receiving {@link String} <tt>quit</tt>.
     * </p>
     *
     * @param args command line arguments, ignored in current implementation.
     */
    public static void main(String[] args) throws FileNotFoundException {
        new Main().run();
    }

    private void run() throws FileNotFoundException {
        Scanner in = new Scanner(new InputStreamReader(System.in));
        Parser parser = new Parser();
        int i = 0;
        while (true) {
            String str = in.nextLine();
            switch (str) {
                case "quit":
                    System.exit(0);
                default:
                    execute(parser, str);
            }
        }
    }

    private void execute(Parser parser, String str) {
        try {

            Node simplified = Simplifier.simplify(parser.parse(str));
            System.out.println("assertTrue(check(\"" + simplified + "\", \"" + str + "\"));");
//            System.out.println(simplified);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}