package bitcamp.project.dto;

import bitcamp.project.vo.Location;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
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
  private int mainPhotoIndex;
  private String firstName;
  private String secondName;
  private User loginUser;


  // VO로 변환하는 메서드
  public Story toStory(Location location) {

    Story story = new Story();
    story.setTitle(title);
    story.setTravelDate(travelDate);
    story.setLocationDetail(locationDetail);
    story.setContent(content);
    story.setShare(share);
    story.setUser(loginUser);

    story.setLocation(location);

    return story;
  }

}
