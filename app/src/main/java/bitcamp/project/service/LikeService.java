package bitcamp.project.service;

import bitcamp.project.vo.Like;
import bitcamp.project.vo.User;

import java.util.List;

public interface LikeService {

  void addLike(int storyId, int userId) throws Exception;

  Like get(int storyId, int userId) throws Exception;

  List<Like> findAllByStory(int storyId) throws Exception;

  List<User> findAllToMe(int userId) throws Exception;

  int countLikes(int storyId) throws Exception;

  boolean getStatus(int storyId, int userId) throws Exception;

  void confirmLikeView(int storyId, int userId, int loginUserId) throws Exception;

  void deleteLike(int storyId, int userId) throws Exception;
}
