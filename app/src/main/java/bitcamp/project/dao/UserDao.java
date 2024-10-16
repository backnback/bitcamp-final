package bitcamp.project.dao;

import bitcamp.project.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    List<User> findAll();
}
