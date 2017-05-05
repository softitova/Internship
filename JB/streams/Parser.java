package org.stepik.titova;

import static org.stepik.titova.Parser.lits.FALSE;
import static org.stepik.titova.Parser.lits.TRUE;
import static org.stepik.titova.Parser.ops.*;
import static org.stepik.titova.Utils.*;

/**
 * Parses {@link String} and creates tree.
 */
class Parser {

    private String expr;
    private int index;

    /**
     * Default constructor.
     */
    public Parser() {

    }

    /**
     * Parse given string and creates tree.
     *
     * @param str String to parse.
     * @return {@link Node} of the root of syntax tree.
     * @throws ParseException if it was error during parsing i.e. incorrect input.
     */
    public Node parse(String str) throws ParseException {
        index = 0;
        expr = str;
        Node res = parseOr();
        if (index < expr.length()) {
            throw new ParseException("Unexpected token " + str.substring(index) + " since pos: " + index);
        }
        return res;
    }

    private void skipSpace() {
        while (index < expr.length() && Character.isWhitespace(expr.charAt(index))) {
            index++;
        }
    }

    private Node parseOr() throws ParseException {
        Node l = parseAnd();
        while (index < expr.length() - 1 && OR.hasSuchName(expr.substring(index, index + 2))) {
            index += 2;
            Node r = parseAnd();
            l = new Node(OR, l, r);
        }
        return l;
    }

    private Node parseAnd() throws ParseException {
        Node l = parseNot();
        while (index < expr.length() - 2 && AND.hasSuchName(expr.substring(index, index + 3))) {
            index += 3;
            Node r = parseNot();
            l = new Node(AND, l, r);
        }
        return l;
    }

    private Node parseNot() throws ParseException {
        Node curRoot;
        skipSpace();
        if (index == expr.length()) {
            throw new ParseException("Incorrect input, on pos: " + index);
        }
        if (index < expr.length() - 2 && NOT.hasSuchName(expr.substring(index, index + 3))) {
            index += 3;
            Node r = parseNot();
            curRoot = negate(r);
        } else if (expr.charAt(index) == '(') {
            index++;
            curRoot = parseOr();
            if (index >= expr.length() || expr.charAt(index) != ')') {
                throw new ParseException("Incorrect input, on pos: " + index + ", missing closing parenthethis");
            }

            index++;
        } else if (expr.substring(index).startsWith(TRUE.toString())) {
            index += TRUE.toString().length();
            curRoot = TRUE_NODE;
        } else if (expr.substring(index).startsWith(FALSE.toString())) {
            index += FALSE.toString().length();
            curRoot = FALSE_NODE;
        } else {
            int indexStart = index;
            while (index < expr.length()
                    && Character.isLowerCase(expr.charAt(index))) {
                index++;
            }
            String val = expr.substring(indexStart, index);
            if (val.isEmpty()) {
                throw new ParseException("Incorrect identifier: " + val
                        + " , error on pos: [" + (indexStart) + ".." + (index) + "]");
            }
            curRoot = new Node(val);
        }
        skipSpace();
        return curRoot;
    }


    /**
     * Supported operations.
     */
    enum ops {
        NOT, AND, OR;

        /**
         * Checks if string view of element of {@link ops} is equals given {@link String}.
         *
         * @param other {@link String} to check.
         * @return <code>true</code> if equals, <code>false</code> otherwise.
         */
        boolean hasSuchName(String other) {
            return toString().equals(other);
        }
    }

    /**
     * Supported literals.
     */
    enum lits {
        TRUE, FALSE;

        /**
         * Checks if string view of element of {@link lits} is equals given {@link String}.
         *
         * @param other {@link String} to check.
         * @return <code>true</code> if equals, <code>false</code> otherwise.
         */
        boolean hasSuchName(String other) {
            return toString().equals(other);
        }
    }
}