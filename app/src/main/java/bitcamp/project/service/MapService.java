package bitcamp.project.service;

import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.vo.Location;

import java.util.List;

public interface MapService {
    List<ProvinceDTO> getProvincesList(int id) throws Exception;

    List<CityDTO> getCitiesList(int userId, int locationId) throws Exception;

    List<StoryListDTO> storyListByLocationId(int userId, int provinceId, int cityId) throws Exception;

    Location getLocationById(int provinceId, int cityId) throws Exception;
}
