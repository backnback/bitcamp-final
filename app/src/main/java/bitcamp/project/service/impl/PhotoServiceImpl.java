package bitcamp.project.service.impl;

import bitcamp.project.dao.StoryDao;
import bitcamp.project.service.PhotoService;
import bitcamp.project.vo.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

  private final StoryDao storyDao;

  @Override
  public List<Photo> getPhotosByStoryId(int storyId) throws Exception {
    return storyDao.getPhotos(storyId);
  }


  @Override
  public Photo getPhoto(int photoId) throws Exception {
    return storyDao.getPhoto(photoId);
  }


  @Transactional
  @Override
  public void addPhotos(List<Photo> photos) throws Exception {
    for (Photo photo : photos) {
      storyDao.insertPhoto(photo);
    }
  }


  @Override
  public void deletePhoto(int photoId) throws Exception {
    storyDao.deletePhoto(photoId);
  }


}
