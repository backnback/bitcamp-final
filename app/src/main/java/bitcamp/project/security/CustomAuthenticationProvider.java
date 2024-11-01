package bitcamp.project.security;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String email = authentication.getName(); // 이메일 추출
        String password = (String) authentication.getCredentials(); // 비밀번호 추출

        // 사용자 정보 조회
        User user = null;
        try {
            user = userService.findByEmailAndPassword(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 비밀번호 비교 (해시 비교)
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 권한을 String으로 저장하고, GrantedAuthority로 변환
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (user.getRole() != null) {
                authorities.add(new SimpleGrantedAuthority(user.getRole())); // 역할을 GrantedAuthority로 추가
            }

            // 인증 성공
            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        }

        // 인증 실패
        throw new AuthenticationException("Invalid credentials") {};
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
