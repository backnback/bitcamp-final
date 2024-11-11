package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.service.MapService;
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
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {
    private final MapService mapService;

    @GetMapping
    public ResponseEntity<?> getProvincesList(@LoginUser User loginUser) {
        try {
            List<ProvinceDTO> provinceDTOs = mapService.getProvincesList(loginUser.getId());
            return ResponseEntity.ok(provinceDTOs);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("story/{provinceId}")
    public ResponseEntity<?> getCityList(@LoginUser User loginUser, @PathVariable String provinceId) throws Exception {
        List<CityDTO> cityDTOS = mapService.getCitiesList(loginUser.getId(), Integer.parseInt(provinceId));
        return ResponseEntity.ok(cityDTOS);
    }

    @GetMapping("story/{provinceId}/{cityId}")
    public ResponseEntity<?> getStoryList(@LoginUser User loginUser, @PathVariable int provinceId, @PathVariable int cityId) throws Exception {
        System.out.println("진입");
        System.out.println(provinceId);
        System.out.println(cityId);
        List<StoryListDTO> storyListDTOS = mapService.storyListByLocationId(loginUser.getId(), provinceId, cityId);
        return ResponseEntity.ok(storyListDTOS);
    }
}
