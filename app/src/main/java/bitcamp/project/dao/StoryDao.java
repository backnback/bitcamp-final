package bitcamp.project.dao;

import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StoryDao {

    boolean insert(Story story) throws Exception;

    List<Story> findAll() throws Exception;

    List<Story> findAllByUserId(int userId) throws Exception;

    List<Story> findAllByMyLike(int userId) throws Exception;

    Story findByStoryId(int storyId) throws Exception;

    boolean update(Story story) throws Exception;

    boolean delete(int storyId) throws Exception;

}
