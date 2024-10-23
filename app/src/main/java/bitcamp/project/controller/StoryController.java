package bitcamp.project.controller;

import bitcamp.project.service.StoryService;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/story")
public class StoryController {

    @Autowired
    StoryService storyService;

    @GetMapping("list")
    public List<Story> list() throws Exception {
        return storyService.list();
    }


    @GetMapping("view/{id}")
    public Story view(@PathVariable  int id) throws Exception {
        Story story = storyService.get(id);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }

        return story;
    }

    @GetMapping("form")
    public void form() {
    }


    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable int id) throws Exception{

        Story story = storyService.get(id);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }

        storyService.delete(id);
    }

    @PostMapping("add")
    public void add(Story story)throws Exception{
        storyService.add(story);
    }


}
