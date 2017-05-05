package org.stepik.titova;

import javafx.util.Pair;

import java.util.*;

import static org.stepik.titova.Parser.ops.*;
import static org.stepik.titova.Utils.*;

/**
 * Simplifies given tree.
 */
public class Simplifier {

    /**
     * Default constructor.
     */
    public Simplifier() {

    }

    /**
     * Simplifies given tree beginning with given {@link Node}.
     *
     * @param root {@link Node} to start simplifying.
     * @return root {@link Node} of simplified tree.
     */
    public static Node simplify(Node root) {
        return recSimple(root).getKey();
    }

    private static Pair<Node, Set<Node>> recSimple(Node node) {
        switch (node.getCurrent()) {
            case "OR":
                return helper(node, TRUE_NODE, FALSE_NODE);
            case "AND":
                return helper(node, FALSE_NODE, TRUE_NODE);
            case "NOT":
                return helper(node);
        }
        return new Pair<>(node, Collections.emptySet());
    }


    private static Pair<Node, Set<Node>> helper(Node node, Node first, Node second) {
        Set<Node> simplifiedData = new HashSet<>();
        String op = node.getCurrent();
        Queue<Node> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            Node q = queue.poll();
            if (!q.getCurrent().equals(op)) {
                q = recSimple(q).getKey();
            }
            if (q.getCurrent().equals(op)) {
                queue.add(q.getLeftChild());
                queue.add(q.getRightChild());
            } else {
                if (q.equals(first)) {
                    return pack(first);
                } else if (!q.equals(second)) {
                    simplifiedData.add(q);
                }
            }
        }
        if (itemWithNegation(simplifiedData))
            return pack(first);
        return new Pair<>(simplifiedData.stream()
                .sorted()
                .reduce((x, y) -> new Node(op, x, y))
                .orElse(second), simplifiedData);
    }

    private static boolean itemWithNegation(Set<Node> data) {
        return data.stream().anyMatch(datum -> data.containsAll(recSimple(negate(datum)).getValue()));
    }

    // NOT
    private static Pair<Node, Set<Node>> helper(Node node) {
        Node child = node.getRightChild();
        if (isBinaryOp(child.getCurrent())) {
            Node ll = negate(child.getLeftChild());
            Node rr = negate(child.getRightChild());
            String op = OR.hasSuchName(child.getCurrent()) ?
                    AND.toString() : OR.toString();

            return recSimple(new Node(op, ll, rr));
        }
        child = recSimple(child).getKey();
        if (child.equals(TRUE_NODE)) {
            return pack(FALSE_NODE);
        } else if (child.equals(FALSE_NODE)) {
            return pack(TRUE_NODE);
        } else if (NOT.hasSuchName(child.getCurrent())) {
            return pack(child.getRightChild());
        }
        return pack(node);
    }

    private static Pair<Node, Set<Node>> pack(Node node) {
        Set<Node> temp = new HashSet<>();
        temp.add(node);
        return new Pair<>(node, temp);
    }
}
