# Builds ../forklift.jar from the Java sources of this directory.
#
# All sources are compiled together, in one javac call, into BUILD: a change
# in one class therefore always recompiles the classes that depend on it, and
# every generated class file (including nested classes) goes into the jar.

SRC     := $(shell find . -name '*.java' -not -path './build/*')
BUILD   := build
EXE     := ../forklift.jar
MAIN    := Main
# records, sealed classes and pattern matching need Java 17
RELEASE := 17


.PHONY: all clean


all: $(EXE)
	@java -jar $(EXE) || true


$(EXE): $(SRC)
	@rm -rf $(BUILD)
	javac --release $(RELEASE) -d $(BUILD) $(SRC)
	@rm -f $(EXE)
	jar --create --file $(EXE) --main-class $(MAIN) -C $(BUILD) .


# also removes the class files left in the sources by the former makefile
clean:
	rm -rf $(BUILD) $(EXE)
	find . -name '*.class' -delete
