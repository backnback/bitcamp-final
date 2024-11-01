package bitcamp.project.dto;

import bitcamp.project.vo.Story;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UpdateStoryRequestDTO {

  private Story story;
  private MultipartFile[] files;
  private int storyId;
  private String firstName;
  private String secondName;
  private int userId;

}
