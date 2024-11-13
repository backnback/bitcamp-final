package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.service.MapService;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping
    public ResponseEntity<?> getProvincesList(@LoginUser User loginUser) {
        try {
            List<ProvinceDTO> provinceDTOs = mapService.getProvincesList(loginUser.getId());
            return ResponseEntity.ok(provinceDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("story/{provinceId}")
    public ResponseEntity<?> getCityList(@LoginUser User loginUser, @PathVariable String provinceId) throws Exception {
        try {
            List<CityDTO> cityDTOS = mapService.getCitiesList(loginUser.getId(), Integer.parseInt(provinceId));
            return ResponseEntity.ok(cityDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("story/{provinceId}/{cityId}")
    public ResponseEntity<?> getCityMainPhoto(@LoginUser User loginUser, @PathVariable int provinceId, @PathVariable int cityId) throws Exception {
        try {
            List<StoryListDTO> photoDTOS = mapService.storyListByLocationId(loginUser.getId(), provinceId, cityId);
            return ResponseEntity.ok(photoDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("story/{provinceId}/{cityId}/list")
    public ResponseEntity<?> getCityStoryList(@LoginUser User loginUser, @PathVariable int provinceId, @PathVariable int cityId) throws Exception {
        try {
            List<StoryListDTO> storyListDTOS = mapService.storyListByLocationId(loginUser.getId(), provinceId, cityId);
            return ResponseEntity.ok(storyListDTOS);
        } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("story/{provinceId}/{cityId}/form")
    public ResponseEntity<?> mapForm(@PathVariable int provinceId, @PathVariable int cityId) {
        try {
            return ResponseEntity.ok(mapService.getLocationById(provinceId, cityId));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
