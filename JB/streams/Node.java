package org.stepik.titova;

/**
 * Parse tree node.
 */
public class Node {

    String current = "";
    Node leftChild = null;
    Node rightChild = null;
    private String stringView = "";

    /**
     * Creates an instance of Node where {@link #current} value is given {@link String}.
     *
     * @param value given value to {@link #current}.
     */
    Node(String value) {
        if (value != null)
            stringView = current = value;
    }

    /**
     * Creates an instance of Node where {@link #current} value is given {@link String}.
     * and given {@link Node} for {@link #rightChild} and  {@link #rightChild}.
     *
     * @param value given value to {@link #current}.
     * @param l     given {@link Node} for {@link #leftChild}.
     * @param r     given {@link Node} for {@link #rightChild}.
     */
    Node(String value, Node l, Node r) {
        current = value;
        leftChild = l;
        rightChild = r;
        if (leftChild != null) {
            stringView += Utils.needParenthethis(this, leftChild) + " ";
        }
        stringView += current;
        if (rightChild != null) {
            stringView += " " + Utils.needParenthethis(this, rightChild);
        }
    }

    /**
     * Creates an instance of Node where {@link #current} value is received from {@link Parser.ops}.
     * and given {@link Node} for {@link #rightChild} and  {@link #rightChild}.
     *
     * @param op given from {@link Parser.ops} value to {@link #current}.
     * @param l  given {@link Node} for {@link #leftChild}.
     * @param r  given {@link Node} for {@link #rightChild}.
     */
    Node(Parser.ops op, Node l, Node r) {
        this(op.toString(), l, r);
    }

    @Override
    public String toString() {
        return stringView;
    }

    @Override
    public int hashCode() {
        return stringView.hashCode();
    }

    @Override
    public boolean equals(Object t) {
        if (t == null || !(t instanceof Node)) {
            return false;
        }
        return this.hashCode() == t.hashCode() && t.toString().equals(toString());
    }

}
