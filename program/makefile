SRC= $(wildcard **/*.java) $(wildcard *.java)
OBJ= $(SRC:.java=.class)
EXE= ../forklift.jar
MAIN= Main


.PHONY: clean


all: $(EXE)
	@java -jar $(EXE) || true


$(EXE): $(OBJ)
	@rm -rf $(EXE)
	@jar -cfe $(EXE) $(MAIN) $(OBJ)


%.class: %.java
	javac $^


clean:
	rm -rf *.class **/*.class

