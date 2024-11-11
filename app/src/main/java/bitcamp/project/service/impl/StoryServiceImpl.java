package bitcamp.project.service.impl;

import bitcamp.project.dao.StoryDao;
import bitcamp.project.dto.*;
import bitcamp.project.mapper.StoryMapper;
import bitcamp.project.service.*;
import bitcamp.project.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    private final StoryDao storyDao;
    private final LikeService likeService;
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

        // 위치 정보
        Location location = locationService.findByFullName(addStoryRequestDTO.getFirstName(),
            addStoryRequestDTO.getSecondName());
        if (location == null) {
            throw new Exception("위치 정보 없음");
        }

        if (files == null || files.length == 0 || Arrays.stream(files).allMatch(file -> file.getSize() == 0)) {
            throw new Exception("사진 입력 필요");
        }

        // Story에 위치 정보 삽입
        Story story = addStoryRequestDTO.toStory(location);
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
        photoService.addPhotos(photos);

        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
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


        if (oldStory.getUser() == null || oldStory.getUser().getId() != updateStoryRequestDTO.getLoginUser().getId()) {
            throw new Exception("접근 권한이 없습니다.");
        }

        List<Photo> oldPhotos = photoService.getPhotosByStoryId(updateStoryRequestDTO.getOldStoryId());


        // 기존 사진이 있는 경우  =>  예외 없음
        // 기존 사진이 없는 겨우
        // (1) files가 들어오지 않거나 들어와도 내부에 파일이 없는 경우  =>  예외 발생
        // (2) 유효하지 않는 (손상된) 파일  =>  예외 발생
        if ((files == null || files.length == 0) && oldPhotos.isEmpty()) {
            throw new Exception("스토리에 최소 한 개의 사진이 필요합니다.");
        }

        if (files != null && Arrays.stream(files).allMatch(file -> file == null || file.getSize() == 0) && oldPhotos.isEmpty()) {
            throw new Exception("유효하지 않는 파일입니다.");
        }


        Location location = locationService.findByFullName(updateStoryRequestDTO.getFirstName(),
            updateStoryRequestDTO.getSecondName());
        if (location == null) {
            throw new Exception("위치 정보 없음");
        }

        Story story = updateStoryRequestDTO.toStory(location);
        story.setId(updateStoryRequestDTO.getOldStoryId());
        storyDao.update(story);


        // Photo 정보
        List<Photo> photos = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                if (file == null || file.getSize() == 0) {
                    continue; // null 파일 또는 크기가 0인 파일은 건너뜀
                }

                // 첨부파일 저장
                String filename = UUID.randomUUID().toString();

                HashMap<String, Object> options = new HashMap<>();
                options.put(StorageService.CONTENT_TYPE, file.getContentType());
                storageService.upload(folderName + filename, file.getInputStream(), options);

                Photo photo = new Photo();
                photo.setStoryId(story.getId());
                photo.setMainPhoto(false);
                photo.setPath(filename);

                photos.add(photo);
            }
        }

        // Photo DB 처리
        photoService.addPhotos(photos);

        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", photos);

        return ResponseEntity.ok(response);
    }



    @Override
    @Transactional
    public void delete(int storyId, int userId) throws Exception {

        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }

        if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        for (Photo photo : photoService.getPhotosByStoryId(storyId)) {
            try {
                // 첨부파일 삭제
                storageService.delete(folderName + photo.getPath());

            } catch (Exception e) {
                System.out.printf("%s 파일 ", photo.getPath());
                throw new Exception("삭제 실패 !");
            }
        }

        likeService.deleteLikes(storyId);
        photoService.deletePhotos(storyId);
        storyDao.delete(storyId);
    }


    @Override
    public void removePhoto(int photoId, int userId) throws Exception {
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

        photoService.deletePhoto(photoId);
    }


    @Override
    public List<StoryListDTO> listAllShareStories(int userId) throws Exception {
        // list : 공유 허용된 모든 스토리 목록   (로그인 ID에 따라 좋아요 상태 변화)

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAll()) {
            if (!story.isShare()) {
                continue;
            }

            // story와 userId를 사용하여 스토리, 사진정보, 좋아요 정보로
            // StoryListDTO를 만들어서 List에 담는다.
            storyListDTOs.add(convertToStoryListDTO(story, userId));
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

        List<Photo> photos = photoService.getPhotosByStoryId(storyId);
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

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAllByUserId(userId)) {

            // story와 userId를 사용하여 스토리, 사진정보, 좋아요 정보로
            // StoryListDTO를 만들어서 List에 담는다.
            storyListDTOs.add(convertToStoryListDTO(story, userId));
        }

        return storyListDTOs;
    }


    @Override
    public List<StoryListDTO> listAllMyStoriesByTitle(int userId, String title) throws Exception {
        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAllByUserIdAndTitle(userId, title)) {
            storyListDTOs.add(convertToStoryListDTO(story, userId));
        }

        return storyListDTOs;
    }


    @Override
    public StoryViewDTO viewMyStory(int storyId, int userId) throws Exception {

        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("스토리가 존재하지 않습니다.");
        }

        StoryViewDTO storyViewDTO = storyMapper.toStoryViewDTO(story);

        List<Photo> photos = photoService.getPhotosByStoryId(story.getId());
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

        List<StoryListDTO> storyListDTOs = new ArrayList<>();

        for (Story story : storyDao.findAllByMyLike(userId)) {
            if (!story.isShare()) {
                continue;
            }

            // story와 userId를 사용하여 스토리, 사진정보, 좋아요 정보로
            // StoryListDTO를 만들어서 List에 담는다.
            storyListDTOs.add(convertToStoryListDTO(story, userId));
        }

        return storyListDTOs;
    }


    @Override
    public Story changeShare(int storyId, int userId) throws Exception {
        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("스토리 정보 없음");
        }

        if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        story.setShare(!story.isShare());
        storyDao.update(story);
        return story;
    }


    @Override
    public void batchUpdateShares(List<BatchUpdateRequestDTO> batchUpdateRequestDTOs, int userId)
        throws Exception {

        for (BatchUpdateRequestDTO batchUpdateRequestDTO : batchUpdateRequestDTOs) {

            Story story = storyDao.findByStoryId(batchUpdateRequestDTO.getStoryId());
            if (story == null) {
                throw new Exception("없는 스토리입니다");
            } else if (story.getUser().getId() != userId) {
                throw new Exception("수정 권한이 없습니다");
            }

            if (batchUpdateRequestDTO.getAction().equals("add")) {
                enableShare(story);

            } else if (batchUpdateRequestDTO.getAction().equals("delete")) {
                disableShare(story);
            }
        }

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


    private void enableShare(Story story) throws Exception {
        if (story.isShare()) {
            System.out.printf("%d번은 이미 활성화되었습니다!\n", story.getId());
            return;
        }

        story.setShare(true);
        storyDao.update(story);
    }


    private void disableShare(Story story) throws Exception {
        if (!story.isShare()) {
            System.out.printf("%d번은 이미 비활성화되었습니다!\n", story.getId());
            return;
        }

        story.setShare(false);
        storyDao.update(story);
    }


}
