public class Tree {
  Tree left;
  String key;
  Tree right;

  Tree(Tree left, String key, Tree right) {
    this.left = left;
    this.key = key;
    this.right = right;
  }

  static Tree insert(String key, Tree t) {
    if (t == null) {
      return new Tree(null, key, null);
    } else if (key.compareTo(t.key) < 0) {
      return new Tree(insert(key, t.left), t.key, t.right);
    } else if (key.compareTo(t.key) > 0) {
      return new Tree(t.left, t.key, insert(key, t.right));
    }  else {
      return new Tree(t.left, key, t.right);
    }
  }

  static boolean member(String key, Tree t) {
    if (t == null) {
      return false;
    }
    if (key.equals(t.key)) {
      return true;
    }
    if (key.compareTo(t.key) < 0) {
      return member(key, t.left);
    } else if (key.compareTo(t.key) > 0) {
      return member(key, t.right);
    }
    throw new IllegalStateException("Not sure how to compare the key");
  }

  public static void main(String[] args) {
    Tree tree1 = insert("a", null);
    Tree tree2 = insert("b", tree1);
    System.out.println(member("c", tree2));
    System.out.println(member("b", tree2));
    System.out.println(member("b", tree1));
  }

}
