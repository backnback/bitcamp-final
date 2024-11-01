package bitcamp.project.dto;

import bitcamp.project.vo.Story;
import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;


@Data
public class AddStoryRequestDTO {

  private String title;
  private Date travelDate;
  private String locationDetail;
  private String content;
  private boolean share;
  private String firstName;
  private String secondName;
  private int userId;

}
