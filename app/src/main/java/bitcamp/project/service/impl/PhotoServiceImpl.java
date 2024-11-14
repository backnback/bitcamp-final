package bitcamp.project.service.impl;

import bitcamp.project.dao.PhotoDao;
import bitcamp.project.service.PhotoService;
import bitcamp.project.vo.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

  private final PhotoDao photoDao;


  @Transactional
  @Override
  public void addPhotos(List<Photo> photos) throws Exception {
    for (Photo photo : photos) {
      photoDao.insertPhoto(photo);
    }
  }


  @Override
  public List<Photo> getPhotosByStoryId(int storyId) throws Exception {
    return photoDao.getPhotos(storyId);
  }


  @Override
  public Photo getPhoto(int photoId) throws Exception {
    return photoDao.getPhoto(photoId);
  }


  @Override
  public void deletePhoto(int photoId) throws Exception {
    photoDao.deletePhoto(photoId);
  }


  @Override
  public void deletePhotos(int storyId) throws Exception {
    photoDao.deletePhotos(storyId);
  }


  @Transactional
  @Override
  public void updatePhotos(List<Photo> photos) throws Exception {
    for (Photo photo : photos) {
      photoDao.updatePhoto(photo);
    }
  }


}
