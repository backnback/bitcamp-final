package bitcamp.project.controller;

import bitcamp.project.service.UserService;
import bitcamp.project.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        return users;
    }

    @GetMapping("finduser")
    public User get(@RequestParam int id) throws Exception{
        User user = userService.findUser(id);
        return user;
    }

    @PostMapping("update")
    public boolean update(@RequestParam("no") int id, @RequestBody User user) throws Exception{
        return userService.update(id, user);
    }

    @DeleteMapping("delete/{id}")
    public boolean delete(@PathVariable int id) throws Exception{
        return userService.delete(id);
    }
}
