package bitcamp.project.controller;

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

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my-story")
public class MyStoryController {

    private final StoryService storyService;
    private final StorageService storageService;
    private final PhotoService photoService;

    private String folderName = "story/";


    @GetMapping("list/{userId}")
    public ResponseEntity<?> list(@PathVariable int userId) {
        try {
            List<StoryListDTO> storyListDTOs = storyService.listAllMyStories(userId);
            return ResponseEntity.ok(storyListDTOs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("내 스토리 목록 가져오기 실패");
        }
    }


    @GetMapping("view/{storyId}/{userId}")
    public ResponseEntity<?> view(
        @PathVariable int storyId,
        @RequestParam int userId) {
        try {
            StoryViewDTO storyViewDTO = storyService.viewMyStory(storyId, userId);
            return ResponseEntity.ok(storyViewDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("내 스토리 상세 조회 실패");
        }
    }


    @GetMapping("form/add")
    public void formAdd() {
    }

    @GetMapping("form/update/{storyId}")
    public ResponseEntity<Map<String, Object>> formUpdate(
        @PathVariable int storyId, @RequestParam int userId) throws Exception {
        Story story = storyService.get(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }

        if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        List<Photo> photos = photoService.getPhotos(storyId);

        // Story와 함께 Photo 데이터 보내기
        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("delete/{storyId}")
    public void delete(
        @PathVariable int storyId, @RequestParam int userId) throws Exception {

        Story story = storyService.get(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }

        if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        for (Photo photo : photoService.getPhotos(storyId)) {
            try {
                // 첨부파일 삭제
                storageService.delete(folderName + photo.getPath());

            } catch (Exception e) {
                System.out.printf("%s 파일 ", photo.getPath());
                throw new Exception("삭제 실패 !");
            }
        }

        storyService.delete(storyId);
    }

    @PostMapping("add")
    public ResponseEntity<?> add (
        @ModelAttribute AddStoryRequestDTO addStoryRequestDTO,
        MultipartFile[] files) {
        try {  // API 요청 테스트로 인한 return
            ResponseEntity<Map<String, Object>> response = storyService.add(addStoryRequestDTO, files);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("스토리 추가에 실패했습니다.");
        }
    }


    @PostMapping("update")
    public ResponseEntity<?> update(
        @ModelAttribute UpdateStoryRequestDTO updateStoryRequestDTO,
        MultipartFile[] files) {
        try {  // API 요청 테스트로 인한 return
            ResponseEntity<Map<String, Object>> response = storyService.update(updateStoryRequestDTO, files);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("스토리 수정에 실패했습니다.");
        }
    }



    @DeleteMapping("photo/delete/{photoId}/{userId}")
    public ResponseEntity<String> deletePhoto(
        @PathVariable int photoId, @PathVariable int userId) throws Exception {
        try {
            storyService.deletePhoto(photoId, userId);
            return ResponseEntity.ok("첨부사진 제거가 성공하였습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
