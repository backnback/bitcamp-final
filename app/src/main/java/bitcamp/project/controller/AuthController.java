package bitcamp.project.controller;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign")
public class AuthController {

    @Autowired
    private UserService userService;

    @Transactional
    @PostMapping("up")
    public void up(User user){
        userService.add(user);
    }
}
