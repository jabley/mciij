import java.util.Arrays;

public class Tree {
  Tree left;
  String key;
  Tree right;
  Object binding;

  Tree(Tree left, String key, Object binding, Tree right) {
    this.left = left;
    this.key = key;
    this.right = right;
    this.binding = binding;
  }

  static Tree insert(String key, Object binding, Tree t) {
    if (t == null) {
      return new Tree(null, key, binding, null);
    } else if (key.compareTo(t.key) < 0) {
      return new Tree(insert(key, binding, t.left), t.key, t.binding, t.right);
    } else if (key.compareTo(t.key) > 0) {
      return new Tree(t.left, t.key, t.binding, insert(key, binding, t.right));
    }  else {
      return new Tree(t.left, key, binding, t.right);
    }
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
    System.out.println(String.format("Height of tspipfbst: expected: 6; actual: %s", height(treeFromString("tspipfbst"))));
    System.out.println(String.format("Height of abcdefghi: expected: 9; actual: %s", height(treeFromString("abcdefghi"))));
  }

}
