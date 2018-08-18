set jdk_dir=C:\Program Files\Java\jdk-10.0.1\bin

del /s /q /f *.class
"%jdk_dir%/javac" -encoding utf8 --source-path . Main.java
"%jdk_dir%/java" --class-path . Main
pause
