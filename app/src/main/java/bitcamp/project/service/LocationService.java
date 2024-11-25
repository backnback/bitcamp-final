package bitcamp.project.service;

import bitcamp.project.vo.Location;

import java.util.List;

public interface LocationService {

  Location findByFullName(String firstName, String secondName) throws Exception;

  List<Location> getSecondNames(String firstName) throws Exception;

  List<String> getFirstNames() throws Exception;

  Location getLocationById(int id) throws Exception;

  List<Location> getProvinceAndIdList() throws Exception;

  List<Location> getCitiesList(int id) throws Exception;
}
