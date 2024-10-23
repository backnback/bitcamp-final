package bitcamp.project.controller;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/sign")
public class AuthController {

    @Autowired
    private UserService userService;

    @Transactional
    @PostMapping("up")
    public void up(@RequestParam("profileImage") MultipartFile file, User user)throws Exception{
       if (file.isEmpty()){
           user.setPath(null);
       }else{
           String fileName = UUID.randomUUID().toString() + ".png";

// Spring Boot static folder path
           String currentPath = System.getProperty("user.dir");
           String uploadDir = currentPath + "/src/main/resources/static/images/";
           System.out.println("Upload Directory: " + uploadDir);
           Path uploadPath = Paths.get(uploadDir);

           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }

           Path filePath = uploadPath.resolve(fileName);
           file.transferTo(filePath.toFile());

// 파일 경로를 "/images/{fileName}" 형식으로 설정
           user.setPath("/images/" + fileName);
           userService.add(user);
       }
    }

    @PostMapping("in")
    public User in(@RequestParam String email, @RequestParam String password, HttpSession session) throws Exception {
        User user = userService.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");
        }
        User loginUser =  userService.findUser(user.getId());
        session.setAttribute("loginUser", loginUser);
        System.out.println("성공 했어요");
        return user;
    }

}