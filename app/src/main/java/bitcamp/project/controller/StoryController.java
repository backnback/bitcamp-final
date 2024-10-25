package bitcamp.project.controller;

import bitcamp.project.service.LocationService;
import bitcamp.project.service.StoryService;
import bitcamp.project.service.UserService;
import bitcamp.project.service.impl.FileServiceImpl;
import bitcamp.project.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Map<String, Object>>> list() throws Exception {

        List<Map<String, Object>> responseList = new ArrayList<>();

        // Story 1개 + Main 사진 1개
        for (Story story : storyService.list()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("story", story);

            for (Photo photo : storyService.getPhotos(story.getId())) {
                if (photo.isMainPhoto()) {
                    map.put("mainPhoto", photo);
                }
            }

            responseList.add(map);
        }

        return ResponseEntity.ok(responseList);
    }


    @GetMapping("view/{id}")
    public ResponseEntity<Map<String, Object>> view(@PathVariable  int id) throws Exception {
        Story story = storyService.get(id);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }

        List<Photo> photos = storyService.getPhotos(id);

        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
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
        try {
            FileServiceImpl fileServiceImpl = new FileServiceImpl();
            fileServiceImpl.deletePhotos(photo.getPath());
        } catch (Exception e) {
            throw new Exception("파일 삭제 실패로 인한 DB 삭제 중단");
        }

        // Photo DB 삭제
        storyService.deletePhoto(id);
    }

}
