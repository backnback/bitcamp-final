package bitcamp.project;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration // Spring Boot가 자동으로 실행하게 만드는 애너테이션
public class EnvConfig {

    @PostConstruct // 애플리케이션 초기화 시 실행
    public void loadEnv() {
        // .env 파일 로드
        Dotenv dotenv = Dotenv.load();

        // 시스템 프로퍼티로 환경 변수 설정
        System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));

//        // 확인용 로그
//        System.out.println("GOOGLE_CLIENT_ID: " + System.getProperty("GOOGLE_CLIENT_ID"));
//        System.out.println("GOOGLE_CLIENT_SECRET: " + System.getProperty("GOOGLE_CLIENT_SECRET"));
    }
}
