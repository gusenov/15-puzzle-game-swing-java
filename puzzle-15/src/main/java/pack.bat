set jdk_dir=C:\Program Files\Java\jdk-10.0.1\bin

del /s /q /f *.class
del /s /q /f *.jar

"%jdk_dir%/javac" -encoding utf8 --source-path . Main.java
"%jdk_dir%/jar" --create --file=15-puzzle.jar --main-class=Main .
"%jdk_dir%/java" -jar 15-puzzle.jar

pause

