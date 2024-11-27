rmdir build /s /q

mkdir build

dir /s /b .\src\*.java > sources.txt

"C:\Program Files\BellSoft\LibericaJDK-21-Full\bin\javac.exe" -cp .;./src;./lib/mariadb-java-client-3.5.1.jar -d ./build @sources.txt

"C:\Program Files\BellSoft\LibericaJDK-21-Full\bin\java.exe" -cp .;./build;./lib/mariadb-java-client-3.5.1.jar %1
