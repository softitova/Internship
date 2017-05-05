package org.stepik.titova;

/**
 * Parse tree node.
 */
class Node implements Comparable<Node> {

    private String current = "";
    private Node leftChild = null;
    private Node rightChild = null;
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

    /**
     * @return current value of <code>Node</code>.
     */
    String getCurrent() {
        return current;
    }

    /**
     * @return left child of <code>Node</code>.
     */
    Node getLeftChild() {
        return leftChild;
    }

    /**
     * @return right child of <code>Node</code>.
     */
    Node getRightChild() {
        return rightChild;
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

    @Override
    public int compareTo(Node node) {
        return toString().compareTo(node.toString());
    }
}
