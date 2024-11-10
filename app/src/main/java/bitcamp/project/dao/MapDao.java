package bitcamp.project.dao;

import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapDao {
    List<ProvinceDTO> findAllProvinceAndCount(int id);

    List<CityDTO> findAllCityByLocationId(int id);
}
