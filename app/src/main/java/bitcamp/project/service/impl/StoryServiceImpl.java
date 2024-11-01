package bitcamp.project.service.impl;

import bitcamp.project.dao.LikeDao;
import bitcamp.project.dao.StoryDao;
import bitcamp.project.dto.PhotoDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.mapper.StoryMapper;
import bitcamp.project.service.LikeService;
import bitcamp.project.service.StoryService;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryDao storyDao;
    private final LikeService likeService;
    private final UserService userService;
    private final StoryMapper storyMapper;


    @Transactional
    @Override
    public void add(Story story) throws Exception {
        storyDao.insert(story);
    }

    @Override
    public List<Story> list() throws Exception {
        return storyDao.findAll();
    }

    @Override
    public Story get(int id) throws Exception {
        return storyDao.findByStoryId(id);
    }

    @Override
    public void update(Story story) throws Exception {
        storyDao.update(story);
    }

    @Override
    @Transactional
    public void delete(int id) throws Exception {
        storyDao.deleteLikes(id);
        storyDao.deletePhotos(id);
        storyDao.delete(id);
    }

    @Transactional
    @Override
    public void addPhotos(List<Photo> photos) throws Exception {
        for (Photo photo : photos) {
            storyDao.insertPhoto(photo);
        }
    }

    @Override
    public List<Photo> getPhotos(int id) throws Exception {
        return storyDao.getPhotos(id);
    }

    @Override
    public Photo getPhoto(int storyId) throws Exception {
        return storyDao.getPhoto(storyId);
    }

    @Override
    public void deletePhoto(int id) throws Exception {
        storyDao.deletePhoto(id);
    }


    @Override
    public List<StoryListDTO> listAllShareStories(int userId) throws Exception {
        // list : 공유 허용된 모든 스토리 목록   (로그인 ID에 따라 좋아요 상태 변화)

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAll()) {
            if (!story.isShare()) {
                continue;
            }

            StoryListDTO storyListDTO = storyMapper.toStoryListDTO(story);

            Photo mainPhoto = storyDao.getPhotos(story.getId())
                .stream()
                .filter(Photo::isMainPhoto)
                .findFirst()
                .orElse(null);

            if (mainPhoto != null) {
                storyListDTO.setMainPhoto(storyMapper.toPhotoDTO(mainPhoto));
            }

            storyListDTO.setLikeCount(likeService.countLikes(story.getId()));
            storyListDTO.setLikeStatus(likeService.getStatus(story.getId(), userId));

            storyListDTOs.add(storyListDTO);
        }

        return storyListDTOs;
    }


    @Override
    public StoryViewDTO viewShareStory(int storyId) throws Exception {

        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        } else if (!story.isShare()) {
            throw new Exception("공개된 스토리가 아닙니다.");
        }

        StoryViewDTO storyViewDTO = storyMapper.toStoryViewDTO(story);

        List<Photo> photos = storyDao.getPhotos(storyId);
        if (photos == null) {
            throw new Exception("사진이 존재하지 않습니다.");
        }

        List<PhotoDTO> photoDTOS = new ArrayList<>();
        for (Photo photo : photos) {
            photoDTOS.add(storyMapper.toPhotoDTO(photo));
        }

        storyViewDTO.setPhotos(photoDTOS);

        return storyViewDTO;
    }


    @Override
    public List<StoryListDTO> listAllMyStories(int userId) throws Exception {
        // list : 내가 올린 모든 스토리 목록  (공유 여부 상관 없음)
        // (로그인 ID에 따라 좋아요 상태 변화)

        User loginUser = userService.findUser(userId);
        if (loginUser == null) {
            throw new Exception("유효하지 않는 사용자입니다.");
        }

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAllByUserId(userId)) {
            StoryListDTO storyListDTO = storyMapper.toStoryListDTO(story);

            Photo mainPhoto = storyDao.getPhotos(story.getId())
                .stream()
                .filter(Photo::isMainPhoto)
                .findFirst()
                .orElse(null);

            if (mainPhoto != null) {
                storyListDTO.setMainPhoto(storyMapper.toPhotoDTO(mainPhoto));
            }

            storyListDTO.setLikeCount(likeService.countLikes(story.getId()));
            storyListDTO.setLikeStatus(likeService.getStatus(story.getId(), userId));

            storyListDTOs.add(storyListDTO);
        }

        return storyListDTOs;
    }

    @Override
    public StoryViewDTO viewMyStory(int storyId, int userId) throws Exception {

        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        } else if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        StoryViewDTO storyViewDTO = storyMapper.toStoryViewDTO(story);


        List<Photo> photos = storyDao.getPhotos(story.getId());
        if (photos == null) {
            throw new Exception("사진이 존재하지 않습니다.");
        }

        List<PhotoDTO> photoDTOS = new ArrayList<>();
        for (Photo photo : photos) {
            photoDTOS.add(storyMapper.toPhotoDTO(photo));
        }

        storyViewDTO.setPhotos(photoDTOS);

        return storyViewDTO;
    }

    @Override
    public List<StoryListDTO> listAllMyLikeStories(int userId) throws Exception {
        // list : 내가 좋아요한 스토리 목록  (공개임 경우만)
        // (로그인 ID에 따라 좋아요 상태 변화)

        User loginUser = userService.findUser(userId);
        if (loginUser == null) {
            throw new Exception("유효하지 않는 사용자입니다.");
        }

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAllByMyLike(userId)) {
            if (!story.isShare()) {
                continue;
            }
            StoryListDTO storyListDTO = storyMapper.toStoryListDTO(story);

            Photo mainPhoto = storyDao.getPhotos(story.getId())
                .stream()
                .filter(Photo::isMainPhoto)
                .findFirst()
                .orElse(null);

            if (mainPhoto != null) {
                storyListDTO.setMainPhoto(storyMapper.toPhotoDTO(mainPhoto));
            }

            storyListDTO.setLikeCount(likeService.countLikes(story.getId()));
            storyListDTO.setLikeStatus(likeService.getStatus(story.getId(), userId));

            storyListDTOs.add(storyListDTO);
        }

        return storyListDTOs;
    }

}
