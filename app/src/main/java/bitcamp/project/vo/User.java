package bitcamp.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class User {
    private int id;
    private String email;
    private String password;
    private String nickname;
    private String path;
}
