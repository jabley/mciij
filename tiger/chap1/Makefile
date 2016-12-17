.PHONY: test clean

test: _build
	java -cp target/classes interp

clean:
	rm -rf target

_build:
	mkdir -p target/classes
	javac -d target/classes interp.java prog.java slp.java
