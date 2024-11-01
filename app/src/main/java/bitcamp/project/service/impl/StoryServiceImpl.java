package bitcamp.project.service.impl;

import bitcamp.project.dao.LikeDao;
import bitcamp.project.dao.StoryDao;
import bitcamp.project.dto.*;
import bitcamp.project.mapper.StoryMapper;
import bitcamp.project.service.*;
import bitcamp.project.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryDao storyDao;
    private final LikeService likeService;
    private final UserService userService;
    private final LocationService locationService;
    private final StorageService storageService;
    private final PhotoService photoService;

    private final StoryMapper storyMapper;

    private String folderName = "story/";


    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> add(
        AddStoryRequestDTO addStoryRequestDTO,
        MultipartFile[] files) throws Exception {

        // 로그인 사용자
        User user = userService.findUser(addStoryRequestDTO.getUserId());
        if (user == null) {
            throw new Exception("로그인이 필요합니다.");
        }

        // 위치 정보
        Location location = locationService.findByFullName(addStoryRequestDTO.getFirstName(),
            addStoryRequestDTO.getSecondName());
        if (location == null) {
            throw new Exception("위치 정보 없음");
        }

        if (files == null || files.length == 0 || Arrays.stream(files).allMatch(file -> file.getSize() == 0)) {
            throw new Exception("사진 입력 필요");
        }

        // Story에 로그인 사용자 및 위치 정보 삽입
        Story story = addStoryRequestDTO.toStory(user, location);
        storyDao.insert(story);  // DB에 스토리 저장

        // Photo 정보
        List<Photo> photos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }

            // 첨부파일 저장
            String filename = UUID.randomUUID().toString();

            HashMap<String, Object> options = new HashMap<>();
            options.put(StorageService.CONTENT_TYPE, file.getContentType());
            storageService.upload(folderName + filename,
                file.getInputStream(),
                options);

            Photo photo = new Photo();
            photo.setStoryId(story.getId());
            photo.setMainPhoto(false);
            photo.setPath(filename);

            photos.add(photo);
        }

        photos.getFirst().setMainPhoto(true);

        // Photo DB 처리
        addPhotos(photos);

        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
    }

    @Override
    public List<Story> list() throws Exception {
        return storyDao.findAll();
    }

    @Override
    public Story get(int id) throws Exception {
        return storyDao.findByStoryId(id);
    }



    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> update(
        UpdateStoryRequestDTO updateStoryRequestDTO,
        MultipartFile[] files) throws Exception {

        Story oldStory = storyDao.findByStoryId(updateStoryRequestDTO.getOldStoryId());
        if (oldStory == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }


        // 로그인 사용자
        User user = userService.findUser(updateStoryRequestDTO.getUserId());
        if (user == null) {
            throw new Exception("로그인이 필요합니다.");
        }


        if (oldStory.getUser().getId() != updateStoryRequestDTO.getUserId()) {
            throw new Exception("접근 권한이 없습니다.");
        }

        List<Photo> oldPhotos = storyDao.getPhotos(updateStoryRequestDTO.getOldStoryId());


        // 기존 사진이 있는 경우  =>  예외 없음
        // 기존 사진이 없는 겨우
        // (1) files가 들어오지 않거나 들어와도 내부에 파일이 없는 경우  =>  예외 발생
        // (2) 유효하지 않는 (손상된) 파일  =>  예외 발생
        if ((files == null || files.length == 0) && oldPhotos.isEmpty()) {
            throw new Exception("스토리에 최소 한 개의 사진이 필요합니다.");
        }

        if (files != null && Arrays.stream(files).allMatch(file -> file.getSize() == 0) && oldPhotos.isEmpty()) {
            throw new Exception("유효하지 않는 파일입니다.");
        }


        Location location = locationService.findByFullName(updateStoryRequestDTO.getFirstName(),
            updateStoryRequestDTO.getSecondName());
        if (location == null) {
            throw new Exception("위치 정보 없음");
        }

        Story story = updateStoryRequestDTO.toStory(user, location);
        story.setId(updateStoryRequestDTO.getOldStoryId());
        storyDao.update(story);


        // Photo 정보
        List<Photo> photos = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.getSize() == 0) {
                continue;
            }

            // 첨부파일 저장
            String filename = UUID.randomUUID().toString();

            HashMap<String, Object> options = new HashMap<>();
            options.put(StorageService.CONTENT_TYPE, file.getContentType());
            storageService.upload(folderName + filename,
                file.getInputStream(),
                options);

            Photo photo = new Photo();
            photo.setStoryId(story.getId());
            photo.setMainPhoto(false);
            photo.setPath(filename);

            photos.add(photo);
        }

        // Photo DB 처리
        addPhotos(photos);

        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
    }


    @Override
    @Transactional
    public void delete(int id) throws Exception {
        storyDao.deleteLikes(id);
        storyDao.deletePhotos(id);
        storyDao.delete(id);
    }

    @Transactional
    @Override
    public void addPhotos(List<Photo> photos) throws Exception {
        for (Photo photo : photos) {
            storyDao.insertPhoto(photo);
        }
    }



    @Override
    public void deletePhoto(int photoId, int userId) throws Exception {
        // 삭제할 Photo 가져오기
        Photo photo = photoService.getPhoto(photoId);
        if (photo == null) {
            throw new Exception("없는 사진입니다.");
        }

        Story story = storyDao.findByStoryId(photo.getStoryId());
        if (story == null) {
            throw new Exception("없는 스토리입니다.");
        }

        if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        // Photo 파일 삭제
        try {
            storageService.delete(folderName + photo.getPath());

        } catch (Exception e) {
            throw new Exception("파일 삭제 실패로 인한 DB 삭제 중단");
        }

        storyDao.deletePhoto(photoId);
    }



    @Override
    public List<StoryListDTO> listAllShareStories(int userId) throws Exception {
        // list : 공유 허용된 모든 스토리 목록   (로그인 ID에 따라 좋아요 상태 변화)

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAll()) {
            if (!story.isShare()) {
                continue;
            }

            StoryListDTO storyListDTO = storyMapper.toStoryListDTO(story);

            Photo mainPhoto = storyDao.getPhotos(story.getId())
                .stream()
                .filter(Photo::isMainPhoto)
                .findFirst()
                .orElse(null);

            if (mainPhoto != null) {
                storyListDTO.setMainPhoto(storyMapper.toPhotoDTO(mainPhoto));
            }

            storyListDTO.setLikeCount(likeService.countLikes(story.getId()));
            storyListDTO.setLikeStatus(likeService.getStatus(story.getId(), userId));

            storyListDTOs.add(storyListDTO);
        }

        return storyListDTOs;
    }


    @Override
    public StoryViewDTO viewShareStory(int storyId) throws Exception {

        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        } else if (!story.isShare()) {
            throw new Exception("공개된 스토리가 아닙니다.");
        }

        StoryViewDTO storyViewDTO = storyMapper.toStoryViewDTO(story);

        List<Photo> photos = storyDao.getPhotos(storyId);
        if (photos == null) {
            throw new Exception("사진이 존재하지 않습니다.");
        }

        List<PhotoDTO> photoDTOS = new ArrayList<>();
        for (Photo photo : photos) {
            photoDTOS.add(storyMapper.toPhotoDTO(photo));
        }

        storyViewDTO.setPhotos(photoDTOS);

        return storyViewDTO;
    }


    @Override
    public List<StoryListDTO> listAllMyStories(int userId) throws Exception {
        // list : 내가 올린 모든 스토리 목록  (공유 여부 상관 없음)
        // (로그인 ID에 따라 좋아요 상태 변화)

        User loginUser = userService.findUser(userId);
        if (loginUser == null) {
            throw new Exception("유효하지 않는 사용자입니다.");
        }

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAllByUserId(userId)) {
            StoryListDTO storyListDTO = storyMapper.toStoryListDTO(story);

            Photo mainPhoto = storyDao.getPhotos(story.getId())
                .stream()
                .filter(Photo::isMainPhoto)
                .findFirst()
                .orElse(null);

            if (mainPhoto != null) {
                storyListDTO.setMainPhoto(storyMapper.toPhotoDTO(mainPhoto));
            }

            storyListDTO.setLikeCount(likeService.countLikes(story.getId()));
            storyListDTO.setLikeStatus(likeService.getStatus(story.getId(), userId));

            storyListDTOs.add(storyListDTO);
        }

        return storyListDTOs;
    }

    @Override
    public StoryViewDTO viewMyStory(int storyId, int userId) throws Exception {

        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        } else if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        StoryViewDTO storyViewDTO = storyMapper.toStoryViewDTO(story);


        List<Photo> photos = storyDao.getPhotos(story.getId());
        if (photos == null) {
            throw new Exception("사진이 존재하지 않습니다.");
        }

        List<PhotoDTO> photoDTOS = new ArrayList<>();
        for (Photo photo : photos) {
            photoDTOS.add(storyMapper.toPhotoDTO(photo));
        }

        storyViewDTO.setPhotos(photoDTOS);

        return storyViewDTO;
    }

    @Override
    public List<StoryListDTO> listAllMyLikeStories(int userId) throws Exception {
        // list : 내가 좋아요한 스토리 목록  (공개임 경우만)
        // (로그인 ID에 따라 좋아요 상태 변화)

        User loginUser = userService.findUser(userId);
        if (loginUser == null) {
            throw new Exception("유효하지 않는 사용자입니다.");
        }

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAllByMyLike(userId)) {
            if (!story.isShare()) {
                continue;
            }
            StoryListDTO storyListDTO = storyMapper.toStoryListDTO(story);

            Photo mainPhoto = storyDao.getPhotos(story.getId())
                .stream()
                .filter(Photo::isMainPhoto)
                .findFirst()
                .orElse(null);

            if (mainPhoto != null) {
                storyListDTO.setMainPhoto(storyMapper.toPhotoDTO(mainPhoto));
            }

            storyListDTO.setLikeCount(likeService.countLikes(story.getId()));
            storyListDTO.setLikeStatus(likeService.getStatus(story.getId(), userId));

            storyListDTOs.add(storyListDTO);
        }

        return storyListDTOs;
    }

}
