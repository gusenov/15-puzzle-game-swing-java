#!/bin/bash


output_dir="classfiles"


clear  # очистка экрана


# Компиляция в .class

rm $output_dir/*.class
#javac -verbose -d "$output_dir" --source-path . Main.java
javac -d "$output_dir" --source-path . Main.java


# Запуск .class

#java --class-path "$output_dir" Main


# Упаковка в .jar

jar --create --file=15-puzzle.jar --main-class=Main -C $output_dir .
rm $output_dir/*.class
#jar --list --verbose --file=15-puzzle.jar  # просмотр содержимого .jar


# Запуск .jar

java -jar 15-puzzle.jar
