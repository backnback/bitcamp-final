package bitcamp.project.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class Like {

  private int userId;
  private int storyId;
  private Date likeDate;
  private boolean view;

}
