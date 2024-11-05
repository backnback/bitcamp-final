package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.service.LikeService;
import bitcamp.project.service.StoryService;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

  private final LikeService likeService;
  private final StoryService storyService;
  @Autowired
  private UserService userService;

  // 나를 좋아요한 User 목록 (미확인만)
  // 확인 방법 : confirmView()
  @GetMapping("list/users")
  public ResponseEntity<?> findAllToMe(@LoginUser User loginUser) {
    try {
      List<User> users = likeService.findAllToMe(loginUser.getId());
      return ResponseEntity.ok(users);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("나를 좋아요한 유저 목록을 불러오는 데 실패했습니다.");
    }
  }


  // 내가 좋아요한 스토리 목록
  @GetMapping("list/my-stories")
  public ResponseEntity<?> listAllMyLikeStories(@LoginUser User loginUser) {
    try {
      List<StoryListDTO> storyListDTOs = storyService.listAllMyLikeStories(loginUser.getId());
      return ResponseEntity.ok(storyListDTOs);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("내가 좋아요한 스토리 목록을 불러오는 데 실패했습니다.");
    }
  }


  // 좋아요 등록
  @GetMapping("add/{storyId}")
  public ResponseEntity<String> add(
      @PathVariable int storyId, @LoginUser User loginUser) {
    try {
      likeService.addLike(storyId, loginUser.getId());
      return ResponseEntity.ok("좋아요 등록 완료!");

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // 좋아요 취소
  @DeleteMapping("delete/{storyId}")
  public ResponseEntity<String> delete(
      @PathVariable int storyId, @LoginUser User loginUser) {
    try {
      likeService.deleteLike(storyId, loginUser.getId());
      return ResponseEntity.ok("좋아요 삭제 완료!");

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }


  // 스토리 알림 확인 시 목록에서 제거
  @GetMapping("confirm/{storyId}/{otherUserId}")
  public ResponseEntity<String> confirmView(
      @PathVariable int storyId, @PathVariable int otherUserId,
      @LoginUser User loginUser) {
    try {
      likeService.confirmLikeView(storyId, otherUserId, loginUser.getId());
      return ResponseEntity.ok("알림 확인 완료!");

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

}
