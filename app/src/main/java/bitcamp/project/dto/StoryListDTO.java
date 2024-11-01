package bitcamp.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class StoryListDTO {

  private int storyId;
  private String title;
  private Date travelDate;
  private String locationDetail;
  private String content;
  private boolean share;
  private String userNickname;
  private String userPath;
  private String locationFirstName;
  private String locationSecondName;
  private PhotoDTO mainPhoto;
  private int likeCount;
  private boolean likeStatus;
}
