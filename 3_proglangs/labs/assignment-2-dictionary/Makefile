NASM ?= nasm
LD ?= ld
PYTHON ?= python3

OBJECTS = main.o dict.o lib.o

.PHONY: all test clean

all: main

main: $(OBJECTS)
	$(LD) -o $@ $(OBJECTS)

main.o: main.asm words.inc colon.inc lib.inc dict.inc
	$(NASM) -f elf64 -o $@ $<

dict.o: dict.asm lib.inc
	$(NASM) -f elf64 -o $@ $<

lib.o: lib.asm
	$(NASM) -f elf64 -o $@ $<

test: main
	$(PYTHON) test.py

clean:
	rm -f main $(OBJECTS) report.xml
