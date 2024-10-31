package bitcamp.project.service.impl;

import bitcamp.project.dao.UserDao;
import bitcamp.project.security.JwtTokenProvider;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.JwtToken;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
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
}