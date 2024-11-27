# 기본 이미지 설정 (OpenJDK 21)
FROM openjdk:21-jdk

# JAR 파일 경로 설정
ARG JAR_FILE=app/build/libs/bitcamp-final.jar

# JAR 파일을 컨테이너에 복사
COPY ${JAR_FILE} bitcamp-final.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT [ "java", "-jar", "bitcamp-final.jar" ]