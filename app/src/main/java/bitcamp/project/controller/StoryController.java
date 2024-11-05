package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.service.*;
import bitcamp.project.vo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    private final StoryService storyService;

    @GetMapping("share/{storyId}")
    public ResponseEntity<?> share(@PathVariable int storyId, @LoginUser User loginUser) {
        try {
            Story story = storyService.changeShare(storyId, loginUser.getId());
            return ResponseEntity.ok(story);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
