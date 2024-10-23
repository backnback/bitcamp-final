package bitcamp.project.service;

import bitcamp.project.vo.Story;

import java.util.List;

public interface StoryService {

    void add(Story story) throws Exception;

    List<Story> list() throws Exception;

    Story get(int id) throws Exception;

    void delete(int id) throws Exception;
}
