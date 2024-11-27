# 기본 이미지 설정 (OpenJDK 21)
FROM openjdk:21-jdk

# JAR 파일 경로 설정
ARG JAR_FILE=app/build/libs/bitcamp-final.jar

# JAR 파일을 컨테이너에 복사
COPY ${JAR_FILE} bitcamp-final.jar

# 설정 파일들 복사
COPY ./bitcamp-final/.env /root/.env
COPY ./bitcamp-final/config/final.properties /root/config/final.properties
COPY ./bitcamp-final/config/email.properties /root/config/email.properties

# 컨테이너 실행 시 java로 JAR 파일 실행
ENTRYPOINT [ "java", "-jar", "bitcamp-final.jar" ]