package bitcamp.project.annotation;

import bitcamp.project.vo.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {

    return parameter.hasParameterAnnotation(LoginUser.class) &&
        parameter.getParameterType().isAssignableFrom(User.class);
  }


  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
    HttpSession session = request.getSession();
    return session.getAttribute(parameter.getParameterName());
  }
}
