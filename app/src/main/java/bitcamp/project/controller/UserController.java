package bitcamp.project.controller;

import bitcamp.project.annotation.LoginUser;
import bitcamp.project.dto.ViewUserDTO;
import bitcamp.project.security.JwtTokenProvider;
import bitcamp.project.service.StorageService;
import bitcamp.project.service.UserService;
import bitcamp.project.vo.JwtToken;
import bitcamp.project.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private StorageService storageService;

    private String folderName = "user/";

    @GetMapping("list")
    public List<User> list() throws Exception {
        List<User> users = userService.list();
        return users;
    }

    @GetMapping("finduser")
    public ResponseEntity<?> getUser(@LoginUser User loginUser) throws Exception {
        try {
            User user = userService.findUser(loginUser.getId());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.ok("유저 정보를 가지고오는데 문제가 생겼습니다");
        }
    }

    @PostMapping("update")
    public ResponseEntity<?> update(@LoginUser User loginUser,
                                    @RequestPart(value = "profileImage", required = false) MultipartFile file,
                                    @RequestPart(value = "nickname", required = false) String nickname,
                                    @RequestPart(value = "password", required = false) String password) throws Exception {
        User old = userService.findUser(loginUser.getId());

        // 프로필 이미지 파일 처리
        if (file != null && file.getSize() > 0) {
            storageService.delete(folderName + old.getPath());

            String filename = UUID.randomUUID().toString();
            HashMap<String, Object> options = new HashMap<>();
            options.put(StorageService.CONTENT_TYPE, file.getContentType());
            storageService.upload(folderName + filename,
                    file.getInputStream(),
                    options);

            old.setPath(filename);
        }

        old.setNickname(nickname);

        if (password != null) {
            old.setPassword(userService.encodePassword(password));
        } else {

        }

        // 사용자 정보 업데이트
        if (userService.update(loginUser.getId(), old)) {
            // 기존 방식으로 토큰 생성
            JwtToken newToken = userService.generateTokenWithoutAuthentication(old);
            return ResponseEntity.ok(newToken); // 새로운 토큰 반환
        } else {
            return ResponseEntity
                    .badRequest()
                    .body("업데이트에 실패했습니다");
        }
    }

    @DeleteMapping("delete")
    public boolean delete(@LoginUser User loginUser) throws Exception {



        User old = userService.findUser(loginUser.getId());

        if (userService.delete(loginUser.getId())) {
            storageService.delete(folderName + old.getPath());
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("userauthentication")
    public Boolean userAuthentication(@LoginUser User loginUser, @RequestBody Map<String, String> getPassword) throws Exception {
        String password = getPassword.get("password");

        return userService.userAuthentication(loginUser.getId(), password);
    }

    @PostMapping("viewuser")
    public ViewUserDTO findUserAndFile(@LoginUser User loginUser) throws Exception {
        User user = userService.findUser(loginUser.getId());  // 로그인한 사용자의 정보 가져오기
        ViewUserDTO sendInfo = new ViewUserDTO();
        sendInfo.setNickname(user.getNickname());  // 닉네임 설정

        // 파일 경로가 null이면 파일을 설정하지 않음
        if (user.getPath() == null) {
            sendInfo.setFilename(null);
            sendInfo.setFile(null);
            return sendInfo;  // 파일이 없을 경우, 닉네임만 반환
        }

        // 파일 경로를 설정
        String filePath = folderName + user.getPath();
        InputStream fileStream = storageService.download(filePath);  // 파일 다운로드

        // 파일을 byte[]로 읽기
        byte[] fileBytes = fileStream.readAllBytes();

        // 파일 이름을 설정
        sendInfo.setFilename(user.getPath());
        sendInfo.setFile(fileBytes);  // fileBytes로 파일 데이터 저장

        return sendInfo;  // UserDTO 반환
    }

    @PostMapping("refreshtoken")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) throws Exception {
        String refreshToken = request.get("refreshToken");
        return userService.generateRefreshToken(refreshToken);
    }
}
