package streams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Sophia Titova on 30.04.17.
 */
public class Manager {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner in = new Scanner(new File("input.txt"));
        PrintWriter out = new PrintWriter("output.txt");
        String str = in.nextLine();
        Node root = Parser.parse(str);
        Node r = new Simplifier().simplify(root);
        System.out.println(r.toString());

    }

}
