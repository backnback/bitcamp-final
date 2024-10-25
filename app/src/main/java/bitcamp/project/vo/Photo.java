package bitcamp.project.vo;

import lombok.Data;

@Data
public class Photo {

  private int id;
  private int storyId;
  private boolean mainPhoto;
  private String path;

}
