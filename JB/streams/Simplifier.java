package streams;

import javafx.util.Pair;
import streams.Parser.lits;
import streams.Parser.ops;

import java.util.HashSet;
import java.util.Set;

public class Simplifier {

    HashSet<String> orSet;
    HashSet<String> andSet;

    Simplifier() {
        orSet = new HashSet<>();
        andSet = new HashSet<>();
    }

    Node simplify(Node root) {
        return recSimple(root);
    }

    private Node recSimple(Node node) {

        switch (node.current) {
            case "OR":
                return helper(node,
                        ops.OR.toString(),
                        lits.TRUE.toString(), lits.FALSE.toString(),
                        andSet, orSet);
            case "AND":
                return helper(node,
                        ops.AND.toString(),
                        lits.FALSE.toString(), lits.TRUE.toString(),
                        orSet, andSet);
            case "NOT":
                return helper(node);

        }
        return node;
    }

    private Node helper(Node node, String op, String first, String second, HashSet<String> setF, HashSet<String> setS) {

        HashSet<String> ss = new HashSet<>(setS);
        HashSet<String> sf = new HashSet<>(setF);

        Pair<Boolean, Node> t;

        Node l = recSimple(node.leftChild);
        if ((t = check(l, node.rightChild, setF, first)).getKey()) {
            return t.getValue();
        }
        Node r = recSimple(node.rightChild);
        setF = sf;
        setS = ss;
        if (!op(r.current) && (t = check(r, l, setF, first)).getKey()) {
            return t.getValue();
        }
        return l.current.equals(first) || r.current.equals(first) ?
                new Node(first) : r.current.equals(second) ?
                l : l.current.equals(second) || l.equals(r) ?
                r : new Node(op, l, r);

//        if (l.current.equals(first) || r.current.equals(first)) {
//            return new Node(first);
//        }
//        if (r.current.equals(second)) {
//            return l;
//        }
//        if (l.current.equals(second) || l.equals(r)) {
//            return r;
//        }
//        return new Node(op, l, r);
    }

    private Node helper(Node node) {
        Node r = node.rightChild;
        if (r.current.equals(ops.AND.toString())
                || r.current.equals(ops.OR.toString())) {

            Node ll = new Node(ops.NOT.toString(), null, r.leftChild);
            Node rr = new Node(ops.NOT.toString(), null, r.rightChild);
            String op = r.current.equals(ops.OR.toString()) ?
                    ops.AND.toString() : ops.OR.toString();

            return recSimple(new Node(op, ll, rr));
        } else {
            r = recSimple(node.rightChild);
        }
        return r.current.equals(lits.TRUE.toString()) ?
                new Node(lits.FALSE.toString()) : r.current.equals(lits.FALSE.toString()) ?
                new Node(lits.TRUE.toString()) : r.current.equals(ops.NOT.toString()) ?
                r.rightChild : new Node(ops.NOT.toString(), null, r);

//        if (r.current.equals(lits.TRUE.toString())) {
//            return new Node(lits.FALSE.toString());
//        }
//        if (r.current.equals(lits.FALSE.toString())) {
//            return new Node(lits.TRUE.toString());
//        }
//        if (r.current.equals(ops.NOT.toString())) {
//            return r.rightChild;
//        }
//
//        return new Node(ops.NOT.toString(), null, r);
    }

    private static boolean op(String s) {
        return s.equals(ops.NOT.toString())
                || s.equals(ops.AND.toString())
                || s.equals(ops.OR.toString())
                || s.equals(lits.FALSE.toString())
                || s.equals(lits.TRUE.toString());
    }


    private  boolean tryNegFind(String s, Set<String> set) {
        if (!op(s)) {
            if (s.length() > 3 && s.substring(0, 3).equals(ops.NOT.toString())) {
                return set.contains(s.substring(3));
            } else {
                return set.contains(ops.NOT.toString() + "(" + s + ")");
            }
        }
        return false;
    }

    private Pair<Boolean, Node> check(Node l, Node r, HashSet<String> set, String res) {
        if (tryNegFind(l.toString(), set)) {
            return new Pair<>(true, new Node(res));
        } else if (!set.add(l.toString())) {
            return new Pair<>(true, r);
        }
        return new Pair<>(false, null);
    }
}


