package bitcamp.project.dao;

import bitcamp.project.vo.Like;
import bitcamp.project.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LikeDao {

  boolean insert(@Param("storyId") int storyId, @Param("userId") int userId) throws Exception;

  Like findBy(@Param("storyId") int storyId, @Param("userId") int userId) throws Exception;

  List<Like> findAllByStory(int storyId) throws Exception;

  List<User> findAllToMe(int userId) throws Exception;

  boolean update(Like like) throws Exception;

  boolean delete(@Param("storyId") int storyId, @Param("userId") int userId) throws Exception;

  boolean deleteAllByStory(int storyId) throws Exception;

  int getStatus(@Param("storyId") int storyId, @Param("userId") int userId) throws Exception;
}
