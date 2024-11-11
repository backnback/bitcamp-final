package bitcamp.project.dto;

import bitcamp.project.vo.Story;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
public class ProvinceDTO {
    private int id;
    private String province;
    private int count;
}