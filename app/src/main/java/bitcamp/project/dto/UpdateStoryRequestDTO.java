package bitcamp.project.dto;

import bitcamp.project.vo.Location;
import bitcamp.project.vo.Story;
import bitcamp.project.vo.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;


@Data
public class UpdateStoryRequestDTO {

  private String title;
  private Date travelDate;
  private String locationDetail;
  private String content;
  private boolean share;
  private String firstName;
  private String secondName;
  private int oldStoryId;
  private int userId;

  // VO로 변환하는 메서드
  public Story toStory(User user, Location location) {

    Story story = new Story();
    story.setTitle(title);
    story.setTravelDate(travelDate);
    story.setLocationDetail(locationDetail);
    story.setContent(content);
    story.setShare(share);

    story.setUser(user);
    story.setLocation(location);

    return story;
  }

}
