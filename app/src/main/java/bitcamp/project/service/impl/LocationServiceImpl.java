package bitcamp.project.service.impl;

import bitcamp.project.dao.LocationDao;
import bitcamp.project.dao.StoryDao;
import bitcamp.project.service.LocationService;
import bitcamp.project.vo.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final StoryDao storyDao;
  private final LocationDao locationDao;


  @Override
  public Location findByNames(String firstName, String secondName) throws Exception {
    return storyDao.findByLocation(firstName, secondName);
  }


  @Override
  public List<Location> getSecondNames(String firstName) throws Exception {
    return locationDao.findAllByFirstName(firstName);
  }

  @Override
  public List<String> getFirstNames() throws Exception {
    return locationDao.findAllFirstNames();
  }

}
