JFLAGS :=

ifdef DEBUG
JFLAGS += -Xdiags:verbose
JFLAGS_TEXT := with compile flags $(JFLAGS)
endif

JFLAGS += -parameters

OUTPUT := bin/java
INPUT  := src/java

MAIN   := test/plcc/GrammarTest
OBJ    := $(OUTPUT)/$(MAIN).class $(OUTPUT)/numlist/package-info.class

build: $(OUTPUT) clean $(OBJ)

bin: 
	@echo "Creating bin" && \
	mkdir bin

$(OUTPUT): bin
	@echo "Creating $(OUTPUT)" && \
	mkdir $(OUTPUT)

$(OUTPUT)/%.class: $(INPUT)/%.java
	@echo "Compiling $< $(JFLAGS_TEXT)" && \
	javac $(JFLAGS) -cp $(OUTPUT) -d $(OUTPUT) -sourcepath $(INPUT) $<

.PHONY: clean build run

run: 
	java -cp $(OUTPUT) $(MAIN)

clean: $(OUTPUT)
	@echo "Cleaning old binary files" && \
	rm -rf $(OUTPUT)/*
