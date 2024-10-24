package bitcamp.project.controller;

import bitcamp.project.service.LocationService;
import bitcamp.project.service.StoryService;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.Location;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/story")
public class StoryController {

    @Autowired
    StoryService storyService;

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;

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


    @PostMapping("add/{firstName}/{secondName}")
    public void add(@RequestBody Story story,
        @PathVariable String firstName, @PathVariable String secondName) throws Exception {

        Location location = locationService.findByLocation(firstName, secondName);
        if (location == null) {
            throw new Exception("위치 정보 없음");
        }

        User user = userService.findUser(4);

        story.setLocation(location);
        story.setUser(user);
        storyService.add(story);
    }


    @PostMapping("update/{firstName}/{secondName}")
    public Story update(@RequestBody Story story,
        @PathVariable String firstName, @PathVariable String secondName) throws Exception {

        Location location = locationService.findByLocation(firstName, secondName);
        if (location == null) {
            throw new Exception("위치 정보 없음");
        }

        User user = userService.findUser(4);

        story.setLocation(location);
        story.setUser(user);
        storyService.update(story);

        return story;
    }


}
