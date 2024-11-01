package bitcamp.project.controller;

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

}