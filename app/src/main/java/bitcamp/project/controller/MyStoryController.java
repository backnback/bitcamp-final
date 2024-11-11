package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.dto.AddStoryRequestDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.dto.UpdateStoryRequestDTO;
import bitcamp.project.service.*;
import bitcamp.project.vo.Location;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-story")
public class MyStoryController {

    private static final Logger logger = LoggerFactory.getLogger(MyStoryController.class);
    private final StoryService storyService;

    @GetMapping("list")
    public ResponseEntity<?> list(
        @LoginUser User loginUser, @RequestParam(value = "title", required = false) String title) {
        try {
            List<StoryListDTO> storyListDTOs;
            if (title != null && !title.isEmpty()) {
                storyListDTOs = storyService.listAllMyStoriesByTitle(loginUser.getId(), title);
            } else {
                storyListDTOs = storyService.listAllMyStories(loginUser.getId());
            }
            return ResponseEntity.ok(storyListDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("view/{storyId}")
    public ResponseEntity<?> view(@PathVariable int storyId, @LoginUser User loginUser) {
        try {
            StoryViewDTO storyViewDTO = storyService.viewMyStory(storyId, loginUser.getId());
            return ResponseEntity.ok(storyViewDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("form/add")
    public void formAdd() {
    }


    @GetMapping("form/update/{storyId}")
    public ResponseEntity<?> formUpdate(@PathVariable int storyId, @LoginUser User loginUser) {
        try {
            StoryViewDTO storyViewDTO = storyService.viewMyStory(storyId, loginUser.getId());
            return ResponseEntity.ok(storyViewDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("add")
    public ResponseEntity<?> add (
        @ModelAttribute AddStoryRequestDTO addStoryRequestDTO,
        MultipartFile[] files,
        @LoginUser User loginUser) {
        try {
            // 로그인 사용자를 설정
            addStoryRequestDTO.setLoginUser(loginUser);

            // API 요청 테스트로 인한 return
            ResponseEntity<Map<String, Object>> response = storyService.add(addStoryRequestDTO, files);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("update")
    public ResponseEntity<?> update(
        @ModelAttribute UpdateStoryRequestDTO updateStoryRequestDTO,
        MultipartFile[] files,
        @LoginUser User loginUser) {
        try {
            // 로그인 사용자를 설정
            updateStoryRequestDTO.setLoginUser(loginUser);

            // API 요청 테스트로 인한 return
            ResponseEntity<Map<String, Object>> response = storyService.update(updateStoryRequestDTO, files);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 예외 로그를 기록
            e.printStackTrace();  // 콘솔에 오류 출력
            logger.error("스토리 업데이트 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("delete/{storyId}")
    public ResponseEntity<String> delete(
        @PathVariable int storyId, @LoginUser User loginUser) {
        try {
            storyService.delete(storyId, loginUser.getId());
            return ResponseEntity.ok("스토리 제거가 성공하였습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping("photo/delete/{photoId}")
    public ResponseEntity<String> deletePhoto(
        @PathVariable int photoId, @LoginUser User loginUser) {
        try {
            storyService.removePhoto(photoId, loginUser.getId());
            return ResponseEntity.ok("첨부사진 제거가 성공하였습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
