package bitcamp.project.interceptor;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private final UserService userService;


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String token = request.getHeader("Authorization");

    // 인증이 필요 없는 경로를 설정
    String requestURI = request.getRequestURI();
    if (requestURI.startsWith("/sign") || requestURI.startsWith("/faqs")
        || requestURI.startsWith("/user") || requestURI.startsWith("/location")) {
      return true;
    }


    if (token != null && !token.isEmpty()) {
      User loginUser = userService.decodeToken(token);
      HttpSession session = request.getSession();
      session.setAttribute("loginUser", loginUser); // userId 저장

    } else {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
      return false;
    }

    return true;
  }

}
