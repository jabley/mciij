.PHONY: test clean

test: _build
	java -cp target/classes interp

clean:
	rm -rf target

_build:
	mkdir -p target/classes
	javac -d target/classes src/main/java/*.java
