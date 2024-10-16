package bitcamp.project.controller;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("list")
    public List<User> list() throws Exception{
        List<User> users =  userService.list();

        for(User user : users){
            System.out.println(user.getNickname());
        }

        return users;
    }
}
