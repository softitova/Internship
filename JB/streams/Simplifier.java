package streams;

import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class Simplifier {

    Set<String> orSet;
    Set<String> andSet;

    Simplifier() {
        orSet = new HashSet<>();
        andSet = new HashSet<>();
    }

    private static boolean op(String s) {
        return s.equals(Parser.ops.NOT.toString())
                || s.equals(Parser.ops.AND.toString())
                || s.equals(Parser.ops.OR.toString())
                || s.equals(lits.FALSE.toString())
                || s.equals(lits.TRUE.toString());
    }

    private static boolean tryAdd(String s, Set<String> set) {
        return !op(s) && !set.add(s);
    }

    private static boolean tryNegFind(String s, Set<String> set) {
        if (!op(s)) {
            if (s.length() > 3 && s.substring(0, 3).equals(Parser.ops.NOT.toString())) {
                return set.contains(s.substring(3));
            } else {
                return set.contains(Parser.ops.NOT.toString() + s);
            }
        }
        return false;
    }

    private static Pair<Boolean, Node> add(Node l, Node r, Set<String> set, String neg, String res) {

        if (l != null && tryNegFind(neg + l.current, set)) {
            return new Pair<>(true, new Node(res));
        }
        if (tryNegFind(neg + r.current, set)) {
            return new Pair<>(true, new Node(res));
        }
        if (l != null && tryAdd(neg + l.current, set)) {
            set.remove(neg + l.current);
            return new Pair<>(true, r);
        }
        if (tryAdd(neg + r.current, set)) {
            set.remove(neg + r.current);
            return new Pair<>(true, l);
        }
        return new Pair<>(false, null);
    }

    Node simplify(Node root) {
        return recSimple(root);
    }

    private Node helper(Node node, String op, String first, String second, Set<String> setF, Set<String> setS) {
        Node r = recSimple(node.rightChild);
        Node l = recSimple(node.leftChild);
        setF.clear();
        if (l == null) {
            return r;
        }
        if (r == null) {
            return l;
        }
        Pair<Boolean, Node> t = add(l, r, setS, "", first);
        if (t.getKey()) {
            return t.getValue();
        }
        if (l.current.equals(first) || r.current.equals(first)) {
            return new Node(first);
        }
        if (r.current.equals(second)) {
            return l;
        }
        if (l.current.equals(second) || l.equals(r)) {
            return r;
        }
        return new Node(op, l, r);
    }

    private Node helper(Node node) {
        Node r = recSimple(node.rightChild);
        Pair<Boolean, Node> t = add(node.leftChild, r, orSet, Parser.ops.NOT.toString(), lits.TRUE.toString());
        if (t.getKey()) {
            return t.getValue();
        }
        t = add(node.leftChild, r, andSet, Parser.ops.NOT.toString(), lits.FALSE.toString());
        if (t.getKey()) {
            return t.getValue();
        }
        if (r.current.equals(lits.TRUE.toString())) {
            return new Node(lits.FALSE.toString());
        }
        if (r.current.equals(lits.FALSE.toString())) {
            return new Node(lits.TRUE.toString());
        }
        if (r.current.equals(Parser.ops.NOT.toString())) {
            return r.rightChild;
        }
        if (node.rightChild.current.equals(Parser.ops.AND.toString())
                || r.current.equals(Parser.ops.OR.toString())) {
            Node ll = new Node(Parser.ops.NOT.toString(), null, r.leftChild);
            Node rr = new Node(Parser.ops.NOT.toString(), null, r.rightChild);
            String op = r.current.equals(Parser.ops.OR.toString()) ?
                    Parser.ops.AND.toString() : Parser.ops.OR.toString();
            return new Node(op, ll, rr); // NOT (x AND y) заменять на NOT (x) OR NOT (y)
        }
        return new Node(Parser.ops.NOT.toString(), null, r);
    }

    private Node recSimple(Node node) {

        switch (node.current) {
            case "OR":
                return helper(node,
                        Parser.ops.OR.toString(),
                        lits.TRUE.toString(), lits.FALSE.toString(),
                        andSet, orSet);
            case "AND":
                return helper(node,
                        Parser.ops.AND.toString(),
                        lits.FALSE.toString(), lits.TRUE.toString(),
                        orSet, andSet);
            case "NOT":
                return helper(node);

        }
        return node;
    }

    private enum lits {TRUE, FALSE}

}


