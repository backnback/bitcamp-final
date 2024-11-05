package bitcamp.project.service;

import bitcamp.project.dto.AddStoryRequestDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.dto.UpdateStoryRequestDTO;
import bitcamp.project.vo.AttachedFile;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface StoryService {

    ResponseEntity<Map<String, Object>> add(AddStoryRequestDTO addStoryRequestDTO, MultipartFile[] files) throws Exception;

    ResponseEntity<Map<String, Object>> update(UpdateStoryRequestDTO updateStoryRequestDTO, MultipartFile[] files) throws Exception;

    void delete(int storyId, int userId) throws Exception;

    void removePhoto(int photoId, int userId) throws Exception;

    List<StoryListDTO> listAllShareStories(int userId) throws Exception;

    StoryViewDTO viewShareStory(int storyId) throws Exception;

    List<StoryListDTO> listAllMyStories(int userId) throws Exception;

    StoryViewDTO viewMyStory(int storyId, int userId) throws Exception;

    List<StoryListDTO> listAllMyLikeStories(int userId) throws Exception;

    Story changeShare(int storyId, int userId) throws Exception;
}
