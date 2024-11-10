package bitcamp.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class CityDTO {
    private int id;
    private String city;
    private List<StoryListDTO> stories;
}
