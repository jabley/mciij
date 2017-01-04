import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TreeTest {

    @Test
    public void membership() {
      Tree<String, Integer> tree1 = new Tree<>("a", 1);
      Tree<String, Integer> tree2 = tree1.insert("b", 2);
      Tree<String, Integer> tree3 = tree2.insert("c", 3);
      assertThat(tree1.member("a"), is(true));
      assertThat(tree1.member("c"), is(false));
      assertThat(tree2.member("b"), is(true));
      assertThat(tree2.member("a"), is(true));
      assertThat(tree1.member("b"), is(false));
      assertThat(tree3.member("a"), is(true));
    }

    @Test
    public void lookup() {
      Tree<String, Integer> tree1 = new Tree<>("a", 1);
      Tree<String, Integer> tree2 = tree1.insert("b", 2);
      Tree<String, Integer> tree3 = tree2.insert("c", 3);
      assertThat(tree2.lookup("c"), is(nullValue()));
      assertThat(tree2.lookup("b"), is(equalTo(2)));
      assertThat(tree2.lookup("a"), is(equalTo(1)));
      assertThat(tree1.lookup("b"), is(nullValue()));
      assertThat(tree3.lookup("c"), is(equalTo(3)));
      assertThat(tree3.lookup("a"), is(equalTo(1)));
    }

    @Test
    public void isBalanced() {
        assertThat(Tree.treeFromString("tspipfbst").height(), is(equalTo(4)));
        assertThat(Tree.treeFromString("abcdefghi").height(), is(equalTo(4)));
        assertThat(Tree.treeFromString("ihgfedcba").height(), is(equalTo(4)));
        assertThat(Tree.treeFromString("ihgkj").height(), is(equalTo(3)));
        assertThat(Tree.treeFromString("egiac").height(), is(equalTo(3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowNull() {
        Tree.treeFromString(null);
    }

    @Test
    public void allowsEmptyString() {
      Tree.treeFromString("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowNullKeysToBeCreateed() {
      new Tree(null, "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowNullKeysToBeInserted() {
      new Tree("a", "1").insert(null, "2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowNullKeysToBeLookedUp() {
      new Tree("a", "1").lookup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotAllowNullKeysToBeCheckedForMembership() {
      new Tree("a", "1").member(null);
    }

}
