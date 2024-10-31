package bitcamp.project.controller;

import bitcamp.project.service.LikeService;
import bitcamp.project.service.StoryService;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.Like;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

  private final LikeService likeService;
  private final StoryService storyService;

  // 특정 스토리의 좋아요 목록 (확인 / 미확인 모두)
  @GetMapping("list/story/{storyId}")
  public List<Like> listByStory(@PathVariable int storyId) throws Exception {
    return likeService.findAllByStory(storyId);
  }

  // 나한테 온 모든 좋아요 목록 (미확인만)
  @GetMapping("list/{userId}")
  public List<User> findAllToMe(@PathVariable int userId) throws Exception {
    return likeService.findAllToMe(userId);
  }


  // 내가 좋아요한 스토리 목록
  @GetMapping("list/user/{userId}")
  public ResponseEntity<List<Map<String, Object>>> findAllByMyLike(
      @PathVariable int userId) throws Exception {

    List<Map<String, Object>> responseList = new ArrayList<>();


    // Story 1개 + Main 사진 1개
    for (Story story : storyService.findAllByMyLike(userId)) {
      if (!story.isShare()) {
        continue;
      }

      HashMap<String, Object> map = new HashMap<>();
      map.put("story", story);

      for (Photo photo : storyService.getPhotos(story.getId())) {
        if (photo.isMainPhoto()) {
          map.put("mainPhoto", photo);
        }
      }

      int likeCount = likeService.findAllByStory(story.getId()).size();
      map.put("likeCount", likeCount);

      responseList.add(map);
    }

    return ResponseEntity.ok(responseList);
  }


  // 좋아요 등록
  @GetMapping("add/{storyId}")
  public void add(
      @PathVariable int storyId, @RequestParam int userId) throws Exception {
    Story story = storyService.get(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    }

    likeService.add(storyId, userId);
  }

  // 좋아요 취소
  @DeleteMapping("delete/{storyId}")
  public void delete(
      @PathVariable int storyId, @RequestParam int userId) throws Exception {

    Story story = storyService.get(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    } else if (story.getUser().getId() != userId) {
      throw new Exception("접근 권한이 없습니다.");
    }

    likeService.delete(storyId, userId);
  }


  // 스토리 알림 확인 시 목록에서 제거
  @GetMapping("confirm/{storyId}/{otherUserId}")
  public void confirmView(
      @PathVariable int storyId, @PathVariable int otherUserId,
      @RequestParam int loginUserId) throws Exception {

    Story story = storyService.get(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    } else if  (story.getUser().getId() != loginUserId) {
      throw new Exception("접근 권한이 없습니다.");
    }

    Like like = likeService.get(storyId, otherUserId);
    if (like.isView()) {
      throw new Exception("이미 확인하였습니다.");
    }

    likeService.confirmView(storyId, otherUserId);
  }

}
