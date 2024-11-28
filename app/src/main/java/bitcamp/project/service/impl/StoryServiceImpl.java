package bitcamp.project.service.impl;

import bitcamp.project.dao.StoryDao;
import bitcamp.project.dto.*;
import bitcamp.project.mapper.StoryMapper;
import bitcamp.project.service.*;
import bitcamp.project.vo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
        Location location = getLocation(addStoryRequestDTO.getFirstName(), addStoryRequestDTO.getSecondName());


        if (files == null || files.length == 0 || Arrays.stream(files).allMatch(file -> file.getSize() == 0)) {
            throw new Exception("사진 입력 필요");
        }

        // Story DB 처리
        Story story = addStoryRequestDTO.toStory(location);
        storyDao.insert(story);


        // 신규 Photo DB 처리
        List<Photo> uploadedPhotos = uploadNewPhotos(files, story.getId());
        uploadedPhotos.get(addStoryRequestDTO.getMainPhotoIndex()).setMainPhoto(true);
        photoService.addPhotos(uploadedPhotos);

        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", uploadedPhotos);

        return ResponseEntity.ok(response);
    }


    @Transactional
    @Override
    public ResponseEntity<Map<String, Object>> update(
        UpdateStoryRequestDTO updateStoryRequestDTO,
        MultipartFile[] files,
        String photosJson) throws Exception {

        Story oldStory = validateStory(updateStoryRequestDTO.getOldStoryId());

        if (oldStory.getUser() == null || oldStory.getUser().getId() != updateStoryRequestDTO.getLoginUser().getId()) {
            throw new Exception("접근 권한이 없습니다.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Photo> oldPhotos = photoService.getPhotosByStoryId(updateStoryRequestDTO.getOldStoryId());
        List<Photo> newPhotos = objectMapper.readValue(photosJson, new TypeReference<List<Photo>>() {});

        // (1) files가 들어오지 않거나 들어와도 내부에 파일이 없는 경우  =>  예외 발생
        // (2) 유효하지 않는 (손상된) 파일  =>  예외 발생
        if ((files == null || files.length == 0) && oldPhotos.isEmpty()) {
            throw new Exception("스토리에 최소 한 개의 사진이 필요합니다.");
        }

        if (files != null && Arrays.stream(files).allMatch(file -> file == null || file.getSize() == 0) && oldPhotos.isEmpty()) {
            throw new Exception("유효하지 않는 파일입니다.");
        }

        // 위치 정보
        Location location = getLocation(updateStoryRequestDTO.getFirstName(), updateStoryRequestDTO.getSecondName());

        // 삭제할 Photo가 있다면 Ncp 파일 삭제
        HashSet<Integer> newPhotosSet
            = newPhotos.stream().map(Photo::getId).collect(Collectors.toCollection(HashSet::new));

        List<Photo> photosToDelete = oldPhotos.stream()
            .filter(photo -> !newPhotosSet.contains(photo.getId())).toList();

        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println(photosToDelete);
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");
        System.out.println("**********************************************");

        try {
            for (Photo photo : photosToDelete) {
                storageService.delete(folderName + photo.getPath());
            }
        } catch (Exception e) {
            throw new Exception("파일 삭제 실패");
        }

        // 스토리 DB 처리
        Story story = updateStoryRequestDTO.toStory(location);
        story.setId(updateStoryRequestDTO.getOldStoryId());
        storyDao.update(story);


        // 기존 Photo DB 삭제
        photoService.deletePhotos(updateStoryRequestDTO.getOldStoryId());


        // 새로운 Photo DB 처리
//        List<Photo> uploadedPhotos = newPhotos;
        newPhotos.addAll(uploadNewPhotos(files, story.getId()));
//        uploadedPhotos.addAll(newPhotos);
        photoService.addPhotos(newPhotos);


        // 메인 Photo 처리
        List<Photo> photos = photoService.getPhotosByStoryId(story.getId());
        if (updateStoryRequestDTO.getMainPhotoIndex() >= photos.size()) {
            throw new Exception("유효하지 않는 메인 index 값");
        }
        for (int i = 0; i < photos.size(); i++) {
            if (i == updateStoryRequestDTO.getMainPhotoIndex()) {
                photos.get(i).setMainPhoto(true);
            } else {
                photos.get(i).setMainPhoto(false);
            }
        }
        photoService.updatePhotos(photos);

        // 아래 코드는 제거용 (POSTMAN 용 코드)
        List<Photo> updatedPhotos = photoService.getPhotosByStoryId(story.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("story", story);
        response.put("photos", updatedPhotos);

        return ResponseEntity.ok(response);
    }



    @Override
    @Transactional
    public void delete(int storyId, int userId) throws Exception {

        Story story = validateStory(storyId);

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
    @Transactional
    public void adminDelete(int storyId) throws Exception {

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
    public List<Story> getStories(int userId) throws Exception {
        return storyDao.findAllByUserId(userId);
    }

    @Override
    public void removePhoto(int photoId, int userId) throws Exception {
        // 삭제할 Photo 가져오기
        Photo photo = photoService.getPhoto(photoId);
        if (photo == null) {
            throw new Exception("없는 사진입니다.");
        }

        Story story = validateStory(photo.getStoryId());

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
    public boolean hasMoreStories(int userId, String title, String userNickname, boolean share, int currentSize) throws Exception {
        int totalStories = 0;
        if(share) {
            if (title != null && !title.isEmpty()) {
                totalStories = storyDao.countAllShareStoriesByTitle(userId, title, share);
            } else if (userNickname != null && !userNickname.isEmpty()) {
                totalStories = storyDao.countAllShareStoriesByNickname(userId, userNickname, share);
            } else {
                totalStories = storyDao.countShareStories(userId, title, userNickname, share);
            }
        }else{
            if (title != null && !title.isEmpty()){
                totalStories = storyDao.countAllMyStoriesByTitle(userId, title);
            }else{
                totalStories = storyDao.countAllMyStories(userId);
            }
        }
        return currentSize < totalStories;
    }

    @Override
    public int countAllStories() throws Exception {
        return storyDao.countAllStories();
    }

    @Override
    public int countStories(int userId, String title, String userNickname, boolean share) throws Exception {
        if(share) {
            if (title != null && !title.isEmpty()) {
                return storyDao.countAllShareStoriesByTitle(userId, title, share);
            } else if (userNickname != null && !userNickname.isEmpty()) {
                return storyDao.countAllShareStoriesByNickname(userId, userNickname, share);
            } else {
                return storyDao.countShareStories(userId, title, userNickname, share);
            }
        }else{
            if (title != null && !title.isEmpty()){
                return storyDao.countAllMyStoriesByTitle(userId, title);
            }else{
                return storyDao.countAllMyStories(userId);
            }
        }
    }


    @Override
    public List<StoryListDTO> listAllStories(
        int userId, String title, String userNickname, boolean share, String sortBy, int limit) throws Exception {

        List<Story> stories;

        // "과거순" 처리
        if (sortBy != null && sortBy.equals("과거순")) {
            if (share) {
                stories = storyDao.findAllShareStoriesAsc(title, userNickname, limit); // 공유된 과거순 정렬

            } else {
                stories = storyDao.findAllByUserIdAsc(userId, title, limit); // 내 스토리 과거순 정렬
            }

        } else {
            // "좋아요순" 처리
            boolean sortByLikes = sortBy != null && sortBy.equals("좋아요순");

            if (share) {
                stories = storyDao.findAllShareStories(title, userNickname, sortByLikes, limit);  // 공유스토리 (+ 검색, 정렬)
            } else {
                stories = storyDao.findAllByUserIdLimitTest(userId, title, sortByLikes, limit);  // 내스토리 (+ 검색, 정렬)
            }
        }

        // StoryListDTO를 만들어서 List에 담는다.
        List<StoryListDTO> storyListDTOs = new ArrayList<>();
        for (Story story : stories) {
            storyListDTOs.add(convertToStoryListDTO(story, userId));
        }


        return storyListDTOs;
    }

    @Override
    public List<StoryListDTO> adminListAllStories(int userId, String title, String userNickname, String sortBy, int limit) throws Exception {
        List<Story> stories;

        if(sortBy != null && sortBy.equals("과거순")){
            stories = storyDao.findAllStoriesAsc(title, userNickname, limit);
        }else{
            // 정렬 판단
            boolean sortByLikes = sortBy != null && sortBy.equals("좋아요순");

            stories = storyDao.findAllStories(title, userNickname, sortByLikes, limit);  // 공유스토리 (+ 검색, 정렬)
        }

        // StoryListDTO를 만들어서 List에 담는다.
        List<StoryListDTO> storyListDTOs = new ArrayList<>();
        for (Story story : stories) {
            storyListDTOs.add(convertToStoryListDTO(story, userId));
        }

        return storyListDTOs;
    }

    @Override
    public StoryViewDTO viewStory(int storyId, int userId, boolean share) throws Exception {

        Story story = validateStory(storyId);

        if (share && !story.isShare()) {
            throw new Exception("공개된 스토리가 아닙니다.");
        }

        if (!share && story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        StoryViewDTO storyViewDTO = storyMapper.toStoryViewDTO(story);

        List<Photo> photos = photoService.getPhotosByStoryId(story.getId());
        if (photos == null) {
            throw new Exception("사진이 존재하지 않습니다.");
        }

        int currentIndex = 0;
        int mainPhotoIndex = 0;
        List<PhotoDTO> photoDTOS = new ArrayList<>();
        for (Photo photo : photos) {
            if (photo.isMainPhoto()) {
                mainPhotoIndex = currentIndex;
            }
            photoDTOS.add(storyMapper.toPhotoDTO(photo));
            currentIndex++;
        }

        storyViewDTO.setMainPhotoIndex(mainPhotoIndex);
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
        Story story = validateStory(storyId);

        if (story.getUser().getId() != userId) {
            throw new Exception("접근 권한이 없습니다.");
        }

        story.setShare(!story.isShare());
        storyDao.update(story);
        return story;
    }


    @Override
    public void changeShare(BatchUpdateRequestDTO batchUpdateRequestDTO, int userId)
        throws Exception {

            Story story = validateStory(batchUpdateRequestDTO.getStoryId());
            if (story.getUser().getId() != userId) {
                throw new Exception("수정 권한이 없습니다");
            }

            if (batchUpdateRequestDTO.getAction().equals("add")) {
                enableShare(story);

            } else if (batchUpdateRequestDTO.getAction().equals("delete")) {
                disableShare(story);
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


    private Story validateStory(int storyId) throws Exception {
        Story story = storyDao.findByStoryId(storyId);
        if (story == null) {
            throw new Exception("없는 스토리입니다.");
        }
        return story;
    }


    private Location getLocation(String firstName, String secondName) throws Exception {
        Location location = locationService.findByFullName(firstName, secondName);
        if (location == null) {
            throw new Exception("위치 정보 없음");
        }
        return location;
    }


    private List<Photo> uploadNewPhotos(MultipartFile[] files, int storyId) throws Exception {

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
                photo.setStoryId(storyId);
                photo.setMainPhoto(false);
                photo.setPath(filename);

                photos.add(photo);
            }
        }

        return photos;
    }

}
