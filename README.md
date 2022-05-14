# spark-m2t-generator


### Zlozky:
 - bin/  -  skompilovany generator vhodny na pouzitie
 - libs/ - zlozka obsahuje potrebne .jar subory na spustenie generatora
 - doc/ -  zdrojove subory technickej spravy
 - out/ - vystupna zlozka pre vygenerovany kod
 - src/  - zdrojove subory generatora
 - sparkWordCount/  - priklad c1
 - sparkPageRank/ - priklad c2
 - dateset-processing/ - priklad c3
 - Makefile - sluzi na vygenerovanie kodu pre priklady

### Vygenerovanie kodu z prikladov:
    make

### Vytvorenie spustitelneho balika z vygenerovaneho kodu:
    cd out
    make package

### Spustenie prikladov:
    cd out/
    make run-wordcount
    make run-pagerank
    make run-dataset
