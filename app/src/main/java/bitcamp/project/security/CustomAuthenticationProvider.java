package bitcamp.project.security;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
        User user = null; // 이메일로 사용자 조회
        try {
            user = userService.findByEmailAndPassword(email);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 비밀번호 비교 (해시 비교)
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 인증 성공
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }

        // 인증 실패
        throw new AuthenticationException("Invalid credentials") {};
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
