.PHONY: test clean

test: _build
	echo "Interpreting"
	java -cp target/classes interp
	echo "Exercise 1.1:"
	java -cp target/classes Tree

clean:
	rm -rf target

_build:
	mkdir -p target/classes
	javac -d target/classes src/main/java/*.java
