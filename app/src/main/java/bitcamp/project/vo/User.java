package bitcamp.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String password;
    private String nickname;
    private String path;
}