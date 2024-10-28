package bitcamp.project.dao;

import bitcamp.project.vo.Location;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoryDao {

    boolean insert(Story story) throws Exception;

    List<Story> findAll() throws Exception;

    Story findBy(int id) throws Exception;

    boolean update(Story story) throws Exception;

    boolean delete(int id) throws Exception;

    boolean deleteLikes(int id) throws Exception;

    boolean deletePhotos(int id) throws Exception;

    Location findByLocation(String firstName, String secondName) throws Exception;

    Photo getPhoto(int id) throws Exception;

    List<Photo> getPhotos(int id) throws Exception;

    boolean deletePhoto(int id) throws Exception;

    boolean insertPhoto(Photo photo) throws Exception;
}
