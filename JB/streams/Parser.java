package streams;

/**
 * Created by Sophia Titova on 30.04.17.
 */
public class Parser extends Node {

    private String expr;
    private int index;

    Parser() {
        expr = "";
        index = 0;
    }

    Parser(String expr, int index) {
        this.expr = expr;
        this.index = index;
    }

    private static void check(Node l, Node r, int index) throws ParseException {
        if (l == null && r == null) {
            throw new ParseException("Unexpected token, error since pos: " + (index + 1));

        }
    }

    Node parse(String str) throws ParseException {
        index = 0;
        expr = str;
        Node res = OR();
        if (index < expr.length()) {
            throw new ParseException("Unexpected token since pos: " + (index + 1));
        }
        return res;
    }

    private void skipSpace() {
        while (index < expr.length() && expr.charAt(index) == ' ') {
            index++;
        }
    }

    private Node OR() throws ParseException {
        Node l = AND();
        skipSpace();
        while (index < expr.length() - 1 && expr.substring(index, index + 2).equals(ops.OR.toString())) {
            index += 2;
            Node r = OR();
            check(l, r, index);
            l = new Node(ops.OR.toString(), l, r);
        }
        return l;
    }

    private Node AND() throws ParseException {
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

    private Node NOT() throws ParseException {

        Node curRoot;
        skipSpace();
        if (index < expr.length() - 2 && expr.substring(index, index + 3).equals(ops.NOT.toString())) {
            index += 3;
            Node r = NOT();
            if (r == null) {
                throw new ParseException("Unexpected token, error since pos: " + (index + 1));
            }
            curRoot = new Node(ops.NOT.toString(), null, r);
        } else if (expr.charAt(index) == '(') {
            index++;
            curRoot = OR();
            index++;
        } else {
            int indexStart = index + 1;
            StringBuilder val = new StringBuilder();
            while (index < expr.length()
                    && Character.isLetterOrDigit(expr.charAt(index))) {
                val.append(expr.charAt(index));
                index++;
            }
            if (!val.toString().equals(lits.TRUE.toString())
                    && !val.toString().equals(lits.FALSE.toString())
                    && !val.toString().equals(val.toString().toLowerCase())) {
                throw new ParseException("Incorrect identifier: " + val.toString()
                        + " , error on pos: [" + indexStart + "..." + (index + 1) + "]");

            }
            curRoot = new Node(val.toString());
        }
        return curRoot;
    }


    enum ops {NOT, AND, OR}

    enum lits {TRUE, FALSE}
}
