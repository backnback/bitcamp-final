package bitcamp.project.controller;

import bitcamp.project.service.LocationService;
import bitcamp.project.service.StoryService;
import bitcamp.project.service.UserService;
import bitcamp.project.service.impl.FileServiceImpl;
import bitcamp.project.vo.*;
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


    @GetMapping("share/{id}")
    public Story share(@PathVariable int id) throws Exception {
        Story story = storyService.get(id);
        if (story == null) {
            throw new Exception("스토리 정보 없음");
        }

        story.setShare(!story.isShare());
        storyService.update(story);
        return story;
    }


    @DeleteMapping("photo/delete/{id}")
    public void photoDelete(@PathVariable int id) throws Exception {

        // 삭제할 Photo 가져오기
        Photo photo = storyService.getPhoto(id);
        if (photo == null) {
            throw new Exception("없는 사진입니다.");
        }

        // Story에서 로그인 사용자 정보를 가져와서 권한 파악 시 필요 (지금 X)
//        Story story = storyService.get(photo.getStoryId());
//        if (story == null) {
//            throw new Exception("없는 스토리입니다.");
//        }

        // Photo 파일 삭제
        FileServiceImpl fileServiceImpl = new FileServiceImpl();
        fileServiceImpl.deletePhotos(photo.getPath());

        // Photo DB 삭제
        storyService.deletePhoto(id);
    }

}
