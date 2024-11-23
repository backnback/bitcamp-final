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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        @RequestParam(value = "sortBy", required = false) String sortBy,
        @RequestParam(value = "share") boolean share,
        @RequestParam(value = "limit", required = false, defaultValue = "6") int limit) { // limit 파라미터 추가
        try {
            Map<String, Object> response = new HashMap<>();
            if(storyService.hasMoreStories(loginUser.getId(), title, userNickname, share, limit)) {
                // StoryService에서 limit 값으로 데이터를 제한

                List<StoryListDTO> storyListDTOs = storyService.listAllStories(
                    loginUser.getId(), title, userNickname, share, sortBy, limit
                );

                response.put("stories", storyListDTOs);
                response.put("hasMore", true);

            }else{
                int fullCount = storyService.countStories(loginUser.getId(), title, userNickname, share);

                List<StoryListDTO> storyListDTOs = storyService.listAllStories(
                    loginUser.getId(), title, userNickname, share, sortBy, fullCount
                );

                response.put("stories", storyListDTOs);
                response.put("hasMore", false);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("admindelete/{storyId}")
    public ResponseEntity<String> adminDelete(
            @PathVariable int storyId, @LoginUser User loginUser) {
        try {
            storyService.adminDelete(storyId);
            return ResponseEntity.ok("스토리 제거가 성공하였습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("alllist")
    public ResponseEntity<?> allList(
            @LoginUser User loginUser,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "userNickname", required = false) String userNickname,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "limit", required = false, defaultValue = "6") int limit) { // limit 파라미터 추가
        try {
            Map<String, Object> response = new HashMap<>();
            if(storyService.countAllStories() > limit) {
                // StoryService에서 limit 값으로 데이터를 제한

                List<StoryListDTO> storyListDTOs = storyService.adminListAllStories(
                        loginUser.getId(), title, userNickname, sortBy, limit
                );

                response.put("stories", storyListDTOs);
                response.put("hasMore", true);

            }else{
                int fullCount = storyService.countAllStories();

                List<StoryListDTO> storyListDTOs = storyService.adminListAllStories(
                        loginUser.getId(), title, userNickname, sortBy, fullCount
                );

                response.put("stories", storyListDTOs);
                response.put("hasMore", false);
            }
            return ResponseEntity.ok(response);
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
    @PostMapping("share-update")
    public ResponseEntity<?> shareUpdate(
        @RequestBody BatchUpdateRequestDTO batchUpdateRequestDTO,
        @LoginUser User loginUser) {
        try {
            storyService.changeShare(batchUpdateRequestDTO, loginUser.getId());
            return ResponseEntity.ok("공유 버튼 배치 처리 완료!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
