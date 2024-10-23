package bitcamp.project.vo;

import lombok.Data;

import java.sql.Date;

@Data
public class Story {

  private int id;
  private User user;
  private Location location;
  private String title;
  private Date travelDate;
  private Date createDate;
  private String locationDetail;
  private String content;
  private boolean share;

}
