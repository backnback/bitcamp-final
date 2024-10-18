package bitcamp.project.service;

import bitcamp.project.vo.User;

import java.util.List;

public interface UserService {
    void add(User user)throws Exception;
    List<User> list()throws Exception;
    boolean update(int no, User user)throws Exception;
    User findUser(int no)throws Exception;
    boolean delete(int no)throws Exception;
}
