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
}
