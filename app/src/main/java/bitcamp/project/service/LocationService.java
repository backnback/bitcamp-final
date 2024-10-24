package bitcamp.project.service;

import bitcamp.project.vo.Location;

public interface LocationService {

  Location findByLocation(String firstName, String secondName) throws Exception;

}
