import java.util.Arrays;

public class Tree {
  enum Color { RED, BLACK }
  private Color color;
  Tree left;
  String key;
  Tree right;
  Object binding;

  Tree(Color color, String key, Object binding, Tree left, Tree right) {
    this.color = color;
    this.key = key;
    this.binding = binding;
    this.left = left;
    this.right = right;
  }

  static Tree insert(String key, Object binding, Tree t) {
    return makeBlack(insert1(key, binding, t));
  }

  private static Tree insert1(String key, Object binding, Tree t) {
    if (t == null) {
      return newRedTree(key, binding, null, null);
    }

    int comparison = key.compareTo(t.key);
    if (comparison < 0) {
      return lbalance(isBlackTree(t), t.key, t.binding, insert1(key, binding, t.left), t.right);
    } else if (comparison > 0) {
      return rbalance(isBlackTree(t), t.key, t.binding, t.left, insert1(key, binding, t.right));
    } else {
      return makeTree(isBlackTree(t), key, binding, t.left, t.right);
    }
  }

  private static Tree newRedTree(String key, Object binding, Tree left, Tree right) {
    return new Tree(Color.RED, key, binding, left, right);
  }

  private static Tree newBlackTree(String key, Object binding, Tree left, Tree right) {
    return new Tree(Color.BLACK, key, binding, left, right);
  }

  private static Tree lbalance(boolean isBlack, String key, Object binding, Tree left, Tree right) {
    if (isRedTree(left) && isRedTree(left.left)) {
      return newRedTree(
        left.key, left.binding,
        newBlackTree(left.left.key, left.left.binding, left.left.left, left.left.right),
        newBlackTree(key, binding, left.right, right)
      );
    } else if (isRedTree(left) && isRedTree(left.right)) {
      return newRedTree(
        left.right.key, left.right.binding,
        newBlackTree(left.key, left.binding, left.left, left.right.left),
        newBlackTree( key, binding, left.right.right, right)
      );
    } else {
      return makeTree(isBlack, key, binding, left, right);
    }
  }

  private static Tree makeTree(boolean isBlack, String key, Object binding, Tree left, Tree right) {
    return isBlack ? newBlackTree(key, binding, left, right) : newRedTree(key, binding, left, right);
  }

  private static Tree rbalance(boolean isBlack, String key, Object binding,  Tree left, Tree right) {
    if (isRedTree(right) && isRedTree(right.left)) {
      return newRedTree(
        right.left.key, right.left.binding,
        newBlackTree(key, binding, left, right.left.left),
        newBlackTree(right.key, right.binding, right.left.right, right.right)
      );
    } else if (isRedTree(right) && isRedTree(right.right)) {
      return newRedTree(
        right.key, right.binding,
        newBlackTree(key, binding, left, right.left),
        newBlackTree(right.right.key, right.right.binding, right.right.left, right.right.right)
      );
    } else {
      return makeTree(isBlack, key, binding, left, right);
    }
  }

  private static boolean isRedTree(Tree t) {
    return t != null && t.color == Color.RED;
  }

  private static boolean isBlackTree(Tree t) {
    return t != null && t.color == Color.BLACK;
  }

  static Tree makeBlack(Tree t) {
    return isBlackTree(t) ? t : newBlackTree(t.key, t.binding, t.left, t.right);
  }

  static Object lookup(String key, Tree t) {
    if (t == null) {
      return null;
    }
    int comparison = key.compareTo(t.key);
    if (comparison < 0) {
      return lookup(key, t.left);
    } else if (comparison > 0) {
      return lookup(key, t.right);
    } else {
      return t.binding;
    }
  }

  static boolean member(String key, Tree t) {
    if (t == null) {
      return false;
    }
    int comparison = key.compareTo(t.key);
    if (comparison < 0) {
      return member(key, t.left);
    } else if (comparison > 0) {
      return member(key, t.right);
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
      return insert("", 0, null);
    }
    return treeFromChars(null, 0, chars);
  }

  static Tree treeFromChars(Tree t, int pos, char[] chars) {
    String head = String.valueOf(chars[0]);
    Tree newTree = insert(head, pos, t);
    if (chars.length == 1) {
      return newTree;
    }
    return treeFromChars(newTree, pos + 1, Arrays.copyOfRange(chars, 1, chars.length));
  }

  static int height(Tree t) {
    if (t == null) {
      return 0;
    }
    int left = height(t.left);
    int right = height(t.right);
    return 1 + Math.max(left, right);
  }

  public static void main(String[] args) {
    Tree tree1 = insert("a", 1, null);
    Tree tree2 = insert("b", 2, tree1);
    System.out.println(String.format("c is in Tree2? expected: false, actual: %5b", member("c", tree2)));
    System.out.println(String.format("b is in Tree2? expected:  true, actual: %5b", member("b", tree2)));
    System.out.println(String.format("a is in Tree2? expected:  true, actual: %5b", member("a", tree2)));
    System.out.println(String.format("b is in Tree1? expected: false, actual: %5b", member("b", tree1)));
    System.out.println();
    System.out.println(String.format("Value of c in Tree2? expected: null, actual: %4s", lookup("c", tree2)));
    System.out.println(String.format("Value of b in Tree2? expected:    2, actual: %4s", lookup("b", tree2)));
    System.out.println(String.format("Value of a in Tree2? expected:    1, actual: %4s", lookup("a", tree2)));
    System.out.println(String.format("Value of b in Tree1? expected: null, actual: %4s", lookup("b", tree1)));
    System.out.println();
    System.out.println(String.format("Height of tspipfbst: expected: 4; actual: %s", height(treeFromString("tspipfbst"))));
    System.out.println(String.format("Height of abcdefghi: expected: 4; actual: %s", height(treeFromString("abcdefghi"))));
  }

}
