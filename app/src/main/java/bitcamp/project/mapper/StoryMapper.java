package bitcamp.project.mapper;

import bitcamp.project.dto.PhotoDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.dto.StoryViewDTO;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StoryMapper {

  @Mapping(source = "id", target = "storyId")
  @Mapping(source = "user.nickname", target = "userNickname")
  @Mapping(source = "user.path", target = "userPath")
  @Mapping(source = "location.firstName", target = "locationFirstName")
  @Mapping(source = "location.secondName", target = "locationSecondName")
  @Mapping(target = "mainPhoto", ignore = true)
  @Mapping(target = "likeCount", ignore = true)
  @Mapping(target = "likeStatus", ignore = true)
  StoryListDTO toStoryListDTO(Story story) throws Exception;

  @Mapping(source = "id", target = "storyId")
  @Mapping(source = "location.firstName", target = "locationFirstName")
  @Mapping(source = "location.secondName", target = "locationSecondName")
  @Mapping(target = "mainPhotoIndex", ignore = true)
  @Mapping(target = "photos", ignore = true)
  StoryViewDTO toStoryViewDTO(Story story) throws Exception;

  PhotoDTO toPhotoDTO(Photo photo) throws Exception;
}
