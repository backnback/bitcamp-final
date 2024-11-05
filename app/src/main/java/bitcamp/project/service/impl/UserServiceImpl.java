package bitcamp.project.service.impl;

import bitcamp.project.dao.UserDao;
import bitcamp.project.security.JwtTokenProvider;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.JwtToken;
import bitcamp.project.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${jwt.secret}")
    String secret;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Override
    public List<User> list(){
        return userDao.findAll();
    }

    @Transactional
    @Override
    public boolean update(int id, User user) throws Exception {
        return userDao.update(id, user);
    }

    @Override
    public User findUser(int id) throws Exception {
        return userDao.findUser(id);
    }

    @Transactional
    @Override
    public boolean delete(int id) throws Exception {
        return userDao.delete(id);
    }

    @Override
    public User findByEmailAndPassword(String email) throws Exception {
        return userDao.findByEmailAndPassword(email);
    }

    @Override
    public JwtToken makeToken(String email, String password) {
        try {
            // 사용자 인증을 위한 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

            // CustomAuthenticationProvider를 통해 인증 수행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            User user = (User) authentication.getPrincipal();
            List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities()); // 권한 정보 가져오기

            // ADMIN 역할 부여 로직
            if (user.getEmail().equals(adminUsername) && password.equals(adminPassword)) {
                user.setRole("ADMIN"); // ADMIN 역할 부여
            } else {
                user.setRole("USER"); // 기본 사용자 역할 설정
            }

            // 인증 성공 시 JWT 토큰 생성
            return jwtTokenProvider.generateToken(user, authorities);
        } catch (AuthenticationException e) {
            // 인증 실패 시 오류 메시지 처리
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다."); // 적절한 예외 클래스를 사용해야 할 수도 있습니다.
        } catch (Exception e) {
            // 다른 예외 처리
            throw new RuntimeException("인증 중 오류가 발생했습니다."); // 적절한 예외 클래스를 사용해야 할 수도 있습니다.
        }
    }

    @Override
    public String encodePassword(String password) throws Exception {
        return passwordEncoder.encode(password);
    }

    @Override
    public User decodeToken(String token) throws Exception {
        try {
            // Bearer 문자열 제거
            String jwtToken = token.replace("Bearer ", "");

            // 토큰을 디코딩하고 페이로드 정보 추출
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwtToken)
                    .getBody();

            // Claims에서 필요한 정보를 추출하여 User 객체 생성
            User user = new User();
            user.setEmail(claims.getSubject()); // 사용자 이메일
            user.setNickname(claims.get("nickname", String.class)); // 닉네임
            user.setId(claims.get("userId", Integer.class)); // 사용자 ID
            user.setPath(claims.get("path", String.class)); // 경로
            user.setRole(claims.get("auth", String.class)); // 역할

            return user;
        }catch (Exception e){
            e.printStackTrace();
            throw e; // 예외를 다시 던져 호출자에게 알림
        }
    }
}