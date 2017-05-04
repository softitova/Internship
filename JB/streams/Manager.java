package streams;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Sophia Titova on 30.04.17.
 */
public class Manager {


    public static void main(String[] args) throws FileNotFoundException {
        new Manager().run();
    }

    private void run() {
        Scanner in = new Scanner(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        Simplifier simplifier = new Simplifier();
        Parser parser = new Parser();
        while (true) {
            String str = in.nextLine();
            switch (str) {
                case "quit":
                    out.close();
                    System.exit(0);
                default:
                    execute(simplifier, parser, str);
            }
        }
    }

    private void execute(Simplifier simplifier, Parser parser, String str) {
        try {
            Node root = parser.parse(str);
            Node r = simplifier.simplify(root);
            System.out.println(r.toString());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}
