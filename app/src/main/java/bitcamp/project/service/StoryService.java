package bitcamp.project.service;

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

    Photo getPhoto(int id) throws Exception;

    void deletePhoto(int id) throws Exception;

}
