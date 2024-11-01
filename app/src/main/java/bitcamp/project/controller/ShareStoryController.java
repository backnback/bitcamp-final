package bitcamp.project.controller;

import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.service.StoryService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("list/{userId}")
    public List<StoryListDTO> list(@PathVariable int userId) throws Exception {
        return storyService.listAllShareStories(userId);
    }


    @GetMapping("view/{storyId}")
    public StoryViewDTO view(@PathVariable  int storyId) throws Exception {
        return storyService.viewShareStory(storyId);
    }
}





