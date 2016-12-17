package Tree;
abstract public class Stm {
	abstract public ExpList kids();
	abstract public Stm build(ExpList kids);
}

abstract public class Exp {
	abstract public ExpList kids();
	abstract public Exp build(ExpList kids);
}
