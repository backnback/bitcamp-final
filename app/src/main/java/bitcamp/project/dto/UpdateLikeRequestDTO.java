package bitcamp.project.dto;

import lombok.Data;


@Data
public class UpdateLikeRequestDTO {

  private int storyId;
  private String action;

}
