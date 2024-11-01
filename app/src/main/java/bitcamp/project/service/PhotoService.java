package bitcamp.project.service;

import bitcamp.project.vo.Photo;

import java.util.List;

public interface PhotoService {

  List<Photo> getPhotosByStoryId(int storyId) throws Exception;

  Photo getPhoto(int photoId) throws Exception;

  void addPhotos(List<Photo> photos) throws Exception;

  void deletePhoto(int photoId) throws Exception;
}
