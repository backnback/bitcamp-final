package bitcamp.project.service.impl;

import bitcamp.project.dao.LikeDao;
import bitcamp.project.dao.StoryDao;
import bitcamp.project.dto.AlarmListDTO;
import bitcamp.project.dto.BatchUpdateRequestDTO;
import bitcamp.project.service.LikeService;
import bitcamp.project.vo.Like;

import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeDao likeDao;
  private final StoryDao storyDao;


  @Override
  public void changeLike(BatchUpdateRequestDTO batchUpdateRequestDTO, int userId)
      throws Exception {

      int storyId = batchUpdateRequestDTO.getStoryId();
      Story story = storyDao.findByStoryId(storyId);
      if (story == null) {
        throw new Exception("없는 스토리입니다");
      }

      if (batchUpdateRequestDTO.getAction().equals("add")) {
        addLike(storyId, userId);

      } else if (batchUpdateRequestDTO.getAction().equals("delete")) {
        deleteLike(storyId, userId);
      }
  }


  @Override
  public Like get(int storyId, int userId) throws Exception {
    return likeDao.findBy(storyId, userId);
  }


  @Override
  public List<AlarmListDTO> findAllToMe(int userId) throws Exception {

    List<Map<String, Object>> result = likeDao.findAllToMe(userId);

    List<AlarmListDTO> alarmListDTOs = new ArrayList<>();
    for (Map<String, Object> row : result) {
      AlarmListDTO alarmDTO = new AlarmListDTO();
      alarmDTO.setUserId((Integer) row.get("user_id"));
      alarmDTO.setUserEmail((String) row.get("email"));
      alarmDTO.setUserNickname((String) row.get("nickname"));
      alarmDTO.setUserPath((String) row.get("path"));
      alarmDTO.setStoryId((Integer) row.get("story_id"));  // storyId 추가
      alarmListDTOs.add(alarmDTO);
    }

    return alarmListDTOs;
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
  public void deleteLikes(int storyId) throws Exception {
    likeDao.deleteAllByStory(storyId);
  }


  private void addLike(int storyId, int userId) throws Exception {
    Like like = likeDao.findBy(storyId, userId);
    if (like != null) {
      System.out.printf("%d번 좋아요는 이미 처리된 좋아요 입니다!\n", storyId);
      return;
    }

    likeDao.insert(storyId, userId);
  }


  private void deleteLike(int storyId, int userId) throws Exception {
    Like like = likeDao.findBy(storyId, userId);
    if (like == null) {
      System.out.printf("%d번 좋아요는 존재하지 않아 삭제 불가 !\n", storyId);
      return;
    }

    likeDao.delete(storyId, userId);
  }



}
