package bitcamp.project.controller;

import bitcamp.project.service.LikeService;
import bitcamp.project.service.StoryService;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/share-story")
public class ShareStoryController {

    private final StoryService storyService;
    private final LikeService likeService;

    @GetMapping("list")
    public ResponseEntity<List<Map<String, Object>>> list() throws Exception {

        List<Map<String, Object>> responseList = new ArrayList<>();

        // Story 1개 + Main 사진 1개
        for (Story story : storyService.list()) {
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


    @GetMapping("view/{storyId}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable  int storyId) throws Exception {
        Story story = storyService.get(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        } else if (!story.isShare()) {
            throw new Exception("공개된 스토리가 아닙니다.");
        }

        List<Photo> photos = storyService.getPhotos(storyId);

        // Story와 함께 Photo 데이터 보내기
        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
    }

}
