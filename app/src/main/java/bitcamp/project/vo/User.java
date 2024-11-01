package bitcamp.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class User {
    private int id;
    private String email;
    private String password;
    private String nickname;
    private String path;
    private Collection<? extends GrantedAuthority> authorities;

    public User() {
    }

    public User(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }
}
