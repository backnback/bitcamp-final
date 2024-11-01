package bitcamp.project.service.impl;

import bitcamp.project.dao.LikeDao;
import bitcamp.project.service.LikeService;
import bitcamp.project.vo.Like;

import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeDao likeDao;

  @Override
  public void add(int storyId, int userId) throws Exception {
    likeDao.insert(storyId, userId);
  }

  @Override
  public Like get(int storyId, int userId) throws Exception {
    return likeDao.findBy(storyId, userId);
  }

  @Override
  public List<Like> findAllByStory(int storyId) throws Exception {
    return likeDao.findAllByStory(storyId);
  }

  @Override
  public List<User> findAllToMe(int userId) throws Exception {
    return likeDao.findAllToMe(userId);
  }

  @Override
  public int countLikes(int storyId) throws Exception {
    return likeDao.findAllByStory(storyId).size();
  }

  @Override
  public boolean getStatus(int storyId, int userId) throws Exception {
    return likeDao.getStatus(storyId, userId) > 0;
  }

  @Override
  public void confirmView(int storyId, int userId) throws Exception {
    Like like = new Like();
    like.setStoryId(storyId);
    like.setUserId(userId);
    like.setView(true);
    likeDao.update(like);
  }

  @Override
  public void delete(int storyId, int userId) throws Exception {
    likeDao.delete(storyId, userId);
  }
}
