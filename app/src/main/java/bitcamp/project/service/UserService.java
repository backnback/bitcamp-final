package bitcamp.project.service;

import bitcamp.project.vo.User;

import java.util.List;

public interface UserService {
    void add(User user)throws Exception;
    List<User> list()throws Exception;
    boolean update(int id, User user)throws Exception;
    User findUser(int id)throws Exception;
    boolean delete(int id)throws Exception;
    User findByEmailAndPassword(String email, String password) throws Exception;
}