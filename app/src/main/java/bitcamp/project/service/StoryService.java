package bitcamp.project.service;

import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.vo.AttachedFile;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;

import java.util.List;

public interface StoryService {

    void add(Story story) throws Exception;

    List<Story> list() throws Exception;

    Story get(int id) throws Exception;

    void update(Story story) throws Exception;

    void delete(int id) throws Exception;

    void addPhotos(List<Photo> photos) throws Exception;

    List<Photo> getPhotos(int id) throws Exception;

    Photo getPhoto(int id) throws Exception;

    void deletePhoto(int id) throws Exception;

    List<StoryListDTO> listAllShareStories(int userId) throws Exception;

    StoryViewDTO viewShareStory(int storyId) throws Exception;

    List<StoryListDTO> listAllMyStories(int userId) throws Exception;

    StoryViewDTO viewMyStory(int storyId, int userId) throws Exception;

    List<StoryListDTO> listAllMyLikeStories(int userId) throws Exception;
}
