class interp {
  static void interp(Stm s) {

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
