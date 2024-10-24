package bitcamp.project.service.impl;

import bitcamp.project.dao.StoryDao;
import bitcamp.project.service.LocationService;
import bitcamp.project.vo.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

  @Autowired
  private StoryDao storyDao;

  @Override
  public Location findByLocation(String firstName, String secondName) throws Exception {
    return storyDao.findByLocation(firstName, secondName);
  }

}
