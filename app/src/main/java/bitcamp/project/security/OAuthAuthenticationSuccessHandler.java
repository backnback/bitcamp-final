package bitcamp.project.security;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.JwtToken;
import bitcamp.project.vo.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    public OAuthAuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, @Lazy UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;

            // OAuth2 사용자 정보 가져오기
            Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
            String email = (String) attributes.get("email");

            try {
                // 사용자 확인
                User user = userService.findByEmailAndPassword(email);

                List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities()); // 권한 정보 가져오기
                user.setRole("USER"); // 기본 사용자 역할 설정

                // JWT 토큰 생성
                JwtToken token = jwtTokenProvider.generateToken(user, authorities);

                String redirectUrl = "http://localhost:3000/oauth2/redirect?accessToken=" + token.getAccessToken() + "&refreshToken=" + token.getRefreshToken();

                // 프론트엔드로 리디렉션
                response.sendRedirect(redirectUrl);
                System.out.println(redirectUrl);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Failed to process OAuth2 login\"}");
                response.getWriter().flush();
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication failed\"}");
            response.getWriter().flush();
        }
    }

}



