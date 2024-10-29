package bitcamp.project.service.impl;

import bitcamp.project.dao.StoryDao;
import bitcamp.project.service.StoryService;
import bitcamp.project.vo.AttachedFile;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryDao storyDao;

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
    public Photo getPhoto(int id) throws Exception {
        return storyDao.getPhoto(id);
    }

    @Override
    public void deletePhoto(int id) throws Exception {
        storyDao.deletePhoto(id);
    }

}
