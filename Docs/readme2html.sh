#!/bin/bash
if [[ -z "$(which pandoc)" ]]; then
    sudo apt install -y pandoc
fi
cd $(dirname "$0")/..
pandoc README.md -f markdown -t html -s --css=readme-style.css -o Readme.html
pandoc README-it.md -f markdown -t html -s --css=readme-style.css -o Readme-it.html