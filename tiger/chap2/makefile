JFLAGS=-cp ../java-cup/java-cup-11b-runtime.jar:. -g

Parse/Main.class: Parse/*.java Parse/Yylex.java ErrorMsg/ErrorMsg.class
	javac ${JFLAGS} Parse/*.java

Parse/Yylex.java: Parse/Tiger.lex
	cd Parse; java -jar ../../JLex/build/libs/JLex.jar Tiger.lex; mv Tiger.lex.java Yylex.java

ErrorMsg/ErrorMsg.class:  ErrorMsg/*.java
	javac ${JFLAGS} ErrorMsg/*.java

clean:
	rm Parse/*.class ErrorMsg/*.class Parse/Yylex.java
