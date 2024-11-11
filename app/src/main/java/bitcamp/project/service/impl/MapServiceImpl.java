package bitcamp.project.service.impl;

import bitcamp.project.dao.MapDao;
import bitcamp.project.dao.PhotoDao;
import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;
import bitcamp.project.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final MapDao mapDao;
    private final PhotoDao photoDao;

    public List<ProvinceDTO> getProvincesList(int id) {
        return mapDao.findAllProvinceAndCount(id);
    }

    public List<CityDTO> getCitiesList(int userId, int locationId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locationId", locationId / 1000);
        for (CityDTO cityDTO : mapDao.findCityPhotoByLocationId(map)){
            System.out.println(cityDTO.getId());
            System.out.println(cityDTO.getProvince());
            System.out.println(cityDTO.getCity());
            System.out.println(cityDTO.getStoryId());
            System.out.println(cityDTO.getMainPhotoPath());
        }
        return mapDao.findCityPhotoByLocationId(map);
    }


}
