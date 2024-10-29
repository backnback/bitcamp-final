package bitcamp.project.controller;

import bitcamp.project.service.LocationService;
import bitcamp.project.vo.Location;
import lombok.RequiredArgsConstructor;
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
  public List<String> getFirstNames() throws Exception {
    return locationService.getFirstNames();
  }

  @GetMapping("list/{firstName}")
  public List<Location> getSecondNames(@PathVariable String firstName) throws Exception {
    return locationService.getSecondNames(firstName);
  }


}
