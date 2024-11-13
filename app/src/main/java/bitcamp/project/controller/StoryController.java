package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.dto.BatchUpdateRequestDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.service.*;
import bitcamp.project.vo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    private final StoryService storyService;

    @GetMapping("list")
    public ResponseEntity<?> list(
        @LoginUser User loginUser,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "userNickname", required = false) String userNickname,
        @RequestParam(value = "share") boolean share) {
        try {
            List<StoryListDTO> storyListDTOs = storyService.listAllStories(loginUser.getId(), title, userNickname, share);
            return ResponseEntity.ok(storyListDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("view/{storyId}")
    public ResponseEntity<?> view(
        @PathVariable int storyId,
        @LoginUser User loginUser,
        @RequestParam(value = "share") boolean share) {
        try {
            StoryViewDTO storyViewDTO = storyService.viewStory(storyId, loginUser.getId(), share);
            return ResponseEntity.ok(storyViewDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("share/{storyId}")
    public ResponseEntity<?> share(@PathVariable int storyId, @LoginUser User loginUser) {
        try {
            Story story = storyService.changeShare(storyId, loginUser.getId());
            return ResponseEntity.ok(story);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 공유 버튼 배치 업데이트
    @PostMapping("batch-update")
    public ResponseEntity<?> batchUpdate(
        @RequestBody List<BatchUpdateRequestDTO> batchUpdateRequestDTOs,
        @LoginUser User loginUser) {
        try {
            storyService.batchUpdateShares(batchUpdateRequestDTOs, loginUser.getId());
            return ResponseEntity.ok("공유 버튼 배치 처리 완료!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
