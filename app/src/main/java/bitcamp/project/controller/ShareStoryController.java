package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.service.StoryService;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/share-story")
public class ShareStoryController {

    private final StoryService storyService;

    @GetMapping("list")
    public ResponseEntity<?> list(@LoginUser User loginUser) throws Exception {
        try {
            List<StoryListDTO> storyListDTOs = storyService.listAllShareStories(loginUser.getId());
            return ResponseEntity.ok(storyListDTOs);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("view/{storyId}")
    public ResponseEntity<?> view(@PathVariable  int storyId) throws Exception {
        try {
            StoryViewDTO storyViewDTO = storyService.viewShareStory(storyId);
            return ResponseEntity.ok(storyViewDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}





