ifndef ARGS
ARGS := -s NumList.lang -c numlist.NumList
endif

JFLAGS :=

ifdef DEBUG
JFLAGS += -Xdiags:verbose -Xlint:unchecked
JFLAGS_TEXT := with compile flags $(JFLAGS)
endif

JFLAGS += -parameters

OUTPUT := bin/java
INPUT  := src/java

MAIN   := plcc/Main
OBJ    := $(OUTPUT)/$(MAIN).class $(OUTPUT)/numlist/NumList.class

build: $(OUTPUT) clean $(OBJ)

jar: build
	@echo "Creating JAR executable" && \
	jar cfe $(OUTPUT)/laps.jar $(MAIN) -C $(OUTPUT) numlist/ -C $(OUTPUT) plcc/ -C $(INPUT) numlist/

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
	java -cp $(OUTPUT) $(MAIN) $(ARGS)

clean: $(OUTPUT)
	@echo "Cleaning old binary files" && \
	rm -rf $(OUTPUT)/*
