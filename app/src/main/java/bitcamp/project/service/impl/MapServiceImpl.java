package bitcamp.project.service.impl;

import bitcamp.project.dao.MapDao;
import bitcamp.project.dao.PhotoDao;
import bitcamp.project.dto.CityDTO;
import bitcamp.project.dto.ProvinceDTO;
import bitcamp.project.dto.StoryListDTO;
import bitcamp.project.mapper.StoryMapper;
import bitcamp.project.service.LikeService;
import bitcamp.project.service.MapService;
import bitcamp.project.service.PhotoService;
import bitcamp.project.vo.Location;
import bitcamp.project.vo.Photo;
import bitcamp.project.vo.Story;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {
    private final MapDao mapDao;
    private final StoryMapper storyMapper;
    private final PhotoService photoService;
    private final LikeService likeService;

    @Override
    public List<ProvinceDTO> getProvincesList(int id) throws Exception {
        return mapDao.findAllProvinceAndCount(id);
    }

    @Override
    public List<CityDTO> getCitiesList(int userId, int locationId) throws Exception{
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locationId", locationId);

        return mapDao.findCityPhotoByLocationId(map);
    }

    @Override
    public List<StoryListDTO> storyListByLocationId(int userId, int provinceId, int cityId) throws Exception {

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        String locationId = String.valueOf(provinceId) + cityId;
        System.out.println(locationId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("locationId", Integer.valueOf(locationId));

        for (Story story : mapDao.storyListByLocationId(map)) {
            // story와 userId를 사용하여 스토리, 사진정보, 좋아요 정보로
            // StoryListDTO를 만들어서 List에 담는다.
            storyListDTOs.add(convertToStoryListDTO(story, userId));
        }

        return storyListDTOs;
    }

    private StoryListDTO convertToStoryListDTO(Story story, int userId) throws Exception {
        StoryListDTO storyListDTO = storyMapper.toStoryListDTO(story);

        Photo mainPhoto = photoService.getPhotosByStoryId(story.getId())
                .stream()
                .filter(Photo::isMainPhoto)
                .findFirst()
                .orElse(null);

        if (mainPhoto != null) {
            storyListDTO.setMainPhoto(storyMapper.toPhotoDTO(mainPhoto));
        }

        boolean likeStatus = likeService.getStatus(story.getId(), userId);
        storyListDTO.setLikeStatus(likeStatus);
        storyListDTO.setLikeCount(likeService.countLikes(story.getId(), likeStatus));

        return storyListDTO;
    }

    @Override
    public Location getLocationById(int provinceId, int cityId) throws Exception {
        String locationId = String.valueOf(provinceId + cityId);

        return mapDao.getLocationById(Integer.parseInt(locationId));
    }
}
