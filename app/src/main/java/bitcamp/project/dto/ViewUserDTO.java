package bitcamp.project.dto;

import lombok.Data;

@Data
public class ViewUserDTO {
    private String nickname;
    private String filename;
    private byte[] file;
}
