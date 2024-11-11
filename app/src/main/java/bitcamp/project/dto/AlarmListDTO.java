package bitcamp.project.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class AlarmListDTO {

  private int storyId;
  private int userId;
  private String userEmail;
  private String userNickname;
  private String userPath;
  private String userRole;

}
