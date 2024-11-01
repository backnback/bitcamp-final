package bitcamp.project.service.impl;

import bitcamp.project.dao.LikeDao;
import bitcamp.project.dao.StoryDao;
import bitcamp.project.dao.UserDao;
import bitcamp.project.dto.PhotoDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.mapper.StoryMapper;
import bitcamp.project.service.LikeService;
import bitcamp.project.service.StoryService;
import bitcamp.project.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final LikeDao likeDao;
    private final LikeService likeService;
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
    public List<Story> myList(int userId) throws Exception {
        return storyDao.findAllByUserId(userId);
    }

    @Override
    public List<Story> findAllByMyLike(int userId) throws Exception {
        return storyDao.findAllByMyLike(userId);
    }

    @Override
    public Story get(int id) throws Exception {
        return storyDao.findBy(id);
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

}
