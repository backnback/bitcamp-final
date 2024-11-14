package bitcamp.project.dao;

import bitcamp.project.vo.Photo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotoDao {

  boolean insertPhoto(Photo photo) throws Exception;

  Photo getPhoto(int photoId) throws Exception;

  List<Photo> getPhotos(int storyId) throws Exception;

  boolean deletePhoto(int photoId) throws Exception;

  boolean deletePhotos(int storyId) throws Exception;

  void updatePhoto(Photo photo) throws Exception;
}
