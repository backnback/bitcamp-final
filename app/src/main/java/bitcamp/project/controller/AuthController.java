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
           user.setPath("");
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
    public String findEmail(@RequestBody User user) throws Exception {
        String email = user.getEmail();
        email = String.valueOf(userService.findByEmailAndPassword(email));
        if (email != null){
            int atIndex = email.indexOf("@");
            String localPart = email.substring(0, atIndex);
            String domainPart = email.substring(atIndex);
            return localPart.charAt(0) + "*".repeat(localPart.length() - 1) + domainPart;
        }else {
            return "해당 이메일을 찾을 수 가 없습니다. 다시 입력해 주세요!";
        }
    }

    @PostMapping("emailverification")
    public Boolean EmailVerification(@RequestBody EmailDTO emailDTO, HttpServletRequest request) throws Exception {
        String email = emailDTO.getEmail();
        String authCode = mailService.joinEmail(email);
        if (authCode != null){
            HttpSession session = request.getSession();;
            session.setAttribute("authCode", authCode);
            return true;
        }
        return false;
    }

    @PostMapping("verificationcode")
    public Boolean VerificationCode(@RequestBody EmailDTO emailDTO, HttpServletRequest request) throws Exception {
        String authCode = emailDTO.getAuthCode();
        System.out.println(authCode);
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute("authCode");
        if (code.equals(authCode)){
            return  true;
        }
        return false;
    }

}
