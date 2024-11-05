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
    private String role;

    private Collection<? extends GrantedAuthority> authorities;

    public User() {
    }

    public User(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // 역할 추가 및 수정 메서드
    public void addRole(String role) {
        this.role = role;
        // 추가적인 역할 관리 로직 구현 가능
    }

    public boolean isAdmin() {
        return "ADMIN".equals(this.role); // 어드민 여부 확인 메서드
    }
}
