abstract class Stm {
  abstract Table eval(Table t);
  abstract int maxargs();
}

class CompoundStm extends Stm {
   Stm stm1;
   Stm stm2;
   CompoundStm(Stm s1, Stm s2) {
     stm1=s1;
     stm2=s2;
   }
   Table eval(Table t) {
     Table first = stm1.eval(t);
     return stm2.eval(first);
   }
   int maxargs() {
     return Math.max(stm1.maxargs(), stm2.maxargs());
   }
}

class AssignStm extends Stm {
   String id;
   Exp exp;
   AssignStm(String i, Exp e) {
     id=i;
     exp=e;
   }
   Table eval(Table t) {
     return new Table(id, exp.eval( t).i, t);
   }
   int maxargs() {
     return exp.maxargs();
   }
}

class PrintStm extends Stm {
   ExpList exps;
   PrintStm(ExpList e) {
     exps=e;
   }
   Table eval(Table t) {
     return exps.printAndEval(t);
   }
   int maxargs() {
     return Math.max(exps.numargs(), exps.maxargs());
   }
}

abstract class Exp {
  abstract IntAndTable eval(Table t);
  int maxargs() {
    return 0;
  }
}

class IdExp extends Exp {
   String id;
   IdExp(String i) {
     id=i;
   }
   IntAndTable eval(Table t) {
     if (t == null) {
       throw new IllegalStateException("Variable " + id + " not found in table");
     }
     return new IntAndTable(t.lookup(id), t);
   }
}

class NumExp extends Exp {
   int num;
   NumExp(int n) {
     num=n;
   }
   IntAndTable eval(Table t) {
     return new IntAndTable(num, t);
   }
}

class OpExp extends Exp {
   Exp left;
   Exp right;
   int oper;
   final static int Plus=1,Minus=2,Times=3,Div=4;
   OpExp(Exp l, int o, Exp r) {
     left=l;
     oper=o;
     right=r;
   }
   IntAndTable eval(Table t) {
     int leftVal = left.eval(t).i;
     int rightVal = right.eval(t).i;
     switch (oper) {
       case OpExp.Plus:
         return new IntAndTable(leftVal + rightVal, t);
       case OpExp.Minus:
         return new IntAndTable(leftVal - rightVal, t);
       case OpExp.Times:
         return new IntAndTable(leftVal * rightVal, t);
       case OpExp.Div:
         return new IntAndTable(leftVal / rightVal, t);
       default:
       throw new IllegalArgumentException("Unknown operation: " + oper);
     }
   }
   @Override
   int maxargs() {
     return Math.max(left.maxargs(), right.maxargs());
   }
}

class EseqExp extends Exp {
   Stm stm;
   Exp exp;
   EseqExp(Stm s, Exp e) {
     stm=s;
     exp=e;
   }
   IntAndTable eval(Table t) {
     Table newTable = stm.eval(t);
     return exp.eval(newTable);
   }
   @Override
   int maxargs() {
     return stm.maxargs();
   }
}

abstract class ExpList {
  abstract Table printAndEval(Table t);
  abstract int numargs();
  abstract int maxargs();

  void print(int value) {
    System.out.println(value);
  }
}

class PairExpList extends ExpList {
   Exp head;
   ExpList tail;
   public PairExpList(Exp h, ExpList t) {
     head=h;
     tail=t;
   }
   Table printAndEval(Table t) {
     IntAndTable res = head.eval(t);
     print(res.i);
     return tail.printAndEval(res.t);
   }
   int numargs() {
     return 1 + tail.numargs();
   }
   int maxargs() {
     return Math.max(head.maxargs(), tail.maxargs());
   }
}

class LastExpList extends ExpList {
   Exp head;
   public LastExpList(Exp h) {
     head=h;
   }
   Table printAndEval(Table t) {
     IntAndTable res = head.eval(t);
     print(res.i);
     return res.t;
   }
   int numargs() {
     return 1;
   }
   int maxargs() {
     return head.maxargs();
   }
}
