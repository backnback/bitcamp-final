package bitcamp.project.service;

import bitcamp.project.vo.User;

import java.util.List;

public interface UserService {
    void add(User user);
    List<User> list()throws Exception;
}
