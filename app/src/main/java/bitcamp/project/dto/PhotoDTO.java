package bitcamp.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PhotoDTO {

  private int id;
  private int storyId;
  private boolean mainPhoto;
  private String path;

}
