class interp {

  static void print(int value) {
    System.out.println(value);
  }

  static void interp(Stm s) {
    interpStm(s, null);
  }

  static Table interpStm(Stm s, Table t) {
    return s.eval(t);
  }

  static IntAndTable interpExp(Exp e, Table t) {
    return e.eval(t);
  }

  public static void main(String args[]) throws java.io.IOException {
    System.out.println(prog.prog.maxargs());
    interp(prog.prog);
  }
}

class Table {
  String id;
  int value;
  Table tail;

  Table(String id, int value, Table tail) {
    this.id = id;
    this.value = value;
    this.tail = tail;
  }

  int lookup(String key) {
    if (this.id.equals(key)) {
      return this.value;
    }
    if (this.tail != null) {
      return this.tail.lookup(key);
    }
    throw new IllegalStateException("Variable " + key + " not found in table");
  }
}

class IntAndTable {
  int i;
  Table t;
  IntAndTable(int i, Table t) {
    this.i = i;
    this.t = t;
  }
}
