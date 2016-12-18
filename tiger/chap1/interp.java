class interp {

  private static void print(int value) {
    System.out.println(value);
  }

  static void interp(Stm s) {
    interpStm(s, null);
  }

  static Table interpStm(Stm s, Table t) {
    if (s instanceof PrintStm) {
      PrintStm ps = (PrintStm) s;
      return interpExpList(ps.exps, t);
    } else if (s instanceof CompoundStm) {
      CompoundStm cs = (CompoundStm) s;
      Table first = interpStm(cs.stm1, t);
      return interpStm(cs.stm2, first);
    } else if (s instanceof AssignStm) {
      AssignStm as = (AssignStm) s;
      return new Table(as.id, interpExp(as.exp, t).i, t);
    }
    return null;
  }

  static Table interpExpList(ExpList exps, Table t) {
    if (exps instanceof LastExpList) {
      LastExpList lel = (LastExpList) exps;
      IntAndTable res = interpExp(lel.head, t);
      print(res.i);
      return res.t;
    } else {
      PairExpList pel = (PairExpList) exps;
      IntAndTable res = interpExp(pel.head, t);
      print(res.i);
      return interpExpList(pel.tail, res.t);
    }
  }

  static IntAndTable interpExp(Exp e, Table t) {
    if (e instanceof IdExp) {
      IdExp id = (IdExp) e;
      if (t == null) {
        throw new IllegalStateException("Variable " + id.id + " not found in table");
      }
      return new IntAndTable(t.lookup(id.id), t);
    } else if (e instanceof NumExp) {
      return new IntAndTable(((NumExp) e).num, t);
    } else if (e instanceof OpExp) {
      OpExp oe = (OpExp) e;
      int left = interpExp(oe.left, t).i;
      int right = interpExp(oe.right, t).i;
      switch (oe.oper) {
        case OpExp.Plus:
          return new IntAndTable(left + right, t);
        case OpExp.Minus:
          return new IntAndTable(left - right, t);
        case OpExp.Times:
          return new IntAndTable(left * right, t);
        case OpExp.Div:
          return new IntAndTable(left / right, t);
        default:
        throw new IllegalArgumentException("Unknown operation: " + oe.oper);
      }
    } else if (e instanceof EseqExp) {
      EseqExp se = (EseqExp) e;
      Table newTable = interpStm(se.stm, t);
      return interpExp(se.exp, newTable);
    }
    return null;
  }

  private static int numargs(ExpList exps) {
    if (exps instanceof LastExpList) {
      return 1;
    }
    return 1 + numargs(((PairExpList) exps).tail);
  }

  private static int maxargs(ExpList exps) {
    if (exps instanceof LastExpList) {
      return maxargs(((LastExpList) exps).head);
    }
    PairExpList pairExpList = (PairExpList) exps;
    return Math.max(maxargs(pairExpList.head), maxargs(pairExpList.tail));
  }

  private static int maxargs(Exp e) {
    if (e instanceof EseqExp) {
      return maxargs(((EseqExp) e).stm);
    } else if (e instanceof OpExp) {
      OpExp opExp = (OpExp) e;
      return Math.max(maxargs(opExp.left), maxargs(opExp.right));
    }
    return 0;
  }

  static int maxargs(Stm s) {
    if (s instanceof PrintStm) {
      PrintStm ps = (PrintStm) s;
      return Math.max(numargs(ps.exps), maxargs(ps.exps));
    } else if (s instanceof CompoundStm) {
      CompoundStm cs = (CompoundStm) s;
      return Math.max(maxargs(cs.stm1), maxargs(cs.stm2));
    } else if (s instanceof AssignStm) {
      return maxargs(((AssignStm) s).exp);
    }
    return 0;
  }

  public static void main(String args[]) throws java.io.IOException {
    System.out.println(maxargs(prog.prog));
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
