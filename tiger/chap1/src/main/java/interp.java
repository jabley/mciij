import java.io.PrintWriter;

class interp {

  static void interp(Stm s, PrintWriter pw) {
    s.eval(new EmptyTable(pw));
  }

  public static void main(String[] args) {
    interp(prog.prog, new PrintWriter(System.out));
  }
}

interface Table {
  int lookup(String key);
  void print(int value);
}

class EmptyTable implements Table {
  private final PrintWriter writer;

  EmptyTable(PrintWriter writer) {
    this.writer = writer;
  }

  public int lookup(String key) {
    throw new IllegalStateException("Variable " + key + " not found in table");
  }

  public void print(int value) {
    this.writer.println(value);
  }
}

class SlotTable implements Table {
  String id;
  int value;
  Table tail;

  SlotTable(String id, int value, Table tail) {
    this.id = id;
    this.value = value;
    this.tail = tail;
  }

  public int lookup(String key) {
    if (this.id.equals(key)) {
      return this.value;
    }
    if (this.tail != null) {
      return this.tail.lookup(key);
    }
    throw new IllegalStateException("Variable " + key + " not found in table");
  }

  public void print(int value) {
    if (this.tail == null) {
      throw new IllegalStateException("SlotTable cannot print");
    }
    this.tail.print(value);
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
