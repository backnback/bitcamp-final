package bitcamp.project.service;

import bitcamp.project.dto.*;
import bitcamp.project.vo.AttachedFile;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StoryService {

    ResponseEntity<Map<String, Object>> add(AddStoryRequestDTO addStoryRequestDTO, MultipartFile[] files) throws Exception;

    ResponseEntity<Map<String, Object>> update(UpdateStoryRequestDTO updateStoryRequestDTO, MultipartFile[] files, String photosJson) throws Exception;

    void delete(int storyId, int userId) throws Exception;

    void removePhoto(int photoId, int userId) throws Exception;

    List<StoryListDTO> listAllStories(int userId, String title, String userNickname, boolean share, String sortBy, int limit) throws Exception;

    StoryViewDTO viewStory(int storyId, int userId, boolean share) throws Exception;

    List<StoryListDTO> listAllMyLikeStories(int userId) throws Exception;

    Story changeShare(int storyId, int userId) throws Exception;

    void changeShare(BatchUpdateRequestDTO batchUpdateRequestDTO, int userId) throws Exception;

    List<Story> getStories(int userId)throws Exception;

    boolean hasMoreStories(int userId, String title, String userNickname, boolean share, int currentSize) throws Exception;

    int countStories(int userId, String title, String userNickname, boolean share) throws Exception;

}
