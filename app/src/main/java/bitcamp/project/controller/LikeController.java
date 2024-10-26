package bitcamp.project.controller;

import bitcamp.project.service.LikeService;
import bitcamp.project.service.StoryService;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.Like;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

  private final LikeService likeService;
  private final StoryService storyService;
  private final UserService userService;


  @GetMapping("list/story/{storyId}")
  public List<Like> listByStory(@PathVariable int storyId) throws Exception {
    return likeService.findAllByStory(storyId);
  }


  @GetMapping("list/user/{userId}")
  public List<Like> listByUser(@PathVariable int userId) throws Exception {
    return likeService.findAllByUser(userId);
  }


  @GetMapping("add/{storyId}")
  public void add(@PathVariable int storyId) throws Exception {
    User loginUser = userService.findUser(4);

    Story story = storyService.get(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    }

    likeService.add(storyId, loginUser.getId());
  }

  @DeleteMapping("delete/{storyId}")
  public void delete(@PathVariable int storyId) throws Exception {
    User loginUser = userService.findUser(4);

    Story story = storyService.get(storyId);
    if (story == null) {
      throw new Exception("없는 스토리입니다");
    }

    likeService.delete(storyId, loginUser.getId());
  }

}
