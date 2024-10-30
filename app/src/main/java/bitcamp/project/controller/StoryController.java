package bitcamp.project.controller;

import bitcamp.project.service.*;
import bitcamp.project.vo.*;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/story")
public class StoryController {

    private final StoryService storyService;

    @GetMapping("share/{storyId}")
    public Story share(@PathVariable int storyId, @RequestParam int userId) throws Exception {
        Story story = storyService.get(storyId);
        if (story == null) {
            throw new Exception("스토리 정보 없음");
        }

        if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        story.setShare(!story.isShare());
        storyService.update(story);
        return story;
    }

}
