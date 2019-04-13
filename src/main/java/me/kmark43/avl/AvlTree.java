package me.kmark43.avl;

import java.util.function.Consumer;

public class AvlTree<T extends Comparable<T>> {
    static boolean DEBUG = false;

    private Node<T> root;
    private int size;

    public AvlTree(){
    }

    AvlTree(Node<T> root, int size) {
        this.root = root;
        this.size = size;
        checkRep();
    }

    public boolean add(T value) {
        checkRep();

        if (!contains(value)) {
            root = add(root, value);
            size++;
            checkRep();
            return true;
        }

        checkRep();
        return false;
    }

    private Node<T> add(Node<T> current, T value) {
        if (current == null) {
            return new Node<>(value);
        } else {
            int cmp = value.compareTo(current.value);
            if (cmp < 0) {
                current.left = add(current.left, value);
            } else if (cmp > 0) {
                current.right = add(current.right, value);
            } else {
                return current;
            }

            updateHeightAndBal(current);
            current = restoreNodeRotation(current);

            return current;
        }
    }

    public boolean remove(T value) {
        checkRep();
        if (contains(value)) {
            root = remove(root, value);
            size--;
            checkRep();
            return true;
        }
        return false;
    }

    private Node<T> remove(Node<T> current, T value) {
        if (current.value.equals(value)) {
            if (current.left == null && current.right == null) {
                return null;
            } else if (current.left == null) {
                current = current.right;
            } else if (current.right == null) {
                current = current.left;
            } else if (current.left.right == null) {
                current.left.right = current.right;
                current = current.left;
            } else {
                Node<T> largestNode = getLargestNode(current.left);
                current.left = remove(current.left, largestNode.value);
                largestNode.left = current.left;
                largestNode.right = current.right;
                current = largestNode;
            }
        } else {
            int cmp = value.compareTo(current.value);
            if (cmp < 0) {
                current.left = remove(current.left, value);
            } else {
                current.right = remove(current.right, value);
            }
        }

        updateHeightAndBal(current);
        current = restoreNodeRotation(current);

        return current;
    }

    private Node<T> getLargestNode(Node<T> current) {
        if (current != null) {
            while (current.right != null) {
                current = current.right;
            }
        }
        return current;
    }

    public boolean contains(T value) {
        return contains(root, value);
    }

    private boolean contains(Node<T> current, T value) {
        if (current == null) {
            return false;
        } else if (current.value.equals(value)) {
            return true;
        } else {
            int cmp = value.compareTo(current.value);
            if (cmp < 0) {
                return contains(current.left, value);
            } else {
                return contains(current.right, value);
            }
        }
    }

    private Node<T> restoreNodeRotation(Node<T> current) {
        int bal = nodeBal(current);

        if (bal < -1) {
            if (current.left.bal <= 0) {
                current = rotateRight(current);
            } else {
                current.left = rotateLeft(current.left);
                current = rotateRight(current);
            }
        } else if (bal > 1) {
            if (current.right.bal >= 0) {
                current = rotateLeft(current);
            } else {
                current.right = rotateRight(current.right);
                current = rotateLeft(current);
            }
        }

        return current;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> temp = node.left;
        node.left = temp.right;
        temp.right = node;

        updateHeightAndBal(node);
        updateHeightAndBal(temp);
        return temp;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> temp = node.right;
        node.right = temp.left;
        temp.left = node;
        updateHeightAndBal(node);
        updateHeightAndBal(temp);
        return temp;
    }

    public int size() {
        return size;
    }

    public void forEach(Consumer<T> action) {
        forEach(root, action);
    }

    private void forEach(Node<T> current, Consumer<T> action) {
        if (current != null) {
            forEach(current.left, action);
            action.accept(current.value);
            forEach(current.right, action);
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        } else {
            StringBuilder builder = new StringBuilder("[");
            forEach(t -> builder.append(t.toString()).append(", "));
            builder.replace(builder.length() - ", ".length(), builder.length(), "]");
            return builder.toString();
        }
    }

    private int leftHeight(Node<T> node) {
        return node.left == null ? 0 : node.left.height;
    }

    private int rightHeight(Node<T> node) {
        return node.right == null ? 0 : node.right.height;
    }

    private int nodeBal(Node<T> node) {
        return rightHeight(node) - leftHeight(node);
    }

    private void updateHeightAndBal(Node<T> node) {
        int lHeight = leftHeight(node);
        int rHeight = rightHeight(node);
        node.height = Math.max(lHeight, rHeight) + 1;
        node.bal = rHeight - lHeight;
    }

    private void checkRep() {
        if (DEBUG) {
            checkAccurateHeightAndBal(root);
        }
    }

    private int checkAccurateHeightAndBal(Node<T> current) {
        if (current == null) {
            return 0;
        } else {
            int leftHeight = checkAccurateHeightAndBal(current.left);
            int rightHeight = checkAccurateHeightAndBal(current.right);
            int height = Math.max(checkAccurateHeightAndBal(current.left), checkAccurateHeightAndBal(current.right)) + 1;
            int bal = rightHeight - leftHeight;

            assert height == current.height : "Improper Height: " + current.value + " expected: " + height + " actual: " + current.height;
            assert bal == current.bal : "Improper balance: " + current.value + " expected: " + bal + " actual: " + current.bal;
            assert Math.abs(current.bal) <= 1 : "Difference in heights > 1: " + "(" + current.bal + ") " + current.value;

            return height;
        }
    }

    static class Node<T extends Comparable<T>> {
        T value;
        int height;
        int bal;
        Node<T> left;
        Node<T> right;

        Node(T value) {
            this.value = value;
            height = 1;
        }
    }
}
