package bitcamp.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class CityDTO {
    private int id;
    private String province;
    private String city;
    private int storyId;
    private String mainPhotoPath;
}
