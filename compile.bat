rmdir build /s /q

mkdir build

dir /s /b .\src\*.java > sources.txt

"C:\Program Files\BellSoft\jdk-17.0.13-full\bin\javac.exe" -cp .;./src;./lib/mariadb-java-client-3.5.1.jar -d ./build -encoding UTF-8 @sources.txt

"C:\Program Files\BellSoft\jdk-17.0.13-full\bin\java.exe" -cp .;./build;./lib/mariadb-java-client-3.5.1.jar -Dfile.encoding=UTF-8 %1
