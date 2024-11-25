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
  public ResponseEntity<?> getFirstNames(){
    try {
      List<String> list = locationService.getFirstNames();
      return ResponseEntity.ok(list);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("list/{firstName}")
  public ResponseEntity<?> getSecondNames(@PathVariable String firstName){
    try {
      List<Location> list = locationService.getSecondNames(firstName);
      return ResponseEntity.ok(list);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("{id}")
  public ResponseEntity<?> getLocation(@PathVariable int id){
    try {
      Location location = locationService.getLocationById(id);
      if(location == null){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      return ResponseEntity.ok(location);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("province")
  public ResponseEntity<?> getProvinceList(){
    try {
      List<Location> locations = locationService.getProvinceAndIdList();
      if(locations == null){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      return ResponseEntity.ok(locations);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("province/{id}")
  public ResponseEntity<?> getCitiesList(@PathVariable int id){
    try {
      List<Location> citiesList = locationService.getCitiesList(id);
      if(citiesList == null){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      return ResponseEntity.ok(citiesList);
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

}
