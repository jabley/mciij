import java.util.Arrays;

public class Tree {
    enum Color {RED, BLACK}

    private Color color;
    Tree left;
    String key;
    Tree right;
    Object binding;

    public Tree(String key, Object binding) {
        this(Color.BLACK, key, binding, EMPTY_TREE, EMPTY_TREE);
    }

    protected Tree(Color color, String key, Object binding, Tree left, Tree right) {
        this.color = color;
        this.key = key;
        this.binding = binding;
        this.left = left;
        this.right = right;
    }

    private static final Tree EMPTY_TREE = new Tree(Color.RED, null, null, null, null) {
        public int height() {
            return 0;
        }

        public boolean member(String key) {
            return false;
        }

        public Object lookup(String key) {
            return null;
        }

        protected Tree insert1(String key, Object binding) {
            return newRedTree(key, binding, this, this);
        }
    };

    public Tree insert(String key, Object binding) {
        if (key == null) {
            throw new IllegalArgumentException("key == null");
        }
        return makeBlack(insert1(key, binding));
    }

    protected Tree insert1(String key, Object binding) {
        int comparison = key.compareTo(this.key);
        if (comparison < 0) {
            return lbalance(isBlackTree(this), this.key, this.binding, this.left.insert1(key, binding), this.right);
        } else if (comparison > 0) {
            return rbalance(isBlackTree(this), this.key, this.binding, this.left, this.right.insert1(key, binding));
        } else {
            return makeTree(isBlackTree(this), key, binding, this.left, this.right);
        }
    }

    protected Tree newRedTree(String key, Object binding, Tree left, Tree right) {
        return new Tree(Color.RED, key, binding, left, right);
    }

    private Tree newBlackTree(String key, Object binding, Tree left, Tree right) {
        return new Tree(Color.BLACK, key, binding, left, right);
    }

    private Tree lbalance(boolean isBlack, String key, Object binding, Tree l, Tree r) {
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

    private Tree makeTree(boolean isBlack, String key, Object binding, Tree left, Tree right) {
        return isBlack ? newBlackTree(key, binding, left, right) : newRedTree(key, binding, left, right);
    }

    private Tree rbalance(boolean isBlack, String key, Object binding, Tree l, Tree r) {
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

    private boolean isRedTree(Tree t) {
        return t != EMPTY_TREE && t.color == Color.RED;
    }

    private boolean isBlackTree(Tree t) {
        return t != EMPTY_TREE && t.color == Color.BLACK;
    }

    private Tree makeBlack(Tree t) {
        return isBlackTree(t) ? t : newBlackTree(t.key, t.binding, t.left, t.right);
    }

    public Object lookup(String key) {
        int comparison = key.compareTo(this.key);
        if (comparison < 0) {
            return this.left.lookup(key);
        } else if (comparison > 0) {
            return this.right.lookup(key);
        }
        return this.binding;
    }

    public boolean member(String key) {
        int comparison = key.compareTo(this.key);
        if (comparison < 0) {
            return this.left.member(key);
        } else if (comparison > 0) {
            return this.right.member(key);
        }
        return true;
    }

    static Tree treeFromString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("input == null");
        }
        return treeFromChars(input.toCharArray());
    }

    static Tree treeFromChars(char[] chars) {
        if (chars.length == 0) {
            return EMPTY_TREE;
        }
        return treeFromChars(EMPTY_TREE, 0, chars);
    }

    static Tree treeFromChars(Tree t, int pos, char[] chars) {
        String head = String.valueOf(chars[0]);
        Tree newTree = t.insert(head, pos);
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
        Tree tree1 = new Tree("a", 1);
        Tree tree2 = tree1.insert("b", 2);
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
