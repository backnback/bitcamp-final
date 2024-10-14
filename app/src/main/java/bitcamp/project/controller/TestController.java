package bitcamp.project.controller;

import bitcamp.project.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class TestController {
    @GetMapping
    public List<User> getUsers() {
        List<User> users = Arrays.asList(
                new User(1, "홍길동", "hong@example.com"),
                new User(2, "김철수", "kim@example.com")
        );

        return users; // List<User>를 JSON 형식으로 반환
    }
}
