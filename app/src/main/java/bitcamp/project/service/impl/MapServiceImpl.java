package bitcamp.project.service.impl;

import bitcamp.project.dao.MapDao;
import bitcamp.project.dto.ProvinceDTO;
import bitcamp.project.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final MapDao mapDao;

    public List<ProvinceDTO> getProvincesList(int id) {
        return mapDao.findAllProvinceAndCount(id);
    }
}
