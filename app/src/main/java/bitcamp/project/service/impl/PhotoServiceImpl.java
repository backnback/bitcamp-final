package bitcamp.project.service.impl;

import bitcamp.project.dao.StoryDao;
import bitcamp.project.service.PhotoService;
import bitcamp.project.vo.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

  private final StoryDao storyDao;


  @Override
  public List<Photo> getPhotos(int storyId) throws Exception {
    return storyDao.getPhotos(storyId);
  }

  @Override
  public Photo getPhoto(int storyId) throws Exception {
    return storyDao.getPhoto(storyId);
  }

}
