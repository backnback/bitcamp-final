FROM openjdk:21-jdk

ARG JAR_FILE=app/build/libs/bitcamp-final.jar 

COPY ${JAR_FILE} bitcamp-final.jar

ENTRYPOINT [ "java", "-jar", "bitcamp-final.jar" ]