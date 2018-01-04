SRC=$(shell find ./ -name '*.cljs')

build: build/js/app.js public/**/**
	rsync -az --exclude js public/ build
	touch build

build/js/app.js: $(SRC)
	mkdir -p build/js
	lein package

.PHONY: clean

clean:
	lein clean
	rm -rf build

