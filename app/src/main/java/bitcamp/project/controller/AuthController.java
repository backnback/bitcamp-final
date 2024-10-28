package bitcamp.project.controller;

import bitcamp.project.service.StorageService;
import bitcamp.project.service.UserService;
import bitcamp.project.service.impl.FileServiceImpl;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/sign")
public class AuthController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserService userService;

    private String folderName = "user/";

    @Transactional
    @PostMapping("up")
    public void up(@RequestParam("profileImage") MultipartFile file, User user)throws Exception{
       if (file.isEmpty()){
           user.setPath(null);
       }else{
//           FileServiceImpl fileService = new FileServiceImpl();
//           user.setPath("/images/" + fileService.saveFile(file));
           // 클라이언트가 보낸 파일을 저장할 때 다른 파일 이름과 충돌나지 않도록 임의의 새 파일 이름을 생성한다.
           String filename = UUID.randomUUID().toString();

           HashMap<String, Object> options = new HashMap<>();
           options.put(StorageService.CONTENT_TYPE, file.getContentType());
           storageService.upload(folderName + filename,
                   file.getInputStream(),
                   options);
           user.setPath(filename);
       }
        userService.add(user);
    }

    @PostMapping("in")
    public User in(@RequestParam String email, @RequestParam String password) throws Exception {
        User user = userService.findByEmailAndPassword(email, password);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 실패: 이메일 또는 비밀번호를 확인해주세요.");
        }
        System.out.println("성공 했어요");
        return user;
    }

}