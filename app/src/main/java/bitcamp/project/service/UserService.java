package bitcamp.project.service;

import bitcamp.project.vo.JwtToken;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface UserService {
    void add(User user)throws Exception;
    List<User> list()throws Exception;
    boolean update(int id, User user)throws Exception;
    User findUser(int id)throws Exception;
    boolean delete(int id)throws Exception;
    User findByEmailAndPassword(String email) throws Exception;
    JwtToken makeToken(String email, String password) throws Exception;
    String encodePassword(String password)throws Exception;
    User decodeToken(String token)throws Exception;
    boolean updatePassword(String email, String password)throws Exception;
    boolean userAuthentication(int id, String password)throws Exception;
    JwtToken generateTokenWithoutAuthentication(User user)throws Exception;
}