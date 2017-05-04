package streams;

/**
 * Created by Sophia Titova on 30.04.17.
 */

class Node {

    String current = "";
    Node leftChild = null;
    Node rightChild = null;
    String stringView = "";

    @Override
    public String toString() {
        return stringView;
    }

    @Override
    public int hashCode() {
        return stringView.hashCode();
    }

    public boolean equals(Node t) {
        return t != null && this.hashCode() == t.hashCode();
    }

    Node() {
    }

    Node(String value) {
        if (value != null)
            stringView = current = value;
    }

    Node(String value, Node l, Node r) {
        current = value;
        leftChild = l;
        rightChild = r;
        if (leftChild != null)
            stringView += "(" + leftChild.toString() + ")";
        stringView += current;
        if (rightChild != null)
            stringView += "(" + rightChild.toString() + ")";
    }

}