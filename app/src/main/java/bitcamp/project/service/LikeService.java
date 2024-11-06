package bitcamp.project.service;

import bitcamp.project.dto.UpdateLikeRequestDTO;
import bitcamp.project.vo.Like;
import bitcamp.project.vo.User;

import java.util.List;

public interface LikeService {

  void batchUpdateLikes(List<UpdateLikeRequestDTO> updateLikeRequestDTOs, int userId) throws Exception;

  Like get(int storyId, int userId) throws Exception;

  List<User> findAllToMe(int userId) throws Exception;

  int countLikes(int storyId, boolean likeStatus) throws Exception;

  boolean getStatus(int storyId, int userId) throws Exception;

  void confirmLikeView(int storyId, int userId, int loginUserId) throws Exception;

  void deleteLikes(int storyId) throws Exception;
}
