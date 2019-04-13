package me.kmark43.avl;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AvlTests {
    @Before
    public void setAvlTreeData() {
        AvlTree.DEBUG = true;
    }

    @Test
    public void testDefaultTree() {
        AvlTree<Integer> tree = new AvlTree<>();
        assertEquals(0, tree.size());
        assertEquals("[]", tree.toString());
    }

    @Test
    public void testSingleAdd() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(2);
        assertEquals(1, tree.size());
        assertEquals("[2]", tree.toString());
    }

    @Test
    public void testDoubleAddBigger() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(2);
        tree.add(5);
        assertEquals(2, tree.size());
        assertEquals("[2, 5]", tree.toString());
    }

    @Test
    public void testDoubleAddSmaller() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(5);
        tree.add(2);
        assertEquals(2, tree.size());
        assertEquals("[2, 5]", tree.toString());
    }

    @Test
    public void testOrderedAdd() {
        AvlTree<Integer> tree = new AvlTree<>();
        for (int i = 0; i < 10; i++) {
            tree.add(i);
        }
        assertEquals(10, tree.size());
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", tree.toString());
    }

    @Test
    public void testReverseOrderedAdd() {
        AvlTree<Integer> tree = new AvlTree<>();
        for (int i = 0; i < 10; i++) {
            tree.add(9 - i);
        }
        assertEquals(10, tree.size());
        assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", tree.toString());
    }

    @Test
    public void testRepeatedAdd() {
        AvlTree<Integer> tree = new AvlTree<>();
        assertTrue(tree.add(5));
        assertFalse(tree.add(5));
    }

    @Test
    public void testAddRightLeftRotation() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(1);
        tree.add(5);
        tree.add(3);
        assertEquals(3, tree.size());
        assertEquals("[1, 3, 5]", tree.toString());
    }

    @Test
    public void testAddLeftRightRotation() {
        AvlTree<Integer> tree = new AvlTree<>();
        tree.add(5);
        tree.add(1);
        tree.add(3);
        assertEquals(3, tree.size());
        assertEquals("[1, 3, 5]", tree.toString());
    }

    @Test
    public void testRemoveEmpty() {
        AvlTree<Integer> tree = new AvlTree<>();
        assertFalse(tree.remove(5));
    }

    @Test
    public void testRemoveImproper() {
        AvlTree<Integer> tree = new AvlTree<>();
        assertFalse(tree.remove(5));
    }

    @Test
    public void testRemoveRoot() {
        AvlTree<Integer> tree = new AvlTree<>(node(5, 1, 0), 1);
        assertTrue(tree.remove(5));
        assertEquals(0, tree.size());
        assertFalse(tree.contains(5));
    }

    @Test
    public void testRemoveRootWithLeft() {
        AvlTree<Integer> tree = new AvlTree<>(node(5, 2, -1, node(3, 1, 0), null), 2);
        tree.remove(5);
        assertEquals("[3]", tree.toString());
    }

    @Test
    public void testRemoveRootWithRight() {
        AvlTree<Integer> tree = new AvlTree<>(node(3, 2, 1, null, node(5, 1, 0)), 2);
        tree.remove(5);
        assertEquals("[3]", tree.toString());
    }

    @Test
    public void testRemoveLeft() {
        AvlTree<Integer> tree = new AvlTree<>(node(5, 2, -1, node(3, 1, 0), null), 2);
        tree.remove(3);
        assertEquals("[5]", tree.toString());
    }

    @Test
    public void testRemoveRight() {
        AvlTree<Integer> tree = new AvlTree<>(node(3, 2, 1, null, node(5, 1, 0)), 2);
        tree.remove(5);
        assertEquals("[3]", tree.toString());
    }

    @Test
    public void testRemoveWithLeftAndRight() {
        AvlTree<Integer> tree = new AvlTree<>(node(5, 2, 0, node(3, 1, 0), node(7, 1, 0)), 3);
        tree.remove(5);
        assertEquals("[3, 7]", tree.toString());
    }

    @Test
    public void testRemoveWithNestedLeft() {
        AvlTree<Integer> tree = new AvlTree<>(node(50, 3, -1,
                node(30, 2, 1,
                        null, node(31, 1, 0)),
                node(70, 1, 0)), 4);
        tree.remove(50);
        assertEquals("[30, 31, 70]", tree.toString());
    }

    @Test
    public void testRemoveWithNestedRight() {
        AvlTree<Integer> tree = new AvlTree<>(node(50, 3, 1,
                node(30, 1, 0),
                node(70, 2, -1,
                        node(31, 1, 0), null)),
                4);
        tree.remove(50);
        assertEquals("[30, 31, 70]", tree.toString());
    }

    @Test
    public void testRemoveWithNestedLeftAndRight() {
        AvlTree<Integer> tree = new AvlTree<>(node(50, 4, -1,
                node(30, 3, 1,
                        node(25, 1, 0),
                        node(38, 2, -1,
                                node(35, 1, 0), null)),
                node(70, 2, 0,
                        node(65, 1, 0), node(75, 1, 0))), 4);
        tree.remove(50);
        assertEquals("[25, 30, 35, 38, 65, 70, 75]", tree.toString());
    }

    @Test
    public void testRemoveWithNestedBalancedLeftAndRight() {
        AvlTree<Integer> tree = new AvlTree<>(node(50, 5, -1,
                node(30, 4, -1,
                        node(25, 3, -1,
                                node(15, 2, -1,
                                        node(13, 1, 0), null),
                                node(28, 1, 0)),
                        node(38, 2, 1, null, node(40, 1, 0))),
                node(70, 3, -1,
                        node(65, 2, -1,
                                node(60, 1, 0), null),
                        node(75, 1, 0))), 4);
        tree.remove(50);
        assertEquals("[13, 15, 25, 28, 30, 38, 40, 60, 65, 70, 75]", tree.toString());
    }

    @Test
    public void testDeleteEvenRightOnlyRotate() {
        AvlTree<Integer> tree = new AvlTree<>(node(50, 4, -1,
                node(25, 3, 0,
                        node(15, 2, -1,
                                node(10, 1, 0), null),
                        node(30, 2, 1,
                                null, node(35, 1, 0))),
                node(55, 2, 1,
                        null, node(60, 1, 0))),
                8);
        tree.remove(60);
        assertEquals("[10, 15, 25, 30, 35, 50, 55]", tree.toString());
    }

    @Test
    public void testDeleteEvenLeftOnlyRotate() {
        AvlTree<Integer> tree = new AvlTree<>(node(50, 4, 1,
                node(45, 2, -1,
                        node(40, 1, 0), null),
                node(75, 3, 0,
                        node(65, 2, -1,
                                node(60, 1, 0), null),
                        node(85, 2, 1,
                                null, node(90, 1, 0)))),
                8);
        tree.remove(40);
        assertEquals("[45, 50, 60, 65, 75, 85, 90]", tree.toString());
    }

    @Test
    public void testContains() {
        AvlTree<Integer> tree = new AvlTree<>(node(5, 1, 0), 1);
        assertTrue(tree.contains(5));
        assertFalse(tree.contains(2));
    }

    @Test
    public void testForEach() {
        AvlTree<Integer> tree = new AvlTree<>(node(5, 2, 1,
                null, node(10, 1, 0)), 2);
        AtomicInteger sum = new AtomicInteger(0);
        tree.forEach(sum::addAndGet);
        assertEquals(15, sum.get());
    }

    private AvlTree.Node<Integer> node(int value, int height, int bal) {
        return node(value, height, bal, null, null);
    }

    private AvlTree.Node<Integer> node(int value, int height, int bal, AvlTree.Node<Integer> left, AvlTree.Node<Integer> right) {
        AvlTree.Node<Integer> node = new AvlTree.Node<>(value);
        node.left = left;
        node.right = right;
        node.height = height;
        node.bal = bal;
        return node;
    }
}
