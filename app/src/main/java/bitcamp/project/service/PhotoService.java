package bitcamp.project.service;

import bitcamp.project.vo.Photo;

import java.util.List;

public interface PhotoService {

  void addPhotos(List<Photo> photos) throws Exception;

  List<Photo> getPhotosByStoryId(int storyId) throws Exception;

  Photo getPhoto(int photoId) throws Exception;

  void deletePhoto(int photoId) throws Exception;

  void deletePhotos(int storyId) throws Exception;
}
