package bitcamp.project.dao;

import bitcamp.project.vo.Story;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoryDao {

    boolean insert(Story story) throws Exception;

    List<Story> findAll() throws Exception;

    List<Story> findAllShareStories(
        @Param("title") String title,
        @Param("userNickname") String userNickname,
        @Param("sortByLikes") boolean sortByLikes,
        @Param("limit") int limit ) throws Exception;

    List<Story> findAllByUserId(int userId) throws Exception;

    List<Story> findAllByUserIdLimit(int userId, int limit) throws Exception;

    List<Story> findAllByUserIdLimitTest(
        @Param("userId") int userId,
        @Param("title") String title,
        @Param("sortByLikes") boolean sortByLikes,
        @Param("limit") int limit ) throws Exception;

    List<Story> findAllByUserIdAndTitle(@Param("userId") int userId, @Param("title") String title, int limit) throws Exception;

    List<Story> findAllByMyLike(int userId) throws Exception;

    Story findByStoryId(int storyId) throws Exception;

    boolean update(Story story) throws Exception;

    boolean delete(int storyId) throws Exception;

    int countShareStories(int userId, String title, String userNickname, boolean share) throws Exception;

    int countAllShareStoriesByTitle(int userId, String title, boolean share) throws Exception;

    int countAllShareStoriesByNickname(int userId, String userNickname, boolean share) throws Exception;

    int countAllMyStoriesByTitle(int userId, String title) throws Exception;

    int countAllMyStories(int userId) throws Exception;


}
