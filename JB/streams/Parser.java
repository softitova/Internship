package streams;

/**
 * Created by Sophia Titova on 30.04.17.
 */
public class Parser extends Node {

    private static String expr = "";

    private static int index = 0;
    enum ops {NOT, AND, OR};


    static Node parse(String str) {
        index = 0;
        expr = str;
        return OR();
    }

    private static void skipSpace() {
        while (index < expr.length() && expr.charAt(index) == ' ') {
            index++;
        }
    }

    private static Node OR() {
        Node l = AND();
        skipSpace();
        while (index < expr.length() - 1 && expr.substring(index, index + 2).equals(ops.OR.toString())) {
            index += 2;
            Node r = AND();
            check(l, r, index);
            l = new Node(ops.OR.toString(), l, r);
        }
        return l;
    }

    private static Node AND() {
        Node l = NOT();
        skipSpace();
        while (index < expr.length() - 2 && expr.substring(index, index + 3).equals(ops.AND.toString())) {
            index += 3;
            Node r = AND();
            check(l, r, index);
            l = new Node(ops.AND.toString(), l, r);
        }
        return l;
    }


    private static Node NOT() {

        Node curRoot;
        skipSpace();
        if (index < expr.length() - 2 && expr.substring(index, index + 3).equals(ops.NOT.toString())) {
            index += 3;
            Node r = NOT();
            if (r == null) {
                System.out.println("Incorrect input since pos: " + index);
                System.exit(0);
            }
            curRoot = new Node(ops.NOT.toString(), null, r);
        } else if (expr.charAt(index) == '(') {
            index++;
            curRoot = OR();
            index++;
        } else {
            StringBuilder val = new StringBuilder();
            while (index < expr.length() && Character.isLetterOrDigit(expr.charAt(index))) {
                val.append(expr.charAt(index));
                index++;
            }
            curRoot = new Node(val.toString());
        }
        return curRoot;
    }


    private static void check(Node l, Node r, int index) {
        if (l == null && r == null) {
            System.out.println("Incorrect input since pos: " + index);
            System.exit(0);
        }
    }
}
