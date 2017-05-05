package org.stepik.titova;


import static org.stepik.titova.Parser.lits.FALSE;
import static org.stepik.titova.Parser.lits.TRUE;
import static org.stepik.titova.Parser.ops.*;

/**
 * Class contains helper methods for working with string expressions.
 */
class Utils {
    /**
     * Single {@link Node} with string view {@link Parser.lits#FALSE} value.
     */
    static final Node FALSE_NODE = new Node(FALSE.toString());
    /**
     * Single {@link Node} with string view {@link Parser.lits#TRUE}value.
     */
    static final Node TRUE_NODE = new Node(TRUE.toString());

    /**
     * Places brackets during printing {@link Node}.
     *
     * @param cur   current {@link Node}.
     * @param child child {@link Node}.
     * @return {@link String} view of child {@link Node}.
     */
    static String needParenthethis(Node cur, Node child) {
        if (OR.hasSuchName(cur.current) && AND.hasSuchName(child.current)) {
            return child.toString();
        }
        if (cur.current.equals(child.current) || !isBinaryOp(child.current)) {
            return child.toString();
        }
        if (cur.current.equals(NOT.toString()) && !isBinaryOp(child.current)) {
            return child.toString();
        }
        return "(" + child.toString() + ")";
    }

    /**
     * Creates {@link Node} with string view {@link Parser.ops#NOT} and right child which is given {@link Node}.
     *
     * @param t right child of created {@link Node}.
     * @return new {@link Node}.
     */
    static Node negate(Node t) {
        return new Node(NOT, null, t);
    }

    /**
     * Checks that given operation is binary.
     *
     * @param s {@link String} which is given operation to check.
     * @return if given operation is binary.
     */
    static boolean isBinaryOp(String s) {
        return s.equals(AND.toString()) || s.equals(OR.toString());
    }

    /**
     * Checks that {@link String} expression is a literal.
     *
     * @param s {@link String} to check.
     * @return if given {@link String} is literal.
     */
    static boolean isLit(String s) {
        return s.equals(FALSE.toString()) || s.equals(TRUE.toString());
    }

}


