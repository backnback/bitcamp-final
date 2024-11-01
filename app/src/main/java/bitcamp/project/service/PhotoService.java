package bitcamp.project.service;

import bitcamp.project.vo.Photo;

import java.util.List;

public interface PhotoService {

  List<Photo> getPhotos(int id) throws Exception;

  Photo getPhoto(int id) throws Exception;

}
