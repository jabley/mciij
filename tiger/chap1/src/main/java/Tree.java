import java.util.Arrays;

/**
 * Persistent red-black tree implementation, originally for use as a symbol table.
 *
 * @param <K>
 * @param <V>
 */
public class Tree<K extends Comparable<K>, V> {
    enum Color {RED, BLACK}

    private final Color color;
    private final K key;
    private final V binding;
    private final Tree<K, V> left;
    private final Tree<K, V> right;

    @SuppressWarnings("unchecked")
    public Tree(K key, V binding) {
        this(Color.BLACK, key, binding, EMPTY_TREE, EMPTY_TREE);
    }

    protected Tree(Color color, K key, V binding, Tree<K, V> left, Tree<K ,V> right) {
        this.color = color;
        this.key = key;
        this.binding = binding;
        this.left = left;
        this.right = right;
    }

    private static final Tree EMPTY_TREE = new Tree<String, Object>(Color.RED, null, null, null, null) {
        @Override
        public int height() {
            return 0;
        }

        @Override
        public boolean member(String key) {
            return false;
        }

        @Override
        public Object lookup(String key) {
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Tree insertInternal(String key, Object binding) {
            return newRedTree(key, binding, this, this);
        }
    };

    /**
     * Adds a new entry to this table, and returns a new Tree.
     * @param key a non-null key for this entry
     * @param binding a value for this entry
     * @return a new non-null Tree<K, V>
     */
    public Tree<K, V> insert(K key, V binding) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        return makeBlack(insertInternal(key, binding));
    }

    protected Tree<K, V> insertInternal(K key, V binding) {
        int comparison = key.compareTo(this.key);
        if (comparison < 0) {
            return lbalance(isBlackTree(this), this.key, this.binding, this.left.insertInternal(key, binding), this.right);
        } else if (comparison > 0) {
            return rbalance(isBlackTree(this), this.key, this.binding, this.left, this.right.insertInternal(key, binding));
        } else {
            return makeTree(isBlackTree(this), key, binding, this.left, this.right);
        }
    }

    protected Tree<K, V> newRedTree(K key, V binding, Tree<K, V> left, Tree<K, V> right) {
        return new Tree<K, V>(Color.RED, key, binding, left, right);
    }

    private Tree<K, V> newBlackTree(K key, V binding, Tree<K, V> left, Tree<K, V> right) {
        return new Tree<K ,V>(Color.BLACK, key, binding, left, right);
    }

    private Tree<K ,V> lbalance(boolean isBlack, K key, V binding, Tree<K, V> l, Tree<K, V> r) {
        if (isRedTree(l) && isRedTree(l.left)) {
            return newRedTree(
                    l.key, l.binding,
                    newBlackTree(l.left.key, l.left.binding, l.left.left, l.left.right),
                    newBlackTree(key, binding, l.right, r)
            );
        } else if (isRedTree(l) && isRedTree(l.right)) {
            return newRedTree(
                    l.right.key, l.right.binding,
                    newBlackTree(l.key, l.binding, l.left, l.right.left),
                    newBlackTree(key, binding, l.right.right, r)
            );
        } else {
            return makeTree(isBlack, key, binding, l, r);
        }
    }

    private Tree<K ,V> makeTree(boolean isBlack, K key, V binding, Tree<K, V> left, Tree<K, V> right) {
        return isBlack ? newBlackTree(key, binding, left, right) : newRedTree(key, binding, left, right);
    }

    private Tree<K ,V> rbalance(boolean isBlack, K key, V binding, Tree<K, V> l, Tree<K, V> r) {
        if (isRedTree(r) && isRedTree(r.left)) {
            return newRedTree(
                    r.left.key, r.left.binding,
                    newBlackTree(key, binding, l, r.left.left),
                    newBlackTree(r.key, r.binding, r.left.right, r.right)
            );
        } else if (isRedTree(r) && isRedTree(r.right)) {
            return newRedTree(
                    r.key, r.binding,
                    newBlackTree(key, binding, l, r.left),
                    newBlackTree(r.right.key, r.right.binding, r.right.left, r.right.right)
            );
        } else {
            return makeTree(isBlack, key, binding, l, r);
        }
    }

    private boolean isRedTree(Tree<K, V> t) {
        return t != EMPTY_TREE && t.color == Color.RED;
    }

    private boolean isBlackTree(Tree<K, V> t) {
        return t != EMPTY_TREE && t.color == Color.BLACK;
    }

    private Tree<K, V> makeBlack(Tree<K, V> t) {
        return isBlackTree(t) ? t : newBlackTree(t.key, t.binding, t.left, t.right);
    }

    /**
     * Returns the value stored for the specified key, or null if there isn't one. If you wish to check whether the key
     * exists within the Tree, use the {@member(K} method.
     *
     * @param key a non-null key
     * @return the stored value (which may be null), or null
     */
    public V lookup(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        int comparison = key.compareTo(this.key);
        if (comparison < 0) {
            return this.left.lookup(key);
        } else if (comparison > 0) {
            return this.right.lookup(key);
        }
        return this.binding;
    }

    /**
     * Returns true if this key exists within the Tree, otherwise false.
     *
     * @param key a non-null key
     * @return a boolean
     */
    public boolean member(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        int comparison = key.compareTo(this.key);
        if (comparison < 0) {
            return this.left.member(key);
        } else if (comparison > 0) {
            return this.right.member(key);
        }
        return true;
    }

    private static Tree<String, Integer> treeFromString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("input == null");
        }
        return treeFromChars(input.toCharArray());
    }

    @SuppressWarnings("unchecked")
    private static Tree<String, Integer> treeFromChars(char[] chars) {
        if (chars.length == 0) {
            return (Tree<String, Integer>) EMPTY_TREE;
        }
        return treeFromChars((Tree<String, Integer>) EMPTY_TREE, 0, chars);
    }

    private static Tree<String, Integer> treeFromChars(Tree<String, Integer> t, int pos, char[] chars) {
        String head = String.valueOf(chars[0]);
        Tree<String, Integer> newTree = t.insert(head, pos);
        if (chars.length == 1) {
            return newTree;
        }
        return treeFromChars(newTree, pos + 1, Arrays.copyOfRange(chars, 1, chars.length));
    }

    public int height() {
        int left = this.left.height();
        int right = this.right.height();
        return 1 + Math.max(left, right);
    }

    public static void main(String[] args) {
        Tree<String, Integer> tree1 = new Tree<>("a", 1);
        Tree<String, Integer> tree2 = tree1.insert("b", 2);
        System.out.println(String.format("a is in Tree1? expected:  true, actual: %5b", tree1.member("a")));
        System.out.println(String.format("c is in Tree2? expected: false, actual: %5b", tree2.member("c")));
        System.out.println(String.format("b is in Tree2? expected:  true, actual: %5b", tree2.member("b")));
        System.out.println(String.format("a is in Tree2? expected:  true, actual: %5b", tree2.member("a")));
        System.out.println(String.format("b is in Tree1? expected: false, actual: %5b", tree1.member("b")));
        System.out.println();
        System.out.println(String.format("Value of c in Tree2? expected: null, actual: %4s", tree2.lookup("c")));
        System.out.println(String.format("Value of b in Tree2? expected:    2, actual: %4s", tree2.lookup("b")));
        System.out.println(String.format("Value of a in Tree2? expected:    1, actual: %4s", tree2.lookup("a")));
        System.out.println(String.format("Value of b in Tree1? expected: null, actual: %4s", tree1.lookup("b")));
        System.out.println();
        System.out.println(String.format("Height of tspipfbst: expected: 4; actual: %s", treeFromString("tspipfbst").height()));
        System.out.println(String.format("Height of abcdefghi: expected: 4; actual: %s", treeFromString("abcdefghi").height()));
    }

}
