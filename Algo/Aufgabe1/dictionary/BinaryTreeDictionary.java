// O. Bittel
// 22.09.2022
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/**
 * Implementation of the Dictionary interface as AVL tree.
 * <p>
 * The entries are ordered using their natural ordering on the keys, 
 * or by a Comparator provided at set creation time, depending on which constructor is used. 
 * <p>
 * An iterator for this dictionary is implemented by using the parent node reference.
 * 
 * @param <K> Key.
 * @param <V> Value.
 */
public class BinaryTreeDictionary<K extends Comparable<K>, V> implements Dictionary<K, V> {
    
    static private class Node<K, V> {
        K key;
        V value;
        int height;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        Node(K k, V v) {
            key = k;
            value = v;
            height = 0;
            left = null;
            right = null;
            parent = null;
        }
    }
    
    private Node<K, V> root = null;
    private int size = 0;
    private V oldValue;
    
    // ...

	/**
	 * Pretty prints the tree
	 */
	public void prettyPrint() {
        printR(0, root);
    }

    private void printR(int level, Node<K, V> p) {
        printLevel(level);
        if (p == null) {
            System.out.println("#");
        } else {
            System.out.println(p.key + " " + p.value + "^" + ((p.parent == null) ? "null" : p.parent.key));
            if (p.left != null || p.right != null) {
                printR(level + 1, p.left);
                printR(level + 1, p.right);
            }
        }
    }

    private static void printLevel(int level) {
        if (level == 0) {
            return;
        }
        for (int i = 0; i < level - 1; i++) {
            System.out.print("   ");
        }
        System.out.print("|__");
    }

    @Override
    public V insert(K key, V value) {
        root = insertR(key, value, root);
        size++;
        if (root != null)
            root.parent = null;
        return oldValue;
    }

    private Node<K,V> insertR(K key, V value, Node<K,V> p) {
        if (p == null) {
            p = new Node(key, value);
            oldValue = null;
        } else if (key.compareTo(p.key) < 0) {
            p.left = insertR(key, value, p.left);
            if (p.left != null)
                p.left.parent = p;
        } else if (key.compareTo(p.key) > 0) {
            p.right = insertR(key, value, p.right);
            if (p.right != null)
                p.right.parent = p;
        } else { 
            oldValue = p.value;
            p.value = value;
        }
        p = balance(p);
        return p;
    }

    private Node<K,V> balance(Node<K,V> p) {
        if (p == null)
            return null;
        if (getBalance(p) == 2) {
            if (getBalance(p.right) < 0)
                p.right = rotateRight(p.right);
            p = rotateLeft(p);
        } else if (getBalance(p) == -2) {
            if (getBalance(p.left) > 0)
                p.left = rotateLeft(p.left);
            p = rotateRight(p);
        }
        p.height = Math.max(getHigh(p.left), getHigh(p.right)) + 1;
        return p;
    }

    private Node<K,V> rotateRight(Node<K,V> p) {
        assert p != null && p.left != null;
        Node<K,V> q = p.left;
        p.left = q.right;
        if (p.left != null)
            p.left.parent = p;
        q.right = p;
        q.parent = p.parent;
        p.parent = q;
        p.height = Math.max(getHigh(p.left), getHigh(p.right)) + 1;
        q.height = Math.max(getHigh(q.left), getHigh(q.right)) + 1;
        return q;
    }

    private Node<K,V> rotateLeft(Node<K,V> p) {
        assert p != null && p.right != null;
        Node<K,V> q = p.right;
        p.right = q.left;
        if (p.right != null)
            p.right.parent = p;
        q.left = p;
        q.parent = p.parent;
        p.parent = q;
        p.height = Math.max(getHigh(p.left), getHigh(p.right)) + 1;
        q.height = Math.max(getHigh(q.left), getHigh(q.right)) + 1;
        return q;
    }

    // private Node<K,V> rotateLeftRight(Node<K,V> p) {
    //     assert p != null && p.left != null && p.left.right != null;
    //     p.left = rotateLeft(p.left);
    //     return rotateRight(p);
    // }

    // private Node<K,V> rotateRightLeft(Node<K,V> p) {
    //     assert p != null && p.right != null && p.right.left != null;
    //     p.right = rotateRight(p.right);
    //     return rotateLeft(p);
    // }

    @Override
    public V search(K key) {
        return searchR(key, root);
    }

    private V searchR(K key, Node<K,V> p) {
        if (p == null)
            return null;
        else if (key.compareTo(p.key) < 0)
            return searchR(key, p.left);
        else if (key.compareTo(p.key) > 0)
            return searchR(key, p.right);
        else
            return p.value;
        }

    @Override
    public V remove(K key) {
        root = removeR(key, root);
        size--;
        return oldValue;
    }

    private Node<K,V> removeR(K key, Node<K,V> p) {
        if (p == null) { 
            oldValue = null; 
        }
        else if(key.compareTo(p.key) < 0)
            p.left = removeR(key, p.left);
        else if (key.compareTo(p.key) > 0)
            p.right = removeR(key, p.right);
        else if (p.left == null || p.right == null) {
        // p muss gelöscht werden
        // und hat ein oder kein Kind:
            oldValue = p.value;
            p = (p.left != null) ? p.left : p.right;
        } else {
        // p muss gelöscht werden und hat zwei Kinder:
            MinEntry<K,V> min = new MinEntry<K,V>();
            p.right = getRemMinR(p.right, min);
            oldValue = p.value;
            p.key = min.key;
            p.value = min.value;
        }
        return p;
    }

    private Node<K,V> getRemMinR(Node<K,V> p, MinEntry<K,V> min) {
        assert p != null;
        if (p.left == null) {
            min.key = p.key;
            min.value = p.value;
            p = p.right;
        }
        else
            p.left = getRemMinR(p.left, min);
        return p;
    }

    private static class MinEntry<K, V> {
        private K key;
        private V value;
    }

    private int getHigh(Node<K,V> p) {
        if (p == null)
            return -1;
        else
            return p.height;
    }

    private int getBalance(Node<K,V> p) {
        if (p == null)
            return 0;
        else
            return getHigh(p.right) - getHigh(p.left);
    }

    public void printInOrder() {
        Node<K,V> p = null;
        if (root != null)
            p = leftMostDescendant(root);
        while (p != null) {
            System.out.print(p.key + ", ");
            if (p.right != null)
                p = leftMostDescendant(p.right);
            else
                p = parentOfLeftMostAncestor(p);
        }
    }

    private Node<K,V> leftMostDescendant(Node<K,V> p) {
        assert p != null;
        while (p.left != null)
            p = p.left;
        return p;
    }

    private Node<K,V> parentOfLeftMostAncestor(Node<K,V> p) {
        assert p != null;
        while (p.parent != null && p.parent.right == p)
            p = p.parent;
        return p.parent; // kann auch null sein
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Dictionary.Entry<K, V>> iterator() {
        return new Iterator<Dictionary.Entry<K,V>>() {
            Node<K,V> p = null;
            Node<K,V> q = null;
            {
                if (root != null)
                    p = leftMostDescendant(root);
            }
            @Override
            public boolean hasNext() {
                return p != null;
            }

            @Override
            public Dictionary.Entry<K, V> next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                q = p;
                if (p.right != null)
                    p = leftMostDescendant(p.right);
                else
                    p = parentOfLeftMostAncestor(p);
                return new Dictionary.Entry<>(q.key, q.value);
            }
        };
    }

}
