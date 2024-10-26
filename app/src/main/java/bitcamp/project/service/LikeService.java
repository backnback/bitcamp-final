package bitcamp.project.service;

import bitcamp.project.vo.Like;
import bitcamp.project.vo.Location;

import java.util.List;

public interface LikeService {

  void add(int storyId, int userId) throws Exception;

  Like get(int storyId, int userId) throws Exception;

  List<Like> findAllByStory(int storyId) throws Exception;

  List<Like> findAllByUser(int userId) throws Exception;

  void confirmView(int storyId, int userId) throws Exception;

  void delete(int storyId, int userId) throws Exception;
}
