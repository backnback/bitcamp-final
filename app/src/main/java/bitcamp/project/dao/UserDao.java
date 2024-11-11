package bitcamp.project.dao;

import bitcamp.project.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    boolean add(User user);
    List<User> findAll();
    User findUser(int id);
    boolean update(int id, User user);
    boolean delete(int id);
    User findByEmailAndPassword(@Param("email") String email);
    boolean updatePassword(String email, String password);
    User checkPassword(int id);

}