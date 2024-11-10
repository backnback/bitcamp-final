package bitcamp.project.service;

import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;

import java.util.List;

public interface MapService {
    List<ProvinceDTO> getProvincesList(int id);

    List<CityDTO> getCitiesList(int userId, int locationId);
}
