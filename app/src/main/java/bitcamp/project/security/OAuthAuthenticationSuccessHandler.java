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
import java.io.PrintWriter;
import java.net.URLEncoder;
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
            String name = (String) attributes.get("name");
            String picture = (String) attributes.get("picture");

            try {
                // 사용자 확인
                User user = userService.findByEmailAndPassword(email);

                if (user != null) {
                    // 기존 사용자: JWT 생성 후 리다이렉션
                    List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
                    JwtToken token = jwtTokenProvider.generateToken(user, authorities);
                    String redirectUrl = "http://go.remapber.p-e.kr/social/redirect?accessToken=" + token.getAccessToken() + "&refreshToken=" + token.getRefreshToken();
                    response.sendRedirect(redirectUrl);
                } else {
                    // 신규 사용자: 추가 정보 입력 페이지로 리다이렉션
                    String registrationRedirectUrl = "http://go.remapber.p-e.kr/signup";
                    registrationRedirectUrl += "?email=" + URLEncoder.encode(email, "UTF-8");
                    registrationRedirectUrl += "&name=" + URLEncoder.encode(name, "UTF-8");
                    registrationRedirectUrl += "&picture=" + URLEncoder.encode(picture, "UTF-8");

                    response.sendRedirect(registrationRedirectUrl);
                }

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