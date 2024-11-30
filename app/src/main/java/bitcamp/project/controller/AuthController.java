package bitcamp.project.controller;

import bitcamp.project.dto.EmailDTO;
import bitcamp.project.service.MailService;
import bitcamp.project.service.StorageService;
import bitcamp.project.service.UserService;
import bitcamp.project.service.impl.FileServiceImpl;
import bitcamp.project.vo.JwtToken;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/sign")
public class AuthController {

    private String code;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    private String folderName = "user/";

    @Transactional
    @PostMapping("up")
    public void up(@RequestParam(name = "profileImage", required = false) MultipartFile file, User user)throws Exception{
       if (file != null && !file.isEmpty()){
           String filename = UUID.randomUUID().toString();

           HashMap<String, Object> options = new HashMap<>();
           options.put(StorageService.CONTENT_TYPE, file.getContentType());
           storageService.upload(folderName + filename,
                   file.getInputStream(),
                   options);
           user.setPath(filename);
       }else{
           user.setPath("default.png");
       }
        user.setRole("ROLE_USER");
        user.setPassword(userService.encodePassword(user.getPassword()));
        userService.add(user);
    }

    @PostMapping("in")
    public ResponseEntity<JwtToken> in(@RequestBody User user) throws Exception {

        String email = user.getEmail();

        String password = user.getPassword();

        JwtToken token = userService.makeToken(email, password);
        return ResponseEntity.ok(token);
    }

    @PostMapping("findemail")
    public Boolean findEmail(@RequestBody User user) throws Exception {
        User finduser = userService.findByEmailAndPassword(user.getEmail());
        if (finduser != null) {
            System.out.println(user.getEmail());
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("emailverification")
    public Boolean EmailVerification(@RequestBody EmailDTO emailDTO) throws Exception {

        // user가 null이 아니면 이메일을 확인하고 인증코드 생성
        String email = emailDTO.getEmail();
        String authCode = mailService.joinEmail(email);

        if (authCode != null) {
            code = authCode;
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("passwordEmailverification")
    public String PasswordEmailVerification(@RequestBody EmailDTO emailDTO) throws Exception {

        // user가 null인지 확인
        if (!userService.checkEmail(emailDTO.getEmail())) {
            System.out.println("여기에 오니까 뜨는거 아닐까?");
            return "가입되지않은이메일"; // user가 null인 경우 처리
        }


        // user가 null이 아니면 이메일을 확인하고 인증코드 생성
        String email = emailDTO.getEmail();
        String authCode = mailService.joinEmail(email);

        if (authCode != null) {
            code = authCode;
            return "성공";
        } else {
            return "코드생성실패";
        }
    }

    @PostMapping("verificationcode")
    public Boolean VerificationCode(@RequestBody EmailDTO emailDTO) throws Exception {
        String authCode = emailDTO.getAuthCode();
        System.out.println(authCode);
        System.out.println(code);

        if (code.equals(authCode)){
            code = null;
            return  true;
        }
        return false;
    }

    @PostMapping("newpassword")
    public Boolean newPassword(@RequestBody User user) throws Exception {
        user.setPassword(userService.encodePassword(user.getPassword()));
        return userService.updatePassword(user.getEmail(), user.getPassword());
    }

}
