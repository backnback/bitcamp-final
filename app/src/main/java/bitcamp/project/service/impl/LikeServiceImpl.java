package bitcamp.project.service.impl;

import bitcamp.project.dao.LikeDao;
import bitcamp.project.dao.StoryDao;
import bitcamp.project.service.LikeService;
import bitcamp.project.vo.Like;

import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeDao likeDao;
  private final StoryDao storyDao;


  @Override
  public void addLike(int storyId, int userId) throws Exception {
    Story story = storyDao.findByStoryId(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    }

    likeDao.insert(storyId, userId);
  }


  @Override
  public Like get(int storyId, int userId) throws Exception {
    return likeDao.findBy(storyId, userId);
  }


  @Override
  public List<User> findAllToMe(int userId) throws Exception {
    return likeDao.findAllToMe(userId);
  }


  @Override
  public int countLikes(int storyId, boolean likeStatus) throws Exception {
    int likeCount = likeDao.findAllByStory(storyId).size();
    return likeStatus ? likeCount - 1 : likeCount;
  }


  @Override
  public boolean getStatus(int storyId, int userId) throws Exception {
    return likeDao.getStatus(storyId, userId) > 0;
  }


  @Override
  public void confirmLikeView(int storyId, int otherUserId, int loginUserId) throws Exception {

    Story story = storyDao.findByStoryId(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    } else if  (story.getUser().getId() != loginUserId) {
      throw new Exception("접근 권한이 없습니다.");
    }

    Like like = likeDao.findBy(storyId, otherUserId);
    if (like.isView()) {
      throw new Exception("이미 확인하였습니다.");
    }

    like.setView(true);
    likeDao.update(like);
  }


  @Override
  public void deleteLike(int storyId, int userId) throws Exception {

    Story story = storyDao.findByStoryId(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    }

    Like like = likeDao.findBy(storyId, userId);
    if (like == null) {
      throw new Exception("접근 권한이 없습니다.");
    }

    likeDao.delete(storyId, userId);
  }


  @Override
  public void deleteLikes(int storyId) throws Exception {
    likeDao.deleteAllByStory(storyId);
  }



}
