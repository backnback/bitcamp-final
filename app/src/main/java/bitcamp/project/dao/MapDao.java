package bitcamp.project.dao;

import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.vo.Story;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Mapper
public interface MapDao {
    List<ProvinceDTO> findAllProvinceAndCount(int id);

    List<CityDTO> findCityPhotoByLocationId(HashMap<String, Object> map);

    List<Story> storyListByLocationId(HashMap<String, Object> map);
}
