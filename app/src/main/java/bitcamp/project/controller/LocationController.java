package bitcamp.project.controller;

import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.service.LocationService;
import bitcamp.project.vo.Location;
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
@RequestMapping("/location")
public class LocationController {

  private final LocationService locationService;

  @GetMapping("list")
  public ResponseEntity<?> getFirstNames() throws Exception {
    try {
      List<String> list = locationService.getFirstNames();
      return ResponseEntity.ok(list);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }


  @GetMapping("list/{firstName}")
  public ResponseEntity<?> getSecondNames(@PathVariable String firstName) throws Exception {
    try {
      List<Location> list = locationService.getSecondNames(firstName);
      return ResponseEntity.ok(list);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
