package bitcamp.project.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class StoryViewDTO {

  private int storyId;
  private String title;
  private Date travelDate;
  private String locationDetail;
  private String content;
  private boolean share;
  private int mainPhotoIndex;
  private String locationFirstName;
  private String locationSecondName;
  private List<PhotoDTO> photos;
}
